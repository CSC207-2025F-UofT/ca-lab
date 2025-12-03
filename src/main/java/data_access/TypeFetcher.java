package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class TypeFetcher {
    private final OkHttpClient client = new OkHttpClient();

    public HashMap<String, ArrayList<String>> getTypeMult(String type) throws IOException {
        String request_url = "https://pokeapi.co/api/v2/type/" + type.toLowerCase() + "/";
        Request request = new Request.Builder().url(request_url).build();

        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful() && response.body() != null) {
                JSONObject responseBody = new JSONObject(response.body().string());
                HashMap<String, ArrayList<String>> result = new HashMap<>();
                JSONObject damage_relations = responseBody.getJSONObject("damage_relations");

                JSONArray double_damage = damage_relations.getJSONArray("double_damage_to");
                ArrayList<String> double_damage_to = parseTypes(double_damage);
                result.put("double_damage_to", double_damage_to);

                JSONArray half_damage = damage_relations.getJSONArray("half_damage_to");
                ArrayList<String> half_damage_to = parseTypes(half_damage);
                result.put("half_damage_to", half_damage_to);

                JSONArray no_damage = damage_relations.getJSONArray("no_damage_to");
                ArrayList<String> no_damage_to = parseTypes(no_damage);
                result.put("no_damage_to", no_damage_to);

                return result;
            } else {
                throw new IOException("PokéAPI returned " + response.code());
            }
        } catch (IOException | JSONException event) {
            throw new RuntimeException(event);
        }
    }

    public ArrayList<String> parseTypes(JSONArray array) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject type = array.getJSONObject(i);
            String name = type.getString("name");
            result.add(name);
        }
        return result;
    }
}


