package data_access.stock_search;

import entity.StockQuote;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.stock_search.StockSearchDataAccessInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FinnhubStockSearchDataAccessObject implements StockSearchDataAccessInterface {
    private static final String QUOTE_URL = "https://finnhub.io/api/v1/quote";
    private static final String SEARCH_URL = "https://finnhub.io/api/v1/search";
    private static final String PROFILE_URL = "https://finnhub.io/api/v1/stock/profile2";

    private final String apiKey;

    public FinnhubStockSearchDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public StockQuote loadQuote(String userQuery) throws Exception {
        if (userQuery == null || userQuery.isBlank()) {
            throw new IllegalArgumentException("Symbol or name must not be empty.");
        }

        String query = userQuery.trim();

        String resolvedSymbol = resolveSymbol(query);
        if (resolvedSymbol == null || resolvedSymbol.isBlank()) {
            throw new IllegalStateException("No matching symbol found for: " + query);
        }

        StringBuilder urlBuilder = new StringBuilder(QUOTE_URL)
                .append("?symbol=")
                .append(URLEncoder.encode(resolvedSymbol, StandardCharsets.UTF_8))
                .append("&token=")
                .append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

        JSONObject quoteJson = fetchJsonObject(urlBuilder.toString());

        // Load basic profile information for extra context
        StringBuilder profileUrlBuilder = new StringBuilder(PROFILE_URL)
                .append("?symbol=")
                .append(URLEncoder.encode(resolvedSymbol, StandardCharsets.UTF_8))
                .append("&token=")
                .append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

        JSONObject profileJson = fetchJsonObject(profileUrlBuilder.toString());

        return parseQuote(resolvedSymbol, quoteJson, profileJson);
    }

    /**
     * Resolve an arbitrary query (symbol, name, ISIN, CUSIP) to a concrete ticker symbol
     * using Finnhub's /search endpoint.
     */
    private String resolveSymbol(String query) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(SEARCH_URL)
                .append("?q=")
                .append(URLEncoder.encode(query, StandardCharsets.UTF_8))
                .append("&token=")
                .append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

        JSONObject json = fetchJsonObject(urlBuilder.toString());
        if (json == null) {
            return null;
        }

        JSONArray results = json.optJSONArray("result");
        if (results == null || results.length() == 0) {
            return null;
        }

        // 1) Exact symbol match (case-insensitive)
        for (int i = 0; i < results.length(); i++) {
            JSONObject r = results.optJSONObject(i);
            if (r == null) continue;
            String sym = r.optString("symbol", "");
            if (!sym.isEmpty() && sym.equalsIgnoreCase(query)) {
                return sym;
            }
        }

        String lowerQuery = query.toLowerCase();

        // 2) Description contains query (case-insensitive)
        for (int i = 0; i < results.length(); i++) {
            JSONObject r = results.optJSONObject(i);
            if (r == null) continue;
            String desc = r.optString("description", "");
            if (!desc.isEmpty() && desc.toLowerCase().contains(lowerQuery)) {
                String sym = r.optString("symbol", "");
                if (!sym.isEmpty()) {
                    return sym;
                }
            }
        }

        // 3) Fallback to the first result's symbol
        JSONObject first = results.optJSONObject(0);
        if (first == null) {
            return null;
        }
        String sym = first.optString("symbol", "");
        return sym.isEmpty() ? null : sym;
    }

    private JSONObject fetchJsonObject(String urlString) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                String errorBody = readStream(connection.getErrorStream());
                throw new IOException("HTTP error " + status + " from Finnhub: " + errorBody);
            }

            inputStream = connection.getInputStream();
            String body = readStream(inputStream);
            return new JSONObject(body);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readStream(InputStream stream) throws IOException {
        if (stream == null) return "";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }

    private StockQuote parseQuote(String symbol, JSONObject quoteJson, JSONObject profileJson) {
        if (quoteJson == null) {
            throw new IllegalStateException("Empty response from Finnhub quote endpoint.");
        }

        double current = quoteJson.optDouble("c", Double.NaN);
        double open = quoteJson.optDouble("o", Double.NaN);
        double high = quoteJson.optDouble("h", Double.NaN);
        double low = quoteJson.optDouble("l", Double.NaN);
        double prevClose = quoteJson.optDouble("pc", Double.NaN);
        long t = quoteJson.optLong("t", 0L);

        String companyName = null;
        String exchange = null;
        String industry = null;
        double marketCap = Double.NaN;

        if (profileJson != null) {
            // Fields from /stock/profile2
            companyName = profileJson.optString("name", null);
            exchange = profileJson.optString("exchange", null);
            // Finnhub industry field can be "finnhubIndustry" or sector-like labels
            industry = profileJson.optString("finnhubIndustry", null);
            marketCap = profileJson.optDouble("marketCapitalization", Double.NaN);
        }

        return new StockQuote(symbol, companyName, exchange, industry, marketCap,
                current, open, high, low, prevClose, t);
    }
}
