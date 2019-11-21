package stock.core.market;

import stock.core.pool.BasicThreadPool;
import stock.core.pool.ThreadPool;

/**
 * The builder class for the {@link BasicStockMarket}
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */
public class BasicStockMarketBuilder {

    private BasicStockMarket stockMarket;

    private ThreadPool defaultThreadPool = new BasicThreadPool(5);

    BasicStockMarketBuilder() {
        this.stockMarket = new BasicStockMarket();
        this.stockMarket.setEnabledLogger(true);
        this.stockMarket.setThreadPool(defaultThreadPool);
    }

    /**
     * @param enabledLogger the state of the logging system of the stock market
     * @return this instance
     */
    BasicStockMarketBuilder setEnabledLogger(boolean enabledLogger) {
        this.stockMarket.setEnabledLogger(enabledLogger);
        return this;
    }

    /**
     * @param threadPool the thread pool that is going to be used
     * @return this instance
     */
    BasicStockMarketBuilder setThreadPool(ThreadPool threadPool) {
        this.defaultThreadPool.shutdown();
        this.stockMarket.setThreadPool(threadPool);
        return this;
    }

    /**
     * @return the {@link BasicStockMarket} instance that was being built
     */
    BasicStockMarket build() {
        return this.stockMarket;
    }

}
