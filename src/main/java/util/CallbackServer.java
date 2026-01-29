package util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * A simple HTTP server that listens for the Spotify OAuth callback
 */
public class CallbackServer {
    private HttpServer server;
    private CompletableFuture<String> codeFuture;
    private static final int PORT = 8080;
    private static final String CALLBACK_PATH = "/callback";

    public CallbackServer() throws IOException {
        this.codeFuture = new CompletableFuture<>();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext(CALLBACK_PATH, exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String code = extractCodeFromQuery(query);

            if (code != null) {
                // Send success response to browser
                String response = buildSuccessPage();
                sendResponse(exchange, 200, response);
                codeFuture.complete(code);
            } else {
                // Send error response
                String response = buildErrorPage();
                sendResponse(exchange, 400, response);
                codeFuture.completeExceptionally(new Exception("No code in callback"));
            }
        });
    }

    /**
     * Start the server and wait for the callback
     * @param timeoutSeconds how long to wait before timing out
     * @return the authorization code
     * @throws Exception if timeout or error occurs
     */
    public String startAndWaitForCode(int timeoutSeconds) throws Exception {
        server.start();
        System.out.println("Callback server started on port " + PORT);

        try {
            return codeFuture.get(timeoutSeconds, TimeUnit.SECONDS);
        } finally {
            stop();
        }
    }

    /**
     * Stop the server
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Callback server stopped");
        }
    }

    private String extractCodeFromQuery(String query) {
        if (query == null) return null;

        for (String param : query.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("code")) {
                return keyValue[1];
            }
        }
        return null;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String buildSuccessPage() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Authorization Successful</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Arial, sans-serif;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                            background: linear-gradient(135deg, #1DB954 0%, #191414 100%);
                        }
                        .container {
                            background: white;
                            padding: 40px;
                            border-radius: 10px;
                            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                            text-align: center;
                            max-width: 400px;
                        }
                        h1 {
                            color: #1DB954;
                            margin-bottom: 20px;
                        }
                        p {
                            color: #333;
                            font-size: 16px;
                        }
                        .checkmark {
                            font-size: 60px;
                            color: #1DB954;
                            margin-bottom: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="checkmark">✓</div>
                        <h1>Authorization Successful!</h1>
                        <p>Your Spotify account has been connected to Better Wrapped.</p>
                        <p>You can close this window and return to the application.</p>
                    </div>
                </body>
                </html>
                """;
    }

    private String buildErrorPage() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Authorization Failed</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Arial, sans-serif;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                            margin: 0;
                            background: linear-gradient(135deg, #E74C3C 0%, #191414 100%);
                        }
                        .container {
                            background: white;
                            padding: 40px;
                            border-radius: 10px;
                            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
                            text-align: center;
                            max-width: 400px;
                        }
                        h1 {
                            color: #E74C3C;
                            margin-bottom: 20px;
                        }
                        p {
                            color: #333;
                            font-size: 16px;
                        }
                        .error {
                            font-size: 60px;
                            color: #E74C3C;
                            margin-bottom: 20px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="error">✗</div>
                        <h1>Authorization Failed</h1>
                        <p>There was a problem connecting your Spotify account.</p>
                        <p>Please try again from the application.</p>
                    </div>
                </body>
                </html>
                """;
    }
}
