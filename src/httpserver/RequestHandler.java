package httpserver;

import java.util.concurrent.BlockingQueue;

/**
 *
 * @author ruben.hume
 */

// The thread that executes Runnable objects (the requests from clients)
public class RequestHandler extends Thread {
    private BlockingQueue taskQ; // The queue in which tasks are put
    public RequestHandler(BlockingQueue tq) {
        taskQ = tq;
    }
    @Override
    public void run() {
        // When run, the thread continuously tries to take the first Runnable in the queue
        while(true) {
            try {
                Runnable task = (Runnable) taskQ.take();
                task.run();
            } catch(Exception ex) {
                // Keep running
            }
        }
    }
}
