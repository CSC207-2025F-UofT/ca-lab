package interface_adapter.market_status;

import use_case.market_status.MarketStatusInputBoundary;

public class MarketStatusController {

    private final MarketStatusInputBoundary interactor;

    public MarketStatusController(MarketStatusInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void updateStatus() {
        interactor.execute();
    }
}
