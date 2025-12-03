package use_case.market_status;

import entity.MarketStatus;

public class MarketStatusInteractor implements MarketStatusInputBoundary{
    private final MarketStatusDataAccessInterface dataAccess;
    private final MarketStatusOutputBoundary presenter;

    public MarketStatusInteractor(MarketStatusDataAccessInterface dataAccess,
                                  MarketStatusOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        try {
            MarketStatus status = dataAccess.loadStatus();

            if (status == null) {
                presenter.prepareFailView("No market status available.");
                return;
            }

            MarketStatusResponseModel response = new MarketStatusResponseModel(status);
            presenter.prepareSuccessView(response);

        } catch (Exception e) {
            presenter.prepareFailView("Unable to load market status: " + e.getMessage());
        }
    }
}
