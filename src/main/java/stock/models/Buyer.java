package stock.models;

import stock.core.market.StockMarket;

/**
 * Buyer Class
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */

public class Buyer extends StockPerson {

    /**
     * @param instanceIdentifier not unique
     * @param stockMarket        the given StockMarket
     */
    public Buyer(String instanceIdentifier, StockMarket stockMarket) {
        super(instanceIdentifier, stockMarket);
    }

    /**
     * @param cost future balance implementations
     */
    @Override
    void notify(double cost) {
        // System.out.println("stock.models.Buyer " + super.getIdentifier() + " bought an action with " + cost);
    }

    /**
     * This method will start a thread that will iterate over the StockMarket supplies in order
     * to buy a new supply. It has a counter for every iteration, let's say for e.g if the counter
     * reaches the 2000 the iteration, there is no supply that can match his demand
     * There is room for improvement. For a fact, is not the buyer job to remove consumed supplies or
     * demands, the StockMarket Thread should do it.
     *
     * @param demand to be added
     */
    public void addDemand(final Demand demand) {
        final Buyer self = this;
        new Thread(new Runnable() {
            public void run() {
                synchronized (self.stockMarket) {
                    self.stockMarket.addDemand(demand);
                }
                Thread.currentThread().interrupt();
            }
        }).start();
    }

}
