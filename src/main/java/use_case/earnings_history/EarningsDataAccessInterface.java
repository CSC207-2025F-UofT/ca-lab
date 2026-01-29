package use_case.earnings_history;

import entity.EarningsRecord;

import java.io.IOException;
import java.util.List;

/**
 * Gateway used by the interactor to fetch earnings data.
 *
 * Contract:
 *  - return null  -> symbol not found / unsupported (API returns "error")
 *  - return empty -> symbol valid but no earnings data
 *  - throw IOException -> network / HTTP issue
 */
public interface EarningsDataAccessInterface {

    List<EarningsRecord> getEarningsFor(String symbol) throws IOException;
}
