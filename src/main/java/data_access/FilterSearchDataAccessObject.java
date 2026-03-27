package data_access;

import entity.Stock;
import use_case.filter_search.FilterSearchDataAccessInterface;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.List;

public class FilterSearchDataAccessObject implements FilterSearchDataAccessInterface {
    private static final String BASE_URL = "https://finnhub.io/api/v1";

    private final String apiKey;

    public FilterSearchDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<Stock> loadStocks(String exchange, String mic, String securityType, String currency) throws Exception {
        if (exchange == null || exchange.isBlank()) {
            throw new IllegalArgumentException("Exchange must not be empty.");
        }
        StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append("/stock")
                .append("/symbol?exchange=")
                .append(URLEncoder.encode(exchange, StandardCharsets.UTF_8));

        if (mic != null && !mic.isBlank()) {
            urlBuilder.append("&mic=")
                    .append(URLEncoder.encode(mic, StandardCharsets.UTF_8));
        }

        if (securityType != null && !securityType.isBlank()) {
            urlBuilder.append("&securityType=")
                    .append(URLEncoder.encode(securityType, StandardCharsets.UTF_8));
        }

        if (currency != null && !currency.isBlank()) {
            urlBuilder.append("&currency=")
                    .append(URLEncoder.encode(currency, StandardCharsets.UTF_8));
        }

        urlBuilder.append("&token=").append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

        JSONArray array = fetchJsonArray(urlBuilder.toString());
        return parseStockArray(array);
    }

    private JSONArray fetchJsonArray(String urlString) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Possible timeout
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                // Read errors
                String errorBody = readStream(connection.getErrorStream());
                throw new IOException("HTTP error " + status + " from Finnhub: " + errorBody);
            }

            inputStream = connection.getInputStream();
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONArray(tokener);

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

    private List<Stock> parseStockArray(JSONArray array) {
        List<Stock> result = new ArrayList<>();
        if (array == null) {
            return result;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj == null) continue;

            String currency = obj.optString("currency", "US");
            String description = obj.optString("description", "");
            String displaySymbol = obj.optString("displaySymbol", "");
            String figi = obj.optString("figi", "");
            String symbol = obj.optString("symbol", "");
            String mic = obj.optString("mic", "");
            String type = obj.optString("type", "");

            Stock stock = new Stock(
                    currency,
                    description,
                    displaySymbol,
                    figi,
                    symbol,
                    mic,
                    type
            );
            result.add(stock);
        }
        return result;
    }
}

