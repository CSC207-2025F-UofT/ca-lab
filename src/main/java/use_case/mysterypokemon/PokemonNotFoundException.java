package use_case.mysterypokemon;

public class PokemonNotFoundException extends Exception{
    public PokemonNotFoundException(String name) {
        super("Pokemon not found: " + name);
    }

}
