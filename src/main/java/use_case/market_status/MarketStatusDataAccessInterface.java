package use_case.market_status;

import entity.MarketStatus;

public interface MarketStatusDataAccessInterface {
    MarketStatus loadStatus() throws Exception;
}
