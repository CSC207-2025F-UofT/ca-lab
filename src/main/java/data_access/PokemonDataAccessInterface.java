package data_access;

import entity.Pokemon;
import use_case.mysterypokemon.PokemonNotFoundException;

public interface PokemonDataAccessInterface {
    Pokemon getByName(String name) throws PokemonNotFoundException;
}
