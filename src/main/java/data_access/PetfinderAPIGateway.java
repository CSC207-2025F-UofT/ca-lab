package data_access;

import entity.Pet;
import okhttp3.*;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

public class PetfinderAPIGateway implements PetAPIGatewayInterface {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiToken;

    public PetfinderAPIGateway(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public List<Pet> fetchPets(String type, String gender) {
        List<Pet> pets = new ArrayList<>();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.petfinder.com/v2/animals").newBuilder();
        if (!type.isBlank()) urlBuilder.addQueryParameter("type", type);
        if (!gender.isBlank()) urlBuilder.addQueryParameter("gender", gender);
        urlBuilder.addQueryParameter("limit", "100");

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", "Bearer " + apiToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonString = response.body().string();
            JSONObject root = new JSONObject(jsonString);

            JSONArray animals = root.getJSONArray("animals");
            for (int i = 0; i < animals.length(); i++) {
                JSONObject a = animals.getJSONObject(i);

                String id = String.valueOf(a.getInt("id"));
                String name = a.optString("name", "Unknown");
                String species = a.optString("species", "Unknown");
                JSONObject breeds = a.getJSONObject("breeds");
                String breed = breeds.optString("primary", "Unknown");
                String age = a.optString("age", "Unknown");
                String g = a.optString("gender", "Unknown");
                String size = a.optString("size", "Unknown");

                String contact = "";
                if (a.has("contact")) {
                    JSONObject c = a.getJSONObject("contact");
                    contact = c.optString("email", "");
                }

                pets.add(new Pet(id, name, species, breed, age, g, size, contact));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pets;
    }

    @Override
    public Pet fetchPetById(String petId) {
        // Optional for later use
        return null;
    }
}

