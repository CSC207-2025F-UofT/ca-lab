package data_access;

import okhttp3.*;
import org.json.JSONObject;

public class PetfinderAuthService {

    private static final String CLIENT_ID = "98LHq0XnAgjCVHKzTakl9iTR7lnOKkOfq9b9JxlyXBhczzXP7z";
    private static final String CLIENT_SECRET = "wOuOnBiPZBpNYyB54epVnhPyZuYuZ6H9XYvsHRuk";
    private static final String TOKEN_URL = "https://api.petfinder.com/v2/oauth2/token";

    public static String getAccessToken() throws Exception {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .build();

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("❌ Failed to get token: " + response.code() + " - " + response.message());
            }

            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            String token = json.getString("access_token");
            System.out.println("🔑 Received token: " + token);
            return token;
        }
    }
}

