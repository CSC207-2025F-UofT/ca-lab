package data_access;

import entity.MarketStatus;
import org.json.JSONObject;
import org.json.JSONTokener;
import use_case.market_status.MarketStatusDataAccessInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MarketStatusDataAccessObject implements MarketStatusDataAccessInterface {
    private static final String BASE_URL = "https://finnhub.io/api/v1/stock/market-status";

    private final String apiKey;

    public MarketStatusDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public MarketStatus loadStatus() throws Exception {
        String urlString = BASE_URL
                + "?exchange=US"
                + "&token=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        JSONObject json = fetchJsonObject(urlString);
        return parseMarketStatus(json);
    }

    // Perform HTTP GET and return the response as a JSONObject.
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
            JSONTokener tokener = new JSONTokener(inputStream);
            return new JSONObject(tokener);

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

    // Convert errorMessage
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


    private MarketStatus parseMarketStatus(JSONObject obj) {
        if (obj == null) {
            return null;
        }

        String exchange = obj.optString("exchange", "US");
        boolean isOpen = obj.optBoolean("isOpen", false);
        String session = obj.optString("session", "");
        String holiday = obj.optString("holiday", "");
        long timestamp = obj.optLong("t", 0L);
        String timezone = obj.optString("timezone", "");

        return new MarketStatus(
                exchange,
                isOpen,
                session,
                holiday,
                timestamp,
                timezone
        );
    }
}
