package entity;

/**
 * A simple entity representing a top item for a user. top_items have a name and rank
 */

public class TopItem {
    private final String name;
    private final int rank;

    public TopItem(String name, int rank) {
        this.name = name;
        this.rank = rank;
    }
    public String getName() { return this.name; }
    public int getRank() { return this.rank; }

}

