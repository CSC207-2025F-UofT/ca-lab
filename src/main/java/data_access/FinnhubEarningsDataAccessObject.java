package data_access;

import entity.EarningsRecord;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.earnings_history.EarningsDataAccessInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Data access object that calls Finnhub's earnings endpoint and returns
 * a list of EarningsRecord entities.
 *
 * Contract with EarningsDataAccessInterface:
 *  - return null  -> symbol not found / unsupported (API returns "error")
 *  - return empty -> symbol valid but no earnings data
 *  - throw IOException -> network / HTTP issue
 */
public class FinnhubEarningsDataAccessObject implements EarningsDataAccessInterface {

    private static final String API_KEY = "d4977ehr01qshn3kvpt0d4977ehr01qshn3kvptg";
    private static final String BASE_URL = "https://finnhub.io/api/v1/stock/earnings";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .build();

    @Override
    public List<EarningsRecord> getEarningsFor(String symbol) throws IOException {
        if (symbol == null || symbol.trim().isEmpty()) {
            return new ArrayList<>();
        }

        symbol = symbol.trim().toUpperCase();

        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("symbol", symbol)
                .addQueryParameter("limit", "10")
                .addQueryParameter("token", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP code " + response);
            }

            String body = response.body() != null ? response.body().string() : "";

            // Finnhub returns { "error": "..." } for bad symbols
            if (body.contains("\"error\"")) {
                return null;
            }

            return parseEarningsJson(body);
        }
    }

    // ---------------- JSON parsing helpers ----------------

    /**
     * Expected JSON:
     * [ { "period": "...", "actual": ..., "estimate": ..., "surprise": ... }, ... ]
     */
    private List<EarningsRecord> parseEarningsJson(String json) {
        List<EarningsRecord> rows = new ArrayList<>();
        if (json == null) return rows;

        String trimmed = json.trim();
        if (trimmed.length() < 2 || "[]".equals(trimmed)) return rows;

        // Remove [ and ]
        if (trimmed.startsWith("[")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("]")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }

        // Split by "},{" between objects
        String[] objects = trimmed.split("\\},\\s*\\{");

        for (String obj : objects) {
            obj = obj.trim();
            if (!obj.startsWith("{")) obj = "{" + obj;
            if (!obj.endsWith("}")) obj = obj + "}";

            String period = extractValue(obj, "period");
            String actualStr = extractValue(obj, "actual");
            String estimateStr = extractValue(obj, "estimate");
            String surpriseStr = extractValue(obj, "surprise");

            Double actual = parseDoubleOrNull(actualStr);
            Double estimate = parseDoubleOrNull(estimateStr);
            Double surprise = parseDoubleOrNull(surpriseStr);

            rows.add(new EarningsRecord(period, actual, estimate, surprise));
        }
        return rows;
    }

    private Double parseDoubleOrNull(String s) {
        if (s == null || s.isEmpty() || "null".equals(s)) return null;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractValue(String json, String key) {
        String keySearch = "\"" + key + "\":";
        int keyIndex = json.indexOf(keySearch);
        if (keyIndex == -1) return null;

        int valueStart = keyIndex + keySearch.length();
        if (valueStart >= json.length()) return null;

        char firstChar = json.charAt(valueStart);

        // String value in quotes
        if (firstChar == '"') {
            valueStart++;
            int valueEnd = json.indexOf('"', valueStart);
            if (valueEnd != -1) {
                return json.substring(valueStart, valueEnd);
            }
        } else { // number / null / boolean
            int indexComma = json.indexOf(',', valueStart);
            int indexCurly = json.indexOf('}', valueStart);

            int terminationIndex = json.length();
            if (indexComma != -1) terminationIndex = Math.min(terminationIndex, indexComma);
            if (indexCurly != -1) terminationIndex = Math.min(terminationIndex, indexCurly);

            return json.substring(valueStart, terminationIndex).trim();
        }
        return null;
    }
}
