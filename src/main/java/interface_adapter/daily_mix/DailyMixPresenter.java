package interface_adapter.daily_mix;

import use_case.daily_mix.DailyMixOutputBoundary;
import use_case.daily_mix.DailyMixOutputData;

public class DailyMixPresenter implements DailyMixOutputBoundary {

    private final DailyMixViewModel dailyMixViewModel;

    public DailyMixPresenter(DailyMixViewModel dailyMixViewModel) {
        this.dailyMixViewModel = dailyMixViewModel;
    }

    @Override
    public void prepareSuccessView(DailyMixOutputData response) {
        DailyMixState state = dailyMixViewModel.getState();
        state.setError(null);
        state.setTracks(response.getTracks());

        dailyMixViewModel.setState(state);
        dailyMixViewModel.firePropertyChange();  // propertyName = "state"
    }

    @Override
    public void prepareFailView(String error) {
        DailyMixState state = dailyMixViewModel.getState();
        state.setError(error);
        state.setTracks(null);

        dailyMixViewModel.setState(state);
        dailyMixViewModel.firePropertyChange();  // LoggedInView 收到后弹错误
    }
}

