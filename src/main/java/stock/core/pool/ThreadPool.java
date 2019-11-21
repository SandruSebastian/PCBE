package stock.core.pool;

/**
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */
public interface ThreadPool {

    /**
     * @param task to be executed on this Thread Pool
     */
    void execute(Runnable task);

    /**
     * Restarts the thread pool
     */
    void restart();

    /**
     * Shuts down the thread pool
     */
    void shutdown();

}
