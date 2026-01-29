package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        // First, try to load from environment variables
        loadFromEnvironment();

        // Then, try to load from config.properties in classpath
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                System.out.println("Loaded configuration from config.properties");
            }
        } catch (IOException ex) {
            System.out.println("No config.properties found, using environment variables only");
        }
    }

    private static void loadFromEnvironment() {
        // Spotify configuration from environment variables
        String clientId = System.getenv("SPOTIFY_CLIENT_ID");
        String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
        String redirectUri = System.getenv("SPOTIFY_REDIRECT_URI");

        if (clientId != null) properties.setProperty("spotify.client.id", clientId);
        if (clientSecret != null) properties.setProperty("spotify.client.secret", clientSecret);
        if (redirectUri != null) properties.setProperty("spotify.redirect.uri", redirectUri);

        // Set defaults if environment variables are not set
        if (clientId == null) {
            System.err.println("WARNING: SPOTIFY_CLIENT_ID environment variable not set");
            properties.setProperty("spotify.client.id", "default_client_id");
        }
        if (clientSecret == null) {
            System.err.println("WARNING: SPOTIFY_CLIENT_SECRET environment variable not set");
            properties.setProperty("spotify.client.secret", "default_client_secret");
        }
        if (redirectUri == null) {
            properties.setProperty("spotify.redirect.uri", "http://localhost:8080/callback");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}