package entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Extremely small domain entity that captures the information we need for the
 * Akinator demo. We only keep a lowercase identifier and a handful of traits
 * that help the interactor narrow down the candidate list.
 */
public class SimplePokemonProfile {
    private final String name;
    private final EnumSet<PokemonTrait> traits;

    private SimplePokemonProfile(String name, EnumSet<PokemonTrait> traits) {
        this.name = name.toLowerCase();
        this.traits = traits.clone();
    }

    public static SimplePokemonProfile of(String name, PokemonTrait... traits) {
        EnumSet<PokemonTrait> set = traits.length == 0
                ? EnumSet.noneOf(PokemonTrait.class)
                : EnumSet.copyOf(Arrays.asList(traits));
        return new SimplePokemonProfile(name, set);
    }

    /**
     * Returns the name of the Pokémon.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if a Pokémon has a certain trait.
     *
     * @return whether the Pokémon has the trait
     */
    public boolean hasTrait(PokemonTrait trait) {
        return traits.contains(trait);
    }

    /**
     * Returns the traits of the Pokémon.
     *
     * @return the set of traits
     */
    public Set<PokemonTrait> getTraits() {
        return Collections.unmodifiableSet(traits);
    }
}
