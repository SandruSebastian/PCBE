package stock.core;

import com.sun.istack.internal.NotNull;
import stock.exceptions.StockMarketAlreadyRunningException;
import stock.models.Demand;
import stock.models.Supply;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Core Singleton Class. The application functionality is based on this.
 * This class will have a record of all the demands and supplies that are
 * being entered by the Buyers/Sellers
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.3
 * @since 11.19.2019
 */
public class StockMarketSingleton {

    private final static StockMarketSingleton ourInstance = new StockMarketSingleton();
    private static boolean isRunning = false;
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
     * Enables runtime logger
     */
    private boolean enabledLogger = false;


    private StockMarketSingleton() {
    }

    /**
     * @return StockMarketSingleton instance
     */
    public static StockMarketSingleton getInstance() {
        return ourInstance;
    }

    /**
     * Simulates the running server func
     *
     * @return StockMarketSingleton instance
     * @throws StockMarketAlreadyRunningException threw If it's already running
     */
    public StockMarketSingleton run() throws StockMarketAlreadyRunningException {
        if (isRunning) {
            throw new StockMarketAlreadyRunningException("The StockMarket is already running");
        }
        isRunning = true;
        return this;
    }

    /**
     * Method to enable runtime logs
     *
     * @param enabledLogger boolean for runtime logs
     */
    public void setEnabledLogger(boolean enabledLogger) {
        this.enabledLogger = enabledLogger;
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
    public void addDemand(@NotNull Demand demand) {
        synchronized (DEMAND_LOCK) {
            this.demands.add(demand);
        }
        this.updateHistory(demand.getOwner().getName() + " with id " + demand.getOwner().getIdentifier() + " added a demand :" + demand.toString());

    }


    /**
     * @return The supplies added at the current time
     */
    public List<Supply> getSupplies() {
        return supplies;
    }

    /**
     * Main method used by the buyer to try to buy a new Supply that matches his demand
     *
     * @param demand published by the buyer
     * @param supply published y the seller in the StockMarket
     */
    public void tryToBuy(@NotNull Demand demand, @NotNull Supply supply) {
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

        this.updateHistory(demand.getOwner().getName() + " with the demand " + demand.toString() + " matched " + supply.toString());


    }

    /**
     * Removing an existing demand
     *
     * @param demand published by the buyer
     */
    public void removeDemand(@NotNull Demand demand) {
        synchronized (DEMAND_LOCK) {
            this.demands.remove(demand);

        }
        this.updateHistory(demand.toString() + " demand has ben consumed");

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
    public void removeSupply(@NotNull Supply supply) {
        synchronized (SUPPLY_LOCK) {
            supplies.remove(supply);
        }
        this.updateHistory(supply.toString() + " supply has ben removed");

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
}
