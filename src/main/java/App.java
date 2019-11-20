import stock.core.market.StockMarket;
import stock.core.market.StockMarketSingleton;
import stock.exceptions.StockMarketAlreadyRunningException;
import stock.models.Buyer;
import stock.models.Demand;
import stock.models.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <br>Quick Review on other version and what we have learnt<br>
 * <br>- version 0.0.1 : The base version that we built was a simple MVP around the project task without any sort of threads
 * running.<br>
 * <br>- version 0.0.2 : The second version had a different approach where the StockMarket would create a separated thread
 * in the StockMarket for every demand added, notifying the buyer when his demand was consumed and the supplier when his
 * supply was consumed.<br>
 * <br>There were some problems related to :
 * <br>- synchronizing the entire methods and not using synchronized on the explicit part of the code where concurrency
 * problems may occur
 * <br>- cloning the Arrays and not actually trying to solve those WRITE problems in a concurrency fashion
 * <br>- using volatiles where it wasn't needed<br>
 * <br>- version 0.0.3 (BETA) : With the third version we tried to correct those things around.<br>
 * <br>We managed to do that by :<br>
 * <br>- better OOP approach on the project
 * <br>- using synchronized only on the explicit parts of the code with primitive Objects that act like Locks
 * <br>- we had to face java.util.ConcurrentModificationException that occurred when two threads tries to modify the value
 * in an enhanced for of. Basically, iterables face this problem when using enhanced for of and the suggestion found
 * on some java documentation was to use some concurrency libraries (e.g concurrent arrays, etc).So we switch to
 * the basic for loop.
 * <br>- paralyze the StockMarketSingleton when a thread use some method
 * <br>Room for improvements :
 * <br>- Buyer class should only add the demand, and not clear the StockMarket Supply and Demand arrayList. It's not his
 * business to do that job. The solution is to make StockMarketSingleton to also start a new Thread to find consumed
 * demands and supplies and remove them.<br>
 * <br>Short review on the implementation :<br>
 * <br>App is the main class that will start the app. It will create some buyers/sellers that will create their demands
 * and supplies. For every supply/buyer that adds a demand or creates a supply, a new thread will be started in their
 * class. Sellers only synchronize the stockMarket only when they are adding a supply. Buyers, on the other hand,
 * synchronize the stockMarket when adding the demand, iterating over the list of supplies and removing supplies/demands
 * Preventive synchronized locks are created in the StockMarketSingleton class (HISTORY_LOCK, SUPPLY_LOCK, DEMAND_LOCK)
 * to ensure thread safe execution.
 * If the buyer doesn't find a supply over 2000 iterations, his thread will be stopped.
 * <br>- version 0.0.4 : Added a fixed size thread pool implementation that is used to schedule matching tasks for newly placed demands.
 * Also added {@link StockMarket} and {@link stock.core.pool.ThreadPool} interfaces as a base guideline for other possible implementations
 * that might arise. Previous implementation of the StockMarket was renamed {@link stock.core.market.BasicStockMarket}
 * and {@link stock.core.market.BasicStockMarketBuilder} was introduces to facilitate the setup of the StockMarket before being used<br>
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */

public final class App {
    public static void main(String[] args) {
        StockMarket stockMarket = StockMarketSingleton.getInstance();
        try {
            stockMarket.run();
            bootstrapApp(2000, 2000, stockMarket);
        } catch (StockMarketAlreadyRunningException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method will bootstrap the entire app with a parameter configuration
     * For every seller that creates and supply, a new Thread will be started in their class
     * For Every buyer that adds a demand, a new Thread will be started in their class
     *
     * @param sellersNumber int
     * @param buyersNumber  int
     * @param stockMarket   StockMarketSingleton
     */
    private static void bootstrapApp(int sellersNumber, int buyersNumber, final StockMarket stockMarket) {
        final Random rnd = new Random();

        List<Seller> sellers = new ArrayList<Seller>();
        List<Buyer> buyers = new ArrayList<Buyer>();

        for (int i = 0; i < sellersNumber; i++) {
            sellers.add(new Seller("Seller" + i, stockMarket));
        }

        for (int i = 0; i < buyersNumber; i++) {
            buyers.add(new Buyer("Buyer" + i, stockMarket));
        }

        for (final Seller s : sellers) {
            s.createSupply(rnd.nextInt(2) + 1, rnd.nextInt(4) + 1);
        }

        for (final Buyer b : buyers) {
            b.addDemand(new Demand(rnd.nextInt(2) + 1, rnd.nextInt(4) + 1, b));
        }

    }
}