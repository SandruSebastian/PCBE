package stock.models;

import stock.core.StockMarketSingleton;

/** Seller Class
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @since 11.19.2019
 * @version 0.0.3
 *
 */

public class Seller extends StockPerson {

    /**
     *
     * @param instanceIdentifier not unique
     * @param stockMarket the given StockMarket
     */
    public Seller(String instanceIdentifier, StockMarketSingleton stockMarket) {
        super(instanceIdentifier, stockMarket);
    }

    /**
     * This method starts a Thread and add a supply to the StockMarket
     * @param price double
     * @param count int
     */
    public void createSupply(double price, int count) {
        final Seller self = this;
        final Supply supply = new Supply(price, count, this);
        new Thread(new Runnable() {
            public void run() {
                synchronized (self.stockMarket) {
                    self.stockMarket.addSupply(supply);
                }
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * @param cost future balance implementations
     */
    @Override
    public void notify(double cost) {
        // System.out.println("stock.models.Seller " + super.getIdentifier() + " sold an action with " + cost);
    }

}
