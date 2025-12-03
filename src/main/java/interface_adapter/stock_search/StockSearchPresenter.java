package interface_adapter.stock_search;

import use_case.stock_search.StockSearchOutputBoundary;
import use_case.stock_search.StockSearchResponseModel;

public class StockSearchPresenter implements StockSearchOutputBoundary {

    private final StockSearchViewModel viewModel;

    public StockSearchPresenter(StockSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(StockSearchResponseModel responseModel) {
        StringBuilder sb = new StringBuilder();
        String symbol = responseModel.getSymbol();
        String name = responseModel.getCompanyName();
        String exchange = responseModel.getExchange();
        String industry = responseModel.getIndustry();
        double marketCap = responseModel.getMarketCap();

        if (name != null && !name.isBlank()) {
            sb.append(symbol != null ? symbol : "");
            sb.append(" - ");
            sb.append(name);
        } else {
            sb.append(symbol != null ? symbol : "");
        }

        if (exchange != null && !exchange.isBlank()) {
            sb.append(" (").append(exchange).append(")");
        }
        sb.append("\n");

        if (industry != null && !industry.isBlank()) {
            sb.append("Industry: ").append(industry).append("\n");
        }

        if (!Double.isNaN(marketCap) && marketCap > 0) {
            sb.append("Market Cap: ").append(marketCap).append(" B").append("\n");
        }

        sb.append("\n");
        sb.append("Current: ").append(responseModel.getCurrentPrice()).append("\n");
        sb.append("Open:    ").append(responseModel.getOpen()).append("\n");
        sb.append("High:    ").append(responseModel.getHigh()).append("\n");
        sb.append("Low:     ").append(responseModel.getLow()).append("\n");
        sb.append("Prev:    ").append(responseModel.getPreviousClose()).append("\n");

        viewModel.setInfoText(sb.toString());
        viewModel.setErrorMessage(null);
        viewModel.setLoading(false);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setInfoText(null);
        viewModel.setErrorMessage(errorMessage);
        viewModel.setLoading(false);
        viewModel.firePropertyChanged();
    }
}
