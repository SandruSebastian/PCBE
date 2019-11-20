package stock.core.market;

import com.sun.istack.internal.NotNull;
import stock.exceptions.StockMarketAlreadyRunningException;
import stock.models.Demand;
import stock.models.Supply;

/**
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */
public interface StockMarket {

    /**
     * @return the instance of the StockMarket
     * @throws StockMarketAlreadyRunningException if the stock market is already running
     */
    StockMarket run() throws StockMarketAlreadyRunningException;

    /**
     * @param supply to be added to the stock market
     */
    void addSupply(@NotNull Supply supply);

    /**
     * @param demand to be added on the stock market
     */
    void addDemand(@NotNull Demand demand);

    /**
     * @return current history of the stock market exchanges
     */
    String printHistory();

}
