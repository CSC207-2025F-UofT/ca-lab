package interface_adapter.daily_mix;

import interface_adapter.ViewModel;

public class DailyMixViewModel extends ViewModel<DailyMixState> {

    public static final String VIEW_NAME = "daily mix";

    public DailyMixViewModel() {
        super(VIEW_NAME);
        this.setState(new DailyMixState());
    }
}

