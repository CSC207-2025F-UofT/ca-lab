package data_access;


import entity.Pokemon;
import data_access.TypeFetcher;

import java.io.IOException;
import java.util.*;


public class TypeMultiplierCalculator {

    private final TypeFetcher typeFetcher;

    public TypeMultiplierCalculator(TypeFetcher typeFetcher) {
        this.typeFetcher = typeFetcher;
    }

    /**
     * Attacker main type vs defender
     */
    public double calcMult(Pokemon attack, Pokemon defend) throws IOException {
        ArrayList<String> atkTypes = attack.getTypes();
        ArrayList<String> defTypes = defend.getTypes();

        String atkMainType = atkTypes.get(0);

        HashMap<String, ArrayList<String>> atkMainTypeMults = typeFetcher.getTypeMult(atkMainType);
        ArrayList<String> double_damage = atkMainTypeMults.get("double_damage_to");
        ArrayList<String> half_damage = atkMainTypeMults.get("half_damage_to");
        ArrayList<String> no_damage = atkMainTypeMults.get("no_damage_to");

        double multiplier = 1.0;

        for (int i = 0; i < defTypes.size(); i ++) {
            String defType = defTypes.get(i);

            if (double_damage.contains(defType)) {
                multiplier *= 2;
            } else if (half_damage.contains(defType)) {
                multiplier *= 0.5;
            } else if (no_damage.contains(defType)) {
                multiplier *= 0;
            }
        }

        return multiplier;
    }

}
