package interface_adapter.earnings_history;

import entity.EarningsRecord;
import use_case.earnings_history.GetEarningsHistoryOutputBoundary;
import use_case.earnings_history.GetEarningsHistoryOutputData;

import java.util.List;

public class EarningsHistoryPresenter implements GetEarningsHistoryOutputBoundary {

    private final EarningsHistoryViewModel viewModel;

    public EarningsHistoryPresenter(EarningsHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GetEarningsHistoryOutputData outputData) {
        EarningsHistoryState state = new EarningsHistoryState(viewModel.getState());
        List<EarningsRecord> records = outputData.getRecords();

        state.setRecords(records);
        state.setMessage("", EarningsHistoryState.MessageType.NONE);
        state.setLoading(false);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareSymbolErrorView(String errorMessage) {
        EarningsHistoryState state = new EarningsHistoryState(viewModel.getState());
        state.setRecords(List.of());
        state.setMessage(errorMessage, EarningsHistoryState.MessageType.ERROR);
        state.setLoading(false);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareNoDataView(String message) {
        EarningsHistoryState state = new EarningsHistoryState(viewModel.getState());
        state.setRecords(List.of());
        state.setMessage(message, EarningsHistoryState.MessageType.WARNING);
        state.setLoading(false);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareConnectionErrorView(String message) {
        EarningsHistoryState state = new EarningsHistoryState(viewModel.getState());
        state.setMessage(message, EarningsHistoryState.MessageType.ERROR);
        state.setLoading(false);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
