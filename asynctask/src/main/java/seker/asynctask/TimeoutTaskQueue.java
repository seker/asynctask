package seker.asynctask;

import seker.asynctask.logger.Log;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xinjian
 */
public class TimeoutTaskQueue extends ActiveTaskQueue {

    private final TimeoutMonitorTask timeoutMonitorTask = new TimeoutMonitorTask();

    /**
     */
    private long timeout = TimeUnit.SECONDS.toMillis(1);

    private ScheduledFuture<?> timeoutMonitorFuture;

    /**
     */
    public TimeoutTaskQueue(String name, boolean priority) {
        super(name, priority);
    }

    /**
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    protected void doExecute(Task runnable) {
        synchronized (TimeoutTaskQueue.class) {
            if (null != timeoutMonitorFuture) {
                timeoutMonitorFuture.cancel(true);
                 Log.d(TAG, "not time out : [" + timeoutMonitorTask.targetTask.runnable + "], cancel the TimeoutMonitorTask.");
            }
            timeoutMonitorTask.setTargetTask(runnable);
            timeoutMonitorFuture = AsyncTaskExecutor.SCHEDULED_THREAD_POOL_EXECUTOR.schedule(
                    timeoutMonitorTask, timeout, TimeUnit.MILLISECONDS);
        }
        super.doExecute(runnable);
    }

    private class TimeoutMonitorTask implements Runnable {

        Task targetTask;

        void setTargetTask(Task runnable) {
            targetTask = runnable;
        }

        @Override
        public void run() {
            synchronized (TimeoutTaskQueue.class) {
                if (runningTask == targetTask) {
                    runningTask.taskQueue = null;
                    timeoutMonitorFuture = null;
                    Log.w(TAG, "time out : [" + targetTask.runnable + "], force to call TaskQueue.next()");
                    executeNext();
                }
            }
        }
    }
}
