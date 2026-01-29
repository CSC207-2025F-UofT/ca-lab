package data_access;

import entity.NewsArticle;
import use_case.news.NewsDataAccessInterface;

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

/**
 * Data access implementation for Finnhub news endpoints using org.json.
 * <p>
 * Endpoints:
 *   - General market news:
 *   - Company news:
 */
public class NewsDataAccessObject implements NewsDataAccessInterface {

    private static final String BASE_URL = "https://finnhub.io/api/v1";

    private final String apiKey;

    public NewsDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<NewsArticle> loadMarketNews() throws Exception {
        String endpoint = BASE_URL + "/news?category=general&token=" +
                URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
        JSONArray array = fetchJsonArray(endpoint);
        return parseNewsArray(array);
    }

    @Override
    public List<NewsArticle> loadCompanyNews(String symbol,
                                             String fromDate,
                                             String toDate) throws Exception {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol must not be empty for company news.");
        }

        // It is okay if fromDate or toDate are null/blank: Finnhub may require them,
        // so we can set defaults here (for example: last 7 days)
        StringBuilder urlBuilder = new StringBuilder(BASE_URL)
                .append("/company-news")
                .append("?symbol=").append(URLEncoder.encode(symbol, StandardCharsets.UTF_8));

        if (fromDate != null && !fromDate.isBlank()) {
            urlBuilder.append("&from=").append(URLEncoder.encode(fromDate, StandardCharsets.UTF_8));
        }
        if (toDate != null && !toDate.isBlank()) {
            urlBuilder.append("&to=").append(URLEncoder.encode(toDate, StandardCharsets.UTF_8));
        }

        urlBuilder.append("&token=").append(URLEncoder.encode(apiKey, StandardCharsets.UTF_8));

        JSONArray array = fetchJsonArray(urlBuilder.toString());
        return parseNewsArray(array);
    }

    /**
     * Helper: performs HTTP GET and returns the response as a JSONArray.
     */
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

    /**
     * Helper: read an InputStream fully into a String (for error messages).
     */
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

    /**
     * Parse JSON array into a list of NewsArticle entities.
     * <p>
     * Each object typically has fields:
     *  - id (long)
     *  - category (String)
     *  - datetime (long)
     *  - headline (String)
     *  - image (String)
     *  - related (String)  // comma-separated symbols
     *  - source (String)
     *  - summary (String)
     *  - url (String)
     */
    private List<NewsArticle> parseNewsArray(JSONArray array) {
        List<NewsArticle> result = new ArrayList<>();
        if (array == null) {
            return result;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj == null) continue;

            long id = obj.optLong("id", 0L);
            String category = obj.optString("category", "");
            long datetime = obj.optLong("datetime", 0L);
            String headline = obj.optString("headline", "");
            String image = obj.optString("image", "");
            String related = obj.optString("related", "");
            String source = obj.optString("source", "");
            String summary = obj.optString("summary", "");
            String url = obj.optString("url", "");

            NewsArticle article = new NewsArticle(
                    id,
                    category,
                    datetime,
                    headline,
                    image,
                    related,
                    source,
                    summary,
                    url
            );
            result.add(article);
        }

        return result;
    }
}
