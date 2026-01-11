package interface_adapter.loyalty_score;

import interface_adapter.ViewModel;



public class LoyaltyViewModel extends ViewModel<LoyaltyState> {
    public LoyaltyViewModel() {
        super("loyalty");
        setState(new LoyaltyState());
    }
}
