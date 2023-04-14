package seker.asynctask;

import seker.asynctask.logger.Log;
import seker.asynctask.pool.Pool;

/**
 * @author xinjian
 */
final class Task implements Runnable, Pool.Poolable {

    /**
     * Log TAG
     */
    private static final String TAG = AsyncTaskExecutor.TAG;

    /**
     * 超过这个值（单位：毫秒），就会打印一下耗时
     */
    private static final int LONG_TIME_COST_WARN = 100;

    /**
     * 真正的执行对象
     */
    Runnable runnable;

    /**
     * 用于排序
     */
    int priority = 0;

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

    void init(Runnable runnable, int priority) {
        this.runnable = runnable;
        this.priority = priority;

        taskQueue = null;
        offeredTime = 0;
    }

    @Override
    public void reset() {
        init(null, 0);
    }

    @Override
    public void run() {
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
                Log.w(TAG, "[" + taskQueue + "] Task(" + runnable + ") cost " + cost + " ms.");
            }

            if (taskQueue != null) {
                taskQueue.executeNext();
            }

            TaskPool.INSTANCE.free(this);
        }
    }
}
