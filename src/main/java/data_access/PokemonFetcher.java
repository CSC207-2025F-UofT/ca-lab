package data_access;

import entity.Pokemon;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import use_case.mysterypokemon.PokemonNotFoundException;

import java.io.IOException;
import java.util.*;

public class PokemonFetcher implements PokemonDataAccessInterface{
    private final OkHttpClient client = new OkHttpClient();

    public Pokemon getByName(String pokemon) throws PokemonNotFoundException {
        String request_url = "https://pokeapi.co/api/v2/pokemon/" + pokemon.toLowerCase() + "/";
        Request request = new Request.Builder().url(request_url).build();

        try (Response response = client.newCall(request).execute()) {

            if (response.isSuccessful() && response.body() != null) {
                JSONObject responseBody = new JSONObject(response.body().string());
                String name = responseBody.getString("name");

                JSONArray typesArray = responseBody.getJSONArray("types");
                ArrayList<String> types = new ArrayList<>();
                for (int j = 0; j < typesArray.length(); j++){
                    JSONObject slot = typesArray.getJSONObject(j);
                    JSONObject typeObject = slot.getJSONObject("type");
                    String type = typeObject.getString("name");
                    types.add(type);
                }

                boolean isLegendary = name.equalsIgnoreCase("articuno") || name.equalsIgnoreCase("zapdos") ||
                        name.equalsIgnoreCase("moltres") || name.equalsIgnoreCase("mewtwo");

                boolean isMythical = name.equalsIgnoreCase("mew");

                JSONArray stats = responseBody.getJSONArray("stats");
                int totalBaseStat = 0;
                for (int i = 0; i < stats.length(); i++){
                    JSONObject statObject = stats.getJSONObject(i);
                    int base_stat = statObject.getInt("base_stat");
                    totalBaseStat += base_stat;
                }

                JSONObject sprites = responseBody.getJSONObject("sprites");
                String front_default = sprites.getString("front_default");
                String sprite_url;
                if (front_default != null){
                    sprite_url = front_default;
                } else {
                    sprite_url = "No sprite available";
                }

                return new Pokemon(name, types, isLegendary, isMythical, totalBaseStat, sprite_url);

            } else {
                throw new PokemonNotFoundException(pokemon);
            }
        } catch (IOException | JSONException event) {
            throw new PokemonNotFoundException(pokemon);
        }

    }

}
