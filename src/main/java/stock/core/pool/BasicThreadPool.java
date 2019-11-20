package stock.core.pool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Basic implementation of a {@link ThreadPool}
 * Has a fixed number of worker threads that schedule tasks added to a blocking queue
 * In a next version, the current implementation of BlockingQueue, namely LinkedBlockingQueue,
 * should be changed with a own implementation of a blocking queue
 *
 * @author Sebastian Sandru, Daniel Incicau, Stefan Oproiu, Paul Iusztin
 * @version 0.0.4
 * @since 11.19.2019
 */
public class BasicThreadPool implements ThreadPool {

    private final int threadsCount;
    private final PoolWorker[] threads;

    //TODO Change this with an own implementation of a blocking queue
    private final LinkedBlockingQueue<Runnable> queue;

    public BasicThreadPool(int threadsCount) {
        this.threadsCount = threadsCount;
        this.queue = new LinkedBlockingQueue<Runnable>();
        this.threads = new PoolWorker[threadsCount];

        for (int i = 0; i < threadsCount; i++) {
            threads[i] = new PoolWorker(i);
            threads[i].start();
        }
    }

    /**
     * @param task to be executed on this Thread Pool
     */
    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    /**
     * Restarts the thread pool
     */
    public void restart() {
        for (int i = 0; i < threadsCount; i++) {
            if(!threads[i].isRunning) {
                threads[i].setRunning(true);
                threads[i].start();
            }
        }
    }

    /**
     * Shuts down the thread pool
     */
    public void shutdown() {
        for (int i = 0; i < threadsCount; i++) {
            threads[i].setRunning(false);
        }

        synchronized (queue) {
            queue.notifyAll();
        }
    }


    /**
     * Wrapper class for {@link Thread} that defines a worker for this pool
     */
    private class PoolWorker extends Thread {

        private int number;

        private boolean isRunning;

        PoolWorker(int number) {
            this.number = number;
            this.isRunning = true;
        }

        public void setRunning(boolean running) {
            this.isRunning = running;
        }

        public void run() {
            Runnable task;

            while(this.isRunning) {
                synchronized (queue) {
                    while(queue.isEmpty() && isRunning) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            System.out.println("An error occurred while queue is waiting: " + e.getMessage());
                        }
                    }

                    if(!isRunning) {
                        return;
                    }

                    task = queue.poll();
                }

                // If we don't catch RuntimeException,
                // the pool could leak threads
                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println("Thread pool is interrupted due to an issue: " + e.getMessage());
                }
            }
        }

        @Override
        public String toString() {
            return BasicThreadPool.this.toString() + " -> " + "PoolWorker #" + this.number;
        }
    }

    @Override
    public String toString() {
        return "BasicThreadPool " + this.hashCode() + " [" + threadsCount + " Workers]";
    }
}
