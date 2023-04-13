package seker.asynctask;

import seker.asynctask.logger.Log;
import seker.asynctask.text.TextUtils;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author seker
 */
public abstract class TaskQueue implements Comparator<Task> {

    /**
     * TAG
     */
    static String TAG = AsyncTaskExecutor.TAG;

    /**
     * 除非构建TaskQueue时，没有指定name，才会使用到此属性。
     * 该值递增，并应用到name上
     */
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * 注意：是线程安全的
     */
    final PriorityBlockingQueue<Task> priorityQueue = new PriorityBlockingQueue<>(TaskPool.INITIAL_CAPACITY, this);
    private final Executor executor = AsyncTaskExecutor.THREAD_POOL_EXECUTOR;
    private final String name;

    /**
     * 是否支持根据Task的 priority 属性值进行排序
     */
    private final boolean priority;

    public TaskQueue(String name, boolean priority) {
        if (TextUtils.isEmpty(name)) {
            this.name = getClass().getSimpleName() + "#" + counter.getAndIncrement();
        } else {
            this.name = name;
            TAG = name;
        }
        this.priority = priority;
    }

    @Override
    public final int compare(Task task1, Task task2) {
        int compare;
        if (priority) {
            compare = task2.priority - task1.priority;
            if (0 == compare) {
                compare = (int) (task1.offeredTime - task2.offeredTime);
            }
        } else {
            compare = (int) (task1.offeredTime - task2.offeredTime);
        }
        return compare;
    }

    /**
     * Add a task into the PriorityTaskQueue.
     *
     * @param runnable         The Task.
     * @param threadNameSuffix ThreadName.
     */
    public final void addTask(Runnable runnable, String threadNameSuffix) {
        addTask(TaskPool.INSTANCE.obtain(runnable, threadNameSuffix, 0));
    }

    public final void addTask(Runnable runnable, String threadNameSuffix, int priority) {
        addTask(TaskPool.INSTANCE.obtain(runnable, threadNameSuffix, priority));
    }

    void addTask(final Task task) {
        task.offeredTime = System.nanoTime();
        priorityQueue.offer(task);
    }

    /**
     * 执行下一个Task
     *
     * @return  true/false
     */
    public boolean executeNext() {
        boolean ret;
        Task task = priorityQueue.poll();
        if (null == task) {
            ret = false;
            Log.v(TAG, "priorityQueue.poll() == null");
        } else {
            ret = true;
            doExecute(task);
        }
        return ret;
    }

    protected void doExecute(Task task) {
//        Log.v(TAG, "doExecute() : task=" + task.threadNameSuffix);
        executor.execute(task);
    }

    @Override
    public String toString() {
        return name;
    }
}
