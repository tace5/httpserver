package httpserver;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author ruben.hume
 */
public class ThreadPool {
    private BlockingQueue<Runnable> taskQ; // Queue for the tasks that need to be executed
    private ArrayList<RequestHandler> threads = new ArrayList<>(); // Active threads in threadpool
    private int nrThreads;
    public ThreadPool(int nrOfThreads, int maxTasks) {
        nrThreads = nrOfThreads;
        taskQ = new ArrayBlockingQueue<>(maxTasks);
        for(int i = 0; i < nrOfThreads; i++) {
            threads.add(new RequestHandler(taskQ));
            threads.get(i).start();
        }
    }
    public synchronized void addTask(Runnable r) {
        try {
            taskQ.put(r);
        } catch(Exception ex) {
            Gui.msgToConsole("ERROR", "Failed to execute " + r.toString());
        }
    }
}
