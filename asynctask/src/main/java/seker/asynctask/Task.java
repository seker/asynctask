package seker.asynctask;

import seker.asynctask.logger.Log;
import seker.asynctask.pool.Pool;
import seker.asynctask.text.TextUtils;

/**
 * @author xinjian
 */
final class Task implements Runnable, Pool.Poolable {

    /**
     * Log TAG
     */
    private static final String TAG = AsyncTaskExecutor.TAG;

    /**
     */
    private static final int LONG_TIME_COST_WARN = 100;

    /**
     */
    private Runnable runnable;

    /**
     */
    String threadNameSuffix;

    /**
     */
    int priority = 0;

    /**
     */
    boolean autoFree = true;

    /**
     */
    ActiveTaskQueue taskQueue;

    /**
     * 该Task加入到TaskQueue的时间值（纳秒）
     * Task在TaskQueue的排队策略
     *  1、比较priority
     *  2、比较offeredTime
     */
    long offeredTime = 0;

    void init(Runnable runnable, String threadNameSuffix, int priority) {
        this.runnable = runnable;
        this.threadNameSuffix = threadNameSuffix;
        this.priority = priority;

        autoFree = true;
        taskQueue = null;
        offeredTime = 0;
    }

    @Override
    public void reset() {
        init(null, null, 0);
    }

    @Override
    public void run() {
        final Thread currentThread = Thread.currentThread();
        final String threadNamePrefix = currentThread.getName();
        final boolean suffix = !TextUtils.isEmpty(threadNameSuffix);
        if (suffix) {
            currentThread.setName(threadNamePrefix + threadNameSuffix);
        }
        long start = System.currentTimeMillis();
        try {
            if (null == runnable) {
                Log.e(TAG, "null == runnable");
            } else {
                runnable.run();
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            if (cost >= LONG_TIME_COST_WARN) {
                Log.w(TAG, "[" + taskQueue + "] Task(" + threadNameSuffix + ") cost " + cost + " ms.");
            }

            if (suffix) {
                currentThread.setName(threadNamePrefix);
            }

            if (taskQueue != null) {
                taskQueue.executeNext();
            }

            if (autoFree) {
                TaskPool.INSTANCE.free(this);
            }
        }
    }
}
