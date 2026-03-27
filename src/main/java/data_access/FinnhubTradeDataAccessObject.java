package data_access;

import entity.Trade;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import use_case.trade.TradeDataAccessInterface;
import use_case.trade.TradeListener;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Finnhub-specific implementation of the TradeDataAccessInterface.
 * This class belongs to the data_access / infrastructure layer and knows about WebSockets and JSON format.
 */
public class FinnhubTradeDataAccessObject implements TradeDataAccessInterface {

    private static final String API_KEY = "d4977ehr01qshn3kvpt0d4977ehr01qshn3kvptg";
    private static final String WEB_SOCKET_URL = "wss://ws.finnhub.io?token="
            + API_KEY;
    private static final int UNSUBSCRIBE_DELAY_MS = 100;
    private static final int RECONNECT_DELAY_MS = 200;
    private static final int CLOSE_CODE_NORMAL = 1000;
    private static final int CONNECT_TIMEOUT_SECONDS = 10;
    private static final int PING_INTERVAL_SECONDS = 25;
    private static final int DATA_ARRAY_OFFSET = 8;

    private WebSocket webSocket;
    private OkHttpClient client;
    private TradeListener listener;
    private String currentSymbol;
    private boolean hasReceivedPing = false;

    @Override
    public void connect(String symbol, TradeListener listener) {
        this.listener = listener;

        if (symbol != null) {
            symbol = symbol.trim().toUpperCase();
        }

        if (webSocket != null) {
            if (currentSymbol != null && !currentSymbol.isEmpty()) {
                try {
                    String unsubscribeMsg = String.format(
                            "{\"type\":\"unsubscribe\",\"symbol\":\"%s\"}",
                            currentSymbol);
                    webSocket.send(unsubscribeMsg);
                    try {
                        Thread.sleep(UNSUBSCRIBE_DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    // Error unsubscribing
                }
            }
            webSocket.close(CLOSE_CODE_NORMAL, "Reconnecting");
            webSocket = null;
            try {
                Thread.sleep(RECONNECT_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (symbol != null) {
            this.currentSymbol = symbol.trim();
        } else {
            this.currentSymbol = null;
        }
        this.hasReceivedPing = false;

        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .pingInterval(PING_INTERVAL_SECONDS, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder().url(WEB_SOCKET_URL).build();

        if (listener != null) {
            listener.onStatusChanged("Status: Connecting...", false);
        }

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket ws, Response response) {
                String subscribeMsg = String.format(
                        "{\"type\":\"subscribe\",\"symbol\":\"%s\"}",
                        currentSymbol);
                ws.send(subscribeMsg);
            }

            @Override
            public void onMessage(WebSocket ws, String text) {
                processMessage(text);
            }
        });
    }

    @Override
    public void disconnect() {
        if (webSocket != null) {
            if (currentSymbol != null && !currentSymbol.isEmpty()) {
                try {
                    String unsubscribeMsg = String.format(
                            "{\"type\":\"unsubscribe\",\"symbol\":\"%s\"}",
                            currentSymbol);
                    webSocket.send(unsubscribeMsg);
                    try {
                        Thread.sleep(UNSUBSCRIBE_DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    // Error unsubscribing
                }
            }
            webSocket.close(CLOSE_CODE_NORMAL, "User disconnect");
            webSocket = null;
        }
        client = null;
        currentSymbol = null;
    }

    /**
     * Small internal JSON parsing helper: extracts a value from a JSON string using basic String methods.
     * Kept here in the infrastructure layer so UI and domain remain free of parsing details.
     */
    private String extractValue(String json, String key) {
        String keySearch = "\"" + key + "\":";
        int keyIndex = json.indexOf(keySearch);

        if (keyIndex == -1) {
            return null;
        }

        int valueStart = keyIndex + keySearch.length();
        char firstChar = json.charAt(valueStart);

        if (firstChar == '"') {
            valueStart++;
            int valueEnd = json.indexOf('"', valueStart);
            if (valueEnd != -1) {
                return json.substring(valueStart, valueEnd);
            }
        } else {
            int valueEnd = valueStart;
            while (valueEnd < json.length()
                    && (Character.isDigit(json.charAt(valueEnd))
                    || json.charAt(valueEnd) == '.'
                    || json.charAt(valueEnd) == '-')) {
                valueEnd++;
            }
            int commaIndex = json.indexOf(',', valueStart);
            int braceIndex = json.indexOf('}', valueStart);

            int terminationIndex = json.length();
            if (commaIndex != -1) {
                terminationIndex = Math.min(terminationIndex, commaIndex);
            }
            if (braceIndex != -1) {
                terminationIndex = Math.min(terminationIndex, braceIndex);
            }

            if (terminationIndex > valueStart) {
                return json.substring(valueStart, terminationIndex).trim();
            }
        }
        return null;
    }

    /**
     * Processes trade data that comes in array format from Finnhub.
     * Format: {"type":"trade","data":[{"s":"SYMBOL","p":price,"v":volume,"t":timestamp},...]}
     * Or: [{"s":"SYMBOL","p":price,"v":volume,"t":timestamp},...]
     */
    private void processTradeArray(String jsonMessage) {
        try {
            String dataArray = null;
            if (jsonMessage.contains("\"data\":[")) {
                int dataStart = jsonMessage.indexOf("\"data\":[")
                        + DATA_ARRAY_OFFSET;
                int bracketCount = 1;
                int dataEnd = dataStart;
                while (dataEnd < jsonMessage.length() && bracketCount > 0) {
                    if (jsonMessage.charAt(dataEnd) == '[') {
                        bracketCount++;
                    }
                    if (jsonMessage.charAt(dataEnd) == ']') {
                        bracketCount--;
                    }
                    dataEnd++;
                }
                if (bracketCount == 0) {
                    dataArray = jsonMessage.substring(dataStart, dataEnd - 1);
                }
            } else if (jsonMessage.trim().startsWith("[")) {
                String trimmedMessage = jsonMessage.trim();
                dataArray = trimmedMessage.substring(1,
                        trimmedMessage.length() - 1);
            }

            if (dataArray == null || dataArray.isEmpty()) {
                return;
            }
            String[] tradeObjects = dataArray.split("\\},\\{");
            for (int i = 0; i < tradeObjects.length; i++) {
                String tradeObj = tradeObjects[i];
                tradeObj = tradeObj.replaceFirst("^\\{?", "{").replaceFirst("\\}?$", "}");
                if (!tradeObj.startsWith("{")) {
                    tradeObj = "{" + tradeObj;
                }
                if (!tradeObj.endsWith("}")) {
                    tradeObj = tradeObj + "}";
                }

                String symbol = extractValue(tradeObj, "s");
                String normalizedReceivedSymbol = (symbol != null) ? symbol.trim() : null;
                String normalizedCurrentSymbol = (currentSymbol != null) ? currentSymbol.trim() : null;

                if (normalizedCurrentSymbol != null
                        && normalizedReceivedSymbol != null
                        && normalizedCurrentSymbol
                        .equalsIgnoreCase(normalizedReceivedSymbol)) {

                    String priceStr = extractValue(tradeObj, "p");
                    double price = (priceStr != null) ? Double.parseDouble(priceStr) : 0.0;

                    String volumeStr = extractValue(tradeObj, "v");
                    double volume = (volumeStr != null) ? Double.parseDouble(volumeStr) : 0.0;

                    String timestampStr = extractValue(tradeObj, "t");
                    long timestamp = (timestampStr != null) ? Long.parseLong(timestampStr) : 0L;

                    Instant ts = timestamp > 0 ? Instant.ofEpochMilli(timestamp) : null;

                    if (listener != null) {
                        listener.onStatusChanged("Status: Connected to "
                                + currentSymbol, false);
                        Trade trade = new Trade(symbol, price, volume, ts);
                        listener.onTrade(trade);
                    }
                }
            }
        } catch (Exception e) {
            // Error processing trade array
        }
    }

    /**
     * Parses the incoming JSON message using manual string manipulation and dispatches Trade domain objects.
     * Based on working Finnhub WebSocket example.
     */
    private void processMessage(String jsonMessage) {
        try {
            if (jsonMessage.contains("\"type\":\"ping\"")) {
                hasReceivedPing = true;
                return;
            }

            if (jsonMessage.contains("\"type\":\"trade\"")) {
                if (jsonMessage.contains("\"data\":[")) {
                    processTradeArray(jsonMessage);
                    return;
                }

                String symbol = extractValue(jsonMessage, "s");

                String priceStr = extractValue(jsonMessage, "p");
                double price;
                if (priceStr != null) {
                    price = Double.parseDouble(priceStr);
                } else {
                    price = 0.0;
                }

                String volumeStr = extractValue(jsonMessage, "v");
                double volume;
                if (volumeStr != null) {
                    volume = Double.parseDouble(volumeStr);
                } else {
                    volume = 0.0;
                }

                String timestampStr = extractValue(jsonMessage, "t");
                long timestamp;
                if (timestampStr != null) {
                    timestamp = Long.parseLong(timestampStr);
                } else {
                    timestamp = 0L;
                }

                Instant ts;
                if (timestamp > 0) {
                    ts = Instant.ofEpochMilli(timestamp);
                } else {
                    ts = null;
                }


                if (listener != null) {
                    listener.onStatusChanged("Status: Connected to "
                            + currentSymbol, false);
                    Trade trade = new Trade(symbol, price, volume, ts);
                    listener.onTrade(trade);
                }
            } else if (jsonMessage.contains("\"type\":\"error\"")) {
                String errorMsg = extractValue(jsonMessage, "msg");
                if (listener != null) {
                    String errorText = errorMsg != null ? errorMsg : "Unknown error";
                    listener.onStatusChanged("Status: Error - " + errorText, true);
                }
            } else {
                if (jsonMessage.trim().startsWith("[")
                        && jsonMessage.contains("\"s\"")
                        && jsonMessage.contains("\"p\"")) {
                    processTradeArray(jsonMessage);
                }
            }
        } catch (Exception e) {
            // Error processing JSON message
        }
    }
}

