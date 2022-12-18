package seker.asynctask;

import seker.asynctask.logger.Log;

import java.util.concurrent.Callable;

/**
 * TaskQueue for tasks
 *
 * @author xinjian
 */
public class ActiveTaskQueue extends TaskQueue {

    protected Runnable idleCallback;

    protected boolean running = false;
    protected Task runningTask = null;

    public ActiveTaskQueue(String name, boolean priority) {
        super(name, priority);
    }

    public void setIdleCallback(Runnable idleCallback) {
        this.idleCallback = idleCallback;
    }

    @Override
    void addTask(Task task) {
        task.taskQueue = this;
        super.addTask(task);
        if (null == runningTask) {
            executeNext();
        }
    }

    public void start() {
        Log.v(TAG, "start()");
        running = true;
        if (null == runningTask) {
            executeNext();
        }
    }

    public void executeNext() {
        if (running) {
            runningTask = priorityQueue.poll();
            if (null == runningTask) {
                Log.v(TAG, "priorityQueue.poll() == null");
                if (null == idleCallback) {
                    Log.v(TAG, "null == idleCallback");
                } else {
                    try {
                        idleCallback.run();
                    } catch (Throwable e) {
                        Log.d(TAG, e);
                    }
                }
            } else {
                doExecute(runningTask);
            }
        }
    }

    public void stop() {
        Log.v(TAG, "stop()");
        running = false;
        runningTask = null;
    }
}
