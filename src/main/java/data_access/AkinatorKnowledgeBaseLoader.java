package data_access;

import entity.PokemonTrait;
import entity.SimplePokemonProfile;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Downloads a batch of Pokémon entries from PokéAPI and converts them into
 * {@link SimplePokemonProfile} objects with coarse traits for the Akinator
 * interactor. This lets the game reason about hundreds of Pokémon instead of
 * the short, hard-coded list we started with.
 */
public class AkinatorKnowledgeBaseLoader {

    private static final String LIST_URL = "https://pokeapi.co/api/v2/pokemon?limit=%d&offset=0";
    private static final String POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String SPECIES_URL = "https://pokeapi.co/api/v2/pokemon-species/";

    private static final Set<String> STARTER_NAMES = new HashSet<>(Arrays.asList(
            "bulbasaur", "charmander", "squirtle",
            "chikorita", "cyndaquil", "totodile",
            "treecko", "torchic", "mudkip",
            "turtwig", "chimchar", "piplup",
            "snivy", "tepig", "oshawott",
            "chespin", "fennekin", "froakie",
            "rowlet", "litten", "popplio",
            "grookey", "scorbunny", "sobble",
            "sprigatito", "fuecoco", "quaxly"
    ));

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Downloads up to {@code limit} Pokémon (starting from #1) and returns
     * derived profiles. If any download fails, it will be skipped to keep
     * things moving; if everything fails, the method throws an exception.
     */
    public List<SimplePokemonProfile> load(int limit) throws IOException {
        List<String> names = fetchNames(Math.max(1, limit));
        List<SimplePokemonProfile> profiles = new ArrayList<>();

        for (String name : names) {
            try {
                JSONObject pokemon = fetchObject(POKEMON_URL + name);
                JSONObject species = fetchObject(SPECIES_URL + name);
                EnumSet<PokemonTrait> traits = deriveTraits(name, pokemon, species);
                profiles.add(SimplePokemonProfile.of(name, traits.toArray(new PokemonTrait[0])));
            } catch (IOException ex) {
                System.err.println("Pokénator: skipped " + name + " (" + ex.getMessage() + ")");
            }
        }

        if (profiles.isEmpty()) {
            throw new IOException("No Pokémon profiles could be downloaded.");
        }

        return profiles;
    }

    private List<String> fetchNames(int limit) throws IOException {
        Request request = new Request.Builder()
                .url(String.format(Locale.US, LIST_URL, limit))
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to fetch Pokémon list (" + response.code() + ")");
            }
            JSONObject body = new JSONObject(response.body().string());
            JSONArray results = body.getJSONArray("results");
            List<String> names = new ArrayList<>(results.length());
            for (int i = 0; i < results.length(); i++) {
                names.add(results.getJSONObject(i).getString("name"));
            }
            return names;
        }
    }

    private JSONObject fetchObject(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("API call failed (" + response.code() + ")");
            }
            return new JSONObject(response.body().string());
        }
    }

    private EnumSet<PokemonTrait> deriveTraits(String name,
                                               JSONObject pokemon,
                                               JSONObject species) {
        EnumSet<PokemonTrait> traits = EnumSet.noneOf(PokemonTrait.class);
        String normalized = name.toLowerCase(Locale.US);

        if (STARTER_NAMES.contains(normalized)) {
            traits.add(PokemonTrait.STARTER);
        }

        JSONArray types = pokemon.getJSONArray("types");
        if (types.length() > 1) {
            traits.add(PokemonTrait.DUAL_TYPE);
        }
        if (hasType(types, "flying")) {
            traits.add(PokemonTrait.FLYING_OR_FLOATING);
        }
        if (hasType(types, "water")) {
            traits.add(PokemonTrait.AQUATIC);
        }
        if (hasType(types, "psychic")) {
            traits.add(PokemonTrait.PSYCHIC_TYPE);
        }
        if (hasType(types, "ghost") || hasType(types, "dark")) {
            traits.add(PokemonTrait.SPOOKY);
        }

        if (species.optBoolean("is_legendary") || species.optBoolean("is_mythical")) {
            traits.add(PokemonTrait.LEGENDARY);
            traits.add(PokemonTrait.FULLY_EVOLVED);
        }

        if (isLikelyFinalStage(pokemon, species)) {
            traits.add(PokemonTrait.FULLY_EVOLVED);
        }

        if (isHumanLike(species)) {
            traits.add(PokemonTrait.HUMANOID);
        }

        if (isCute(pokemon, species)) {
            traits.add(PokemonTrait.CUTE_MASCOT);
        }

        if (isDefensive(pokemon)) {
            traits.add(PokemonTrait.DEFENSIVE_TANK);
        }

        if (isKantoOriginal(species)) {
            traits.add(PokemonTrait.KANTO_ORIGINAL);
        }

        return traits;
    }

    private boolean hasType(JSONArray types, String sought) {
        for (int i = 0; i < types.length(); i++) {
            JSONObject type = types.getJSONObject(i).getJSONObject("type");
            if (sought.equals(type.getString("name"))) {
                return true;
            }
        }
        return false;
    }

    private boolean isHumanLike(JSONObject species) {
        JSONArray eggGroups = species.optJSONArray("egg_groups");
        if (eggGroups == null) {
            return false;
        }
        for (int i = 0; i < eggGroups.length(); i++) {
            if ("human-like".equals(eggGroups.getJSONObject(i).getString("name"))) {
                return true;
            }
        }
        return false;
    }

    private boolean isCute(JSONObject pokemon, JSONObject species) {
        if (species.optBoolean("is_baby")) {
            return true;
        }
        int heightDm = pokemon.optInt("height");
        int weightHg = pokemon.optInt("weight");
        return heightDm <= 10 && weightHg <= 200;
    }

    private boolean isDefensive(JSONObject pokemon) {
        int defense = getStat(pokemon, "defense");
        int hp = getStat(pokemon, "hp");
        return defense >= 100 || hp >= 110;
    }

    private boolean isLikelyFinalStage(JSONObject pokemon, JSONObject species) {
        if (species.optBoolean("is_baby")) {
            return false;
        }
        // Legendary/Mythical already handled earlier.
        if (species.isNull("evolves_from_species")) {
            return false;
        }
        int baseExperience = pokemon.optInt("base_experience");
        return baseExperience >= 185;
    }

    private boolean isKantoOriginal(JSONObject species) {
        JSONObject generation = species.optJSONObject("generation");
        return generation != null && "generation-i".equals(generation.optString("name"));
    }

    private int getStat(JSONObject pokemon, String statName) {
        JSONArray stats = pokemon.getJSONArray("stats");
        for (int i = 0; i < stats.length(); i++) {
            JSONObject stat = stats.getJSONObject(i);
            if (statName.equals(stat.getJSONObject("stat").getString("name"))) {
                return stat.optInt("base_stat");
            }
        }
        return 0;
    }
}
