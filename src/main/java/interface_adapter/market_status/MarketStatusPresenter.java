package interface_adapter.market_status;

import use_case.market_status.MarketStatusOutputBoundary;
import use_case.market_status.MarketStatusResponseModel;
import entity.MarketStatus;

public class MarketStatusPresenter implements  MarketStatusOutputBoundary {
    private final MarketStatusViewModel viewModel;

    public MarketStatusPresenter(MarketStatusViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(MarketStatusResponseModel responseModel) {
        MarketStatus status = responseModel.getMarketStatus();

        boolean open   = status.isOpen();
        String session = status.getSession();   // e.g. "pre-market", "regular", "post-market", or null
        String holiday = status.getHoliday();

        String sessionNorm = (session == null) ? "" : session.toLowerCase();
        String text;

        // Treat any "pre-..." as pre-market, any "post-..." as post-market
        if (sessionNorm.startsWith("pre")) {
            text = "US market in PRE-MARKET session";
        } else if (sessionNorm.startsWith("post")) {
            text = "US market in POST-MARKET session";
        } else if ("regular".equals(sessionNorm) || open) {
            text = "US market is OPEN (regular session)";
        } else {
            // truly closed – weekend / holiday / after hours with no session info
            if (holiday != null && !holiday.isBlank()) {
                text = "US market is CLOSED (Holiday: " + holiday + ")";
            } else {
                text = "US market is CLOSED";
            }
        }

        viewModel.setStatusText(text);
        viewModel.setOpen(open);                 // raw flag from API
        viewModel.setSession(session);
        viewModel.setHoliday(holiday);
        viewModel.setTimezone(status.getTimezone());
        viewModel.setErrorMessage(null);

        viewModel.firePropertyChanged();
    }
    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setStatusText("Market status unavailable");
        viewModel.setOpen(false);
        viewModel.setSession(null);
        viewModel.setHoliday(null);
        viewModel.setTimezone(null);
        viewModel.setErrorMessage(errorMessage);

        viewModel.firePropertyChanged();
    }
}
