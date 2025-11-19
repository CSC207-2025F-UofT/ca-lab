package interface_adapter.manage_application;

import java.util.ArrayList;
import java.util.List;

public class ManageApplicationState {

    private final List<ManageApplicationCardState> cards = new ArrayList<>();

    public List<ManageApplicationCardState> getCards() {
        return cards;
    }

    public void addCard(ManageApplicationCardState card) {
        cards.add(card);
    }
}
