package stock.core.market;

import stock.core.pool.BasicThreadPool;

/**
 * Core Class that guards access to a singleton instance of the designed stock market
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */
public class StockMarketSingleton {

    private final static StockMarket ourInstance = getStockMarketInstance();

    /**
     * @return StockMarketSingleton instance
     */
    public static StockMarket getInstance() {
        return ourInstance;
    }

    private static StockMarket getStockMarketInstance() {
        return new BasicStockMarketBuilder()
                .setEnabledLogger(true)
                .setThreadPool(new BasicThreadPool(10))
                .build();
    }

}
