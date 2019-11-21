package stock.core.market;

import com.sun.istack.internal.NotNull;
import stock.core.pool.ThreadPool;
import stock.exceptions.StockMarketAlreadyRunningException;
import stock.exceptions.StockMarketAlreadyStoppedException;
import stock.models.Demand;
import stock.models.Supply;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic {@link StockMarket} implementation
 * This class will have a record of all the demands and supplies that are
 * being entered by the Buyers/Sellers
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */
public class BasicStockMarket implements StockMarket {

    private final Object SUPPLY_LOCK = new Object();
    private final Object HISTORY_LOCK = new Object();
    private final Object DEMAND_LOCK = new Object();

    /**
     * List of strings to see all the actions that took place in the runtime
     */
    private List<String> history = new ArrayList<String>();
    private List<Supply> supplies = new ArrayList<Supply>();
    private List<Demand> demands = new ArrayList<Demand>();

    /**
     * Flag that starts the matching of new demands with existing supplies
     */
    private boolean isRunning = false;

    /**
     * Enables runtime logger
     */
    private boolean enabledLogger = false;

    /**
     * Thread pool used to schedule tasks on the stock market
     */
    private ThreadPool threadPool;

    /**
     * Method to enable runtime logs
     *
     * @param enabledLogger boolean for runtime logs
     */
    public void setEnabledLogger(boolean enabledLogger) {
        this.enabledLogger = enabledLogger;
    }

    /**
     * @param threadPool underlying thread pool used by the stock market
     */
    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /**
     * Adding a new Supply in the StockMarket
     *
     * @param supply added by the seller
     */
    public void addSupply(@NotNull Supply supply) {
        synchronized (SUPPLY_LOCK) {
            supplies.add(supply);
        }
        this.updateHistory(supply.getOwner().getName() + " with id " + supply.getOwner().getIdentifier() + " added a supply :" + supply.toString());

    }

    /**
     * Adding a new demand in the StockMarket
     *
     * @param demand added by the buyer
     */
    public void addDemand(@NotNull final Demand demand) {
        synchronized (DEMAND_LOCK) {
            this.demands.add(demand);
        }
        this.updateHistory(demand.getOwner().getName() + " with id " + demand.getOwner().getIdentifier() + " added a demand :" + demand.toString());

        this.threadPool.execute(new Runnable() {
            public void run() {
                matchDemandWithSupply(demand);
            }
        });
    }

    /**
     * Simulates the running server functionality
     *
     * @return StockMarket instance
     * @throws StockMarketAlreadyRunningException threw If it's already running
     */
    public StockMarket run() throws StockMarketAlreadyRunningException {
        if (this.isRunning) {
            throw new StockMarketAlreadyRunningException("The StockMarket is already running");
        }
        this.isRunning = true;
        this.threadPool.restart();
        return this;
    }

    /**
     * Simulates stopping the server functionality
     *
     * @return StockMarket instance
     * @throws StockMarketAlreadyStoppedException threw If it's already running
     */
    public StockMarket stop() throws StockMarketAlreadyStoppedException {
        if (!this.isRunning) {
            throw new StockMarketAlreadyStoppedException("The StockMarket is already stopped");
        }
        this.isRunning = false;
        this.threadPool.shutdown();
        return this;
    }

    /**
     * Method to print every action that took place in the market
     *
     * @return The whole history as a String
     */
    public String printHistory() {
        StringBuilder fullHistory = new StringBuilder();

        for (int i = 0; i < history.size(); i++) {
            fullHistory.append(history.get(i) + "\n");
        }

        return fullHistory.toString();
    }

    /**
     * Main method used by the buyer to try to buy a new Supply that matches his demand
     *
     * @param demand published by the buyer
     * @param supply published y the seller in the StockMarket
     */
    private void tryToBuy(@NotNull Demand demand, @NotNull Supply supply) {
        if (demand.getPrice() != supply.getPrice() || supply.getCount() == 0 || demand.getCount() == 0) {
            return;
        }

        int min = Math.min(supply.getCount(), demand.getCount());

        synchronized (SUPPLY_LOCK) {

            supply.consume(min);

        }
        synchronized (DEMAND_LOCK) {

            demand.consume(min);

        }

        this.updateHistory("[" + Thread.currentThread() + "]:" + demand.getOwner().getName() + " with the demand " + demand.toString() + " matched " + supply.toString());


    }

    /**
     * Removing an existing demand
     *
     * @param demand published by the buyer
     */
    private void removeDemand(@NotNull Demand demand) {
        synchronized (DEMAND_LOCK) {
            this.demands.remove(demand);

        }
        this.updateHistory("[" + Thread.currentThread() + "]:" + demand.toString() + " demand has ben consumed");

    }

    /**
     * Method to update the history
     *
     * @param message to be updated with
     */
    private void updateHistory(String message) {
        synchronized (HISTORY_LOCK) {
            history.add(new Timestamp(System.currentTimeMillis()).getTime() + " : " + message);
            if (enabledLogger)
                System.out.println(new Timestamp(System.currentTimeMillis()).getTime() + " : " + message);
        }
    }

    /**
     * Method to remove an actual supply from the StockMarket
     *
     * @param supply published by the seller, deleted by the buyer
     */
    private void removeSupply(@NotNull Supply supply) {
        synchronized (SUPPLY_LOCK) {
            supplies.remove(supply);
        }
        this.updateHistory("[" + Thread.currentThread() + "]:" + supply.toString() + " supply has ben removed");

    }

    private void matchDemandWithSupply(Demand demand) {
        int timesTriedToBuy = 0;

        for (;;) {
//            synchronized (SUPPLY_LOCK) {
                for (int i = 0; i < this.supplies.size() && this.supplies.get(i) != null; i++) {
                    Supply supply = this.supplies.get(i);
                    tryToBuy(demand, supply);

                    if (supply.getCount() == 0) {
                        removeSupply(supply);
                    }

                    if (demand.getCount() == 0) {
                        removeDemand(demand);
                        return;
                    }
                }
                if (timesTriedToBuy++ == 2000) {
                    return;
                }
//            }
        }
    }

}
