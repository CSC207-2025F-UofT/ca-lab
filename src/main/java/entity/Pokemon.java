package entity;

import java.util.*;

public class Pokemon {
    private final String name;
    private final ArrayList<String> types;
    private final boolean isLegendary;
    private final boolean isMythical;
    private final int totalBaseStats;
    private final String sprite_url;


    public Pokemon(String name, ArrayList<String> types, boolean isLegendary, boolean isMythical, int totalBaseStats,
                   String spriteUrl) {
        this.name = name;
        this.types = types;
        this.isLegendary = isLegendary;
        this.isMythical = isMythical;
        this.totalBaseStats = totalBaseStats;
        sprite_url = spriteUrl;
    }

    public String getName() {return this.name;}

    public ArrayList<String> getTypes() {return this.types;}

    public boolean getLegendary() {return this.isLegendary;}

    public boolean getMythical() {return this.isMythical;}

    public int getTotalBaseStats() {return this.totalBaseStats;}

    public String getSpriteUrl() {return this.sprite_url;}
}
