package interface_adapter.earnings_history;

import use_case.earnings_history.GetEarningsHistoryInputBoundary;
import use_case.earnings_history.GetEarningsHistoryInputData;

public class EarningsHistoryController {

    private final GetEarningsHistoryInputBoundary interactor;
    private final EarningsHistoryViewModel viewModel;

    public EarningsHistoryController(GetEarningsHistoryInputBoundary interactor,
                                     EarningsHistoryViewModel viewModel) {
        this.interactor = interactor;
        this.viewModel = viewModel;
    }

    public void onLoadButtonClicked(String symbol) {
        EarningsHistoryState state = new EarningsHistoryState(viewModel.getState());
        state.setSymbol(symbol);
        state.setLoading(true);
        state.setMessage("", EarningsHistoryState.MessageType.NONE);
        viewModel.setState(state);
        viewModel.firePropertyChanged();

        // background thread
        new Thread(() -> {
            GetEarningsHistoryInputData inputData =
                    new GetEarningsHistoryInputData(symbol);
            interactor.execute(inputData);
        }).start();
    }
}
