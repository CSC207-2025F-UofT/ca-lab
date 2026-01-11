package data_access;

import entity.ArtistLoyaltyScore;
import entity.SpotifyUser;
import okhttp3.*;
import org.json.JSONObject;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.*;
import util.ConfigManager;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import java.util.HashSet;
import java.util.Set;

public class SpotifyDataAccessObject {
    private final SpotifyApi spotifyApi;
    private final String clientId;
    private final String redirectUri;
    private String codeVerifier;
    private final OkHttpClient httpClient;

    public SpotifyDataAccessObject() {
        this.clientId = ConfigManager.getProperty("spotify.client.id");
        this.redirectUri = ConfigManager.getProperty("spotify.redirect.uri");
        this.httpClient = new OkHttpClient();

        if ("default_client_id".equals(this.clientId)) {
            System.err.println("ERROR: Please set SPOTIFY_CLIENT_ID environment variable");
            System.err.println("Get it from: https://developer.spotify.com/dashboard");
        }

        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setRedirectUri(URI.create(this.redirectUri))
                .build();
    }

    private String generateCodeVerifier() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifierBytes = new byte[32];
        secureRandom.nextBytes(codeVerifierBytes);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(codeVerifierBytes);
    }

    private String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate code challenge", e);
        }
    }

    public String getAuthorizationUrl() {
        try {
            this.codeVerifier = generateCodeVerifier();
            String codeChallenge = generateCodeChallenge(codeVerifier);

            System.out.println("Generated code verifier (length: " + codeVerifier.length() + ")");
            System.out.println("Generated code challenge: " + codeChallenge);

            // Build authorization URL manually with PKCE parameters
            String scope = String.join(" ",
                    "user-library-read",
                    "user-read-recently-played",
                    "user-top-read",
                    "playlist-read-private",
                    "user-read-currently-playing",
                    "user-read-playback-state"
            );

            String authUrl = String.format(
                    "https://accounts.spotify.com/authorize?" +
                            "client_id=%s&" +
                            "response_type=code&" +
                            "redirect_uri=%s&" +
                            "code_challenge_method=S256&" +
                            "code_challenge=%s&" +
                            "scope=%s",
                    URLEncoder.encode(clientId, StandardCharsets.UTF_8),
                    URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                    URLEncoder.encode(codeChallenge, StandardCharsets.UTF_8),
                    URLEncoder.encode(scope, StandardCharsets.UTF_8)
            );

            System.out.println("Authorization URL: " + authUrl);
            return authUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SpotifyUser exchangeCodeForTokens(String authorizationCode, String username) {
        try {
            if (codeVerifier == null) {
                throw new RuntimeException("Code verifier not generated. Call getAuthorizationUrl() first.");
            }

            System.out.println("Exchanging code for tokens with PKCE...");
            System.out.println("Authorization code: " + authorizationCode);
            System.out.println("Using code verifier");

            // Exchange code for tokens using PKCE via HTTP request
            RequestBody formBody = new FormBody.Builder()
                    .add("grant_type", "authorization_code")
                    .add("code", authorizationCode)
                    .add("redirect_uri", redirectUri)
                    .add("client_id", clientId)
                    .add("code_verifier", codeVerifier)
                    .build();

            Request request = new Request.Builder()
                    .url("https://accounts.spotify.com/api/token")
                    .post(formBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    System.err.println("Token exchange failed: " + responseBody);
                    throw new RuntimeException("Token exchange failed: " + responseBody);
                }

                JSONObject json = new JSONObject(responseBody);
                String accessToken = json.getString("access_token");
                String refreshToken = json.optString("refresh_token", null);

                System.out.println("Successfully obtained access token");

                spotifyApi.setAccessToken(accessToken);
                if (refreshToken != null) {
                    spotifyApi.setRefreshToken(refreshToken);
                }

                // Get Spotify user profile
                var userProfile = spotifyApi.getCurrentUsersProfile().build().execute();
                System.out.println("Got user profile: " + userProfile.getDisplayName());

                SpotifyUser spotifyUser = new SpotifyUser(
                        userProfile.getDisplayName(),
                        accessToken,
                        refreshToken,
                        userProfile.getId()
                );

                // Clear code verifier after use
                this.codeVerifier = null;

                return spotifyUser;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to exchange authorization code: " + e.getMessage(), e);
        }
    }

    public SpotifyApi getSpotifyApiForUser(SpotifyUser user) {
        return new SpotifyApi.Builder()
                .setAccessToken(user.getAccessToken())
                .setRefreshToken(user.getRefreshToken())
                .setClientId(clientId)
                .build();
    }

    public SpotifyUser refreshAccessToken(SpotifyUser user) {
        try {
            if (user.getRefreshToken() == null) {
                throw new RuntimeException("No refresh token available");
            }

            System.out.println("Refreshing access token...");

            RequestBody formBody = new FormBody.Builder()
                    .add("grant_type", "refresh_token")
                    .add("refresh_token", user.getRefreshToken())
                    .add("client_id", clientId)
                    .build();

            Request request = new Request.Builder()
                    .url("https://accounts.spotify.com/api/token")
                    .post(formBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    System.err.println("Token refresh failed: " + responseBody);
                    throw new RuntimeException("Token refresh failed: " + responseBody);
                }

                JSONObject json = new JSONObject(responseBody);
                String accessToken = json.getString("access_token");
                String refreshToken = json.optString("refresh_token", user.getRefreshToken());

                System.out.println("Successfully refreshed access token");

                return new SpotifyUser(
                        user.getUsername(),
                        accessToken,
                        refreshToken,
                        user.getSpotifyUserId()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to refresh access token: " + e.getMessage(), e);
        }
    }

    public List<ArtistLoyaltyScore> getArtistLoyaltyScores(SpotifyUser user) {
        try {
            SpotifyApi userApi = getSpotifyApiForUser(user);

            System.out.println("Fetching saved tracks...");
            // Get saved tracks
            Map<String, Integer> artistTrackCount = new HashMap<>();
            Map<String, String> artistNames = new HashMap<>();

            Paging<SavedTrack> savedTracks = userApi.getUsersSavedTracks().limit(50).build().execute();
            System.out.println("Got " + savedTracks.getItems().length + " saved tracks");

            for (SavedTrack savedTrack : savedTracks.getItems()) {
                Track track = savedTrack.getTrack();
                for (ArtistSimplified artist : track.getArtists()) {
                    String artistId = artist.getId();
                    artistTrackCount.put(artistId, artistTrackCount.getOrDefault(artistId, 0) + 1);
                    artistNames.put(artistId, artist.getName());
                }
            }

            System.out.println("Fetching saved albums...");
            // Get saved albums
            Map<String, Integer> artistAlbumCount = new HashMap<>();
            Paging<SavedAlbum> savedAlbums = userApi.getCurrentUsersSavedAlbums().limit(50).build().execute();
            System.out.println("Got " + savedAlbums.getItems().length + " saved albums");

            for (SavedAlbum savedAlbum : savedAlbums.getItems()) {
                Album album = savedAlbum.getAlbum();
                for (ArtistSimplified artist : album.getArtists()) {
                    String artistId = artist.getId();
                    artistAlbumCount.put(artistId, artistAlbumCount.getOrDefault(artistId, 0) + 1);
                    artistNames.putIfAbsent(artistId, artist.getName());
                }
            }

            System.out.println("Fetching recently played tracks...");
            // Get recently played tracks
            Set<String> recentlyPlayedArtists = new HashSet<>();
            PagingCursorbased<PlayHistory> recentlyPlayed = userApi.getCurrentUsersRecentlyPlayedTracks()
                    .limit(50).build().execute();
            System.out.println("Got " + recentlyPlayed.getItems().length + " recently played tracks");

            for (PlayHistory playHistory : recentlyPlayed.getItems()) {
                Track track = playHistory.getTrack();
                for (ArtistSimplified artist : track.getArtists()) {
                    recentlyPlayedArtists.add(artist.getId());
                    artistNames.putIfAbsent(artist.getId(), artist.getName());
                }
            }

            // Combine all artists
            Set<String> allArtists = new HashSet<>();
            allArtists.addAll(artistTrackCount.keySet());
            allArtists.addAll(artistAlbumCount.keySet());
            allArtists.addAll(recentlyPlayedArtists);

            System.out.println("Total unique artists: " + allArtists.size());

            // Create loyalty scores
            List<ArtistLoyaltyScore> loyaltyScores = new ArrayList<>();
            for (String artistId : allArtists) {
                String artistName = artistNames.get(artistId);
                int trackCount = artistTrackCount.getOrDefault(artistId, 0);
                int albumCount = artistAlbumCount.getOrDefault(artistId, 0);
                boolean inRecent = recentlyPlayedArtists.contains(artistId);

                ArtistLoyaltyScore score = new ArtistLoyaltyScore(
                        artistName, artistId, trackCount, albumCount, inRecent
                );
                loyaltyScores.add(score);
            }

            // Sort by loyalty score descending
            loyaltyScores.sort((a, b) -> Double.compare(b.getLoyaltyScore(), a.getLoyaltyScore()));

            System.out.println("Calculated loyalty scores for " + loyaltyScores.size() + " artists");

            return loyaltyScores;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to get artist loyalty scores: " + e.getMessage(), e);
        }
    }

    public Set<String> getUserTopGenres(SpotifyUser user) {
        try {
            SpotifyApi userApi = getSpotifyApiForUser(user);

            // Get user's top artists (50, medium term)
            Paging<Artist> topArtists = userApi.getUsersTopArtists()
                    .limit(50)
                    .time_range("medium_term")
                    .build()
                    .execute();

            Set<String> genres = new HashSet<>();

            for (Artist artist : topArtists.getItems()) {
                String[] artistGenres = artist.getGenres();
                if (artistGenres != null) {
                    for (String g : artistGenres) {
                        if (g != null && !g.isBlank()) {
                            genres.add(g.toLowerCase());
                        }
                    }
                }
            }

            return genres;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch top genres: " + e.getMessage(), e);
        }
    }
    // NEW: Generate a Daily Mix of tracks based on user's saved and recently played tracks
    public List<String> generateDailyMix(SpotifyUser user, int mixSize) {
        try {
            SpotifyApi userApi = getSpotifyApiForUser(user);

            List<Track> pool = new ArrayList<>();

            // 1. Saved tracks
            Paging<SavedTrack> savedTracks = userApi.getUsersSavedTracks()
                    .limit(50)
                    .build()
                    .execute();

            for (SavedTrack savedTrack : savedTracks.getItems()) {
                pool.add(savedTrack.getTrack());
            }

            // 2. Recently played
            PagingCursorbased<PlayHistory> recentlyPlayed = userApi
                    .getCurrentUsersRecentlyPlayedTracks()
                    .limit(50)
                    .build()
                    .execute();

            for (PlayHistory history : recentlyPlayed.getItems()) {
                pool.add(history.getTrack());
            }

            if (pool.isEmpty()) {
                return Collections.emptyList();
            }

            // sample
            Collections.shuffle(pool);
            int n = Math.min(mixSize, pool.size());

            List<String> mix = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                Track t = pool.get(i);
                StringBuilder artists = new StringBuilder();
                ArtistSimplified[] artistArray = t.getArtists();
                for (int j = 0; j < artistArray.length; j++) {
                    if (j > 0) {
                        artists.append(", ");
                    }
                    artists.append(artistArray[j].getName());
                }

                // Song Name - Artist1, Artist2
                String line = String.format("%s - %s", t.getName(), artists);
                mix.add(line);
            }

            return mix;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Daily Mix: " + e.getMessage(), e);
        }
    }

}