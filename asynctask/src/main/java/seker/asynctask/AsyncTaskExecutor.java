package seker.asynctask;

import seker.asynctask.logger.Log;
import seker.asynctask.text.TextUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xinjian
 */
public final class AsyncTaskExecutor {
    /**
     * Log TAG
     */
    public static final String TAG = "AsyTsk";

    /**
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     */
    private static final int CORE_POOL_SIZE = 1;
    /**
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT + 1;
    private static final String ACTIVE_TASK_QUEUE = "gActiveTaskQueue";

    private static final ThreadFactory ASYNC_THREAD_FACTORY = new AsyncThreadFactory();

    /**
     */
    static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(CORE_POOL_SIZE, ASYNC_THREAD_FACTORY);
    static {
        SCHEDULED_THREAD_POOL_EXECUTOR.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        SCHEDULED_THREAD_POOL_EXECUTOR.setKeepAliveTime(60L, TimeUnit.SECONDS);
        SCHEDULED_THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
        SCHEDULED_THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
    /**
     */
    static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = (ThreadPoolExecutor) Executors.newCachedThreadPool(ASYNC_THREAD_FACTORY);
    static {
        THREAD_POOL_EXECUTOR.setCorePoolSize(CORE_POOL_SIZE);
        THREAD_POOL_EXECUTOR.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        THREAD_POOL_EXECUTOR.setKeepAliveTime(60L, TimeUnit.SECONDS);
        THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     */
    final static AsyncTaskExecutor INSTANCE = new AsyncTaskExecutor();

    /**
     */
    private final ConcurrentHashMap<String, ActiveTaskQueue> activeTaskQueues = new ConcurrentHashMap<>();
    /**
     */
    private final ActiveTaskQueue activeTaskQueue;

    /**
     */
    private AsyncTaskExecutor() {
        activeTaskQueue = new ActiveTaskQueue(ACTIVE_TASK_QUEUE, true);
        activeTaskQueue.start();
    }

    /**
     * @return AsyncTaskExecutor
     */
    public static AsyncTaskExecutor getInstance() {
        return INSTANCE;
    }

    /**
     */
    public void executeSerially(Runnable runnable, String threadName) {
        executeSerially(ACTIVE_TASK_QUEUE, runnable, threadName, 0);
    }

    /**
     */
    public void executeSerially(Runnable runnable, String threadName, int priority) {
        executeSerially(ACTIVE_TASK_QUEUE, runnable, threadName, priority);
    }

    /**
     */
    public void executeSerially(String taskQueue, Runnable runnable, String threadName) {
        executeSerially(taskQueue, runnable, threadName, 0);
    }

    /**
     */
    public void executeSerially(String taskQueue, Runnable runnable, String threadName, int priority) {
        if (TextUtils.isEmpty(threadName)) {
            throw new IllegalArgumentException("The parameter 'threadName' can't be empty.");
        }
        if (TextUtils.isEmpty(taskQueue) || TextUtils.equals(ACTIVE_TASK_QUEUE, taskQueue)) {
            activeTaskQueue.addTask(runnable, threadName, priority);
        } else {
            ActiveTaskQueue queue = activeTaskQueues.get(taskQueue);
            if (null == queue) {
                queue = new ActiveTaskQueue(taskQueue, true);
                queue.start();
                activeTaskQueues.put(taskQueue, queue);
            }
            queue.addTask(runnable, threadName, priority);
        }
    }

    /**
     */
    public void execute(Runnable runnable, String threadName) {
        if (TextUtils.isEmpty(threadName)) {
            throw new IllegalArgumentException("The parameter 'threadName' can't be empty.");
        }
        Task task = TaskPool.INSTANCE.obtain(runnable, threadName);
        THREAD_POOL_EXECUTOR.execute(task);
    }

    /**
     */
    public ScheduledFuture<?> executeDelay(Runnable runnable, String threadName, long delay, TimeUnit unit) {
        if (TextUtils.isEmpty(threadName)) {
            throw new IllegalArgumentException("The parameter 'threadName' can't be empty.");
        }
        Task task = TaskPool.INSTANCE.obtain(runnable, threadName);
        task.autoFree = false;
        ScheduledFuture<?> scheduledFuture = SCHEDULED_THREAD_POOL_EXECUTOR.schedule(task, delay, unit);
        return ScheduledFutureHandler.getProxyObject(scheduledFuture, task);
    }

    /**
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, String threadName, long initialDelay, long period, TimeUnit unit) {
        if (TextUtils.isEmpty(threadName)) {
            throw new IllegalArgumentException("The parameter 'threadName' can't be empty.");
        }
        Task task = TaskPool.INSTANCE.obtain(runnable, threadName);
        task.autoFree = false;
        ScheduledFuture<?> scheduledFuture = SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(task, initialDelay, period, unit);
        return ScheduledFutureHandler.getProxyObject(scheduledFuture, task);
    }

    /**
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, String threadName, long initialDelay, long delay, TimeUnit unit) {
        if (TextUtils.isEmpty(threadName)) {
            throw new IllegalArgumentException("The parameter 'threadName' can't be empty.");
        }
        Task task = TaskPool.INSTANCE.obtain(runnable, threadName);
        task.autoFree = false;
        ScheduledFuture<?> scheduledFuture = SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(task, initialDelay, delay, unit);
        return ScheduledFutureHandler.getProxyObject(scheduledFuture, task);
    }

    /**
     * 线程工厂类，用于创建标准的线程
     */
    private static final class AsyncThreadFactory implements ThreadFactory {
        /**
         * 线程池工厂计数器：用于作为线程名称
         */
        private final AtomicInteger counter = new AtomicInteger(0);

        /**
         * 创建线程
         */
        @Override
        public Thread newThread(Runnable runnable) {
            if (null == runnable) {
                throw new IllegalArgumentException("null == runnable");
            }
            String name = "AsyTsk#" + counter.incrementAndGet() + "_";
            Thread thread = new Thread(runnable, name);
            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        }
    };

    /**
     * <a href="https://blog.csdn.net/I_r_o_n_M_a_n/article/details/114902921">Java之动态代理</a>
     *
     * @author xinjian
     */
    private static final class ScheduledFutureHandler implements InvocationHandler {

        public static final String METHOD_NAME_CANCEL = "cancel";
        //目标类，被代理对象
        private final ScheduledFuture<?> target;
        private final Task task;

        private ScheduledFutureHandler(ScheduledFuture<?> target, Task task) {
            this.target = target;
            this.task = task;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (TextUtils.equals(method.getName(), METHOD_NAME_CANCEL)) {
                Log.d(TAG, "ScheduledFuture is canceled: task=" + task.threadNameSuffix);
                TaskPool.INSTANCE.free(task);
            }
            return method.invoke(target, args);
        }

        //生成代理类
        static public ScheduledFuture<?> getProxyObject(ScheduledFuture<?> target, Task task) {
            ScheduledFutureHandler handler = new ScheduledFutureHandler(target, task);

            // 第一个参数，是类的加载器
            // 第二个参数是委托类的接口类型，证代理类返回的是同一个实现接口下的类型，保持代理类与抽象角色行为的一致
            // 第三个参数就是代理类本身，即告诉代理类，代理类遇到某个委托类的方法时该调用哪个类下的invoke方法
            Class<?> clazz = target.getClass();
            return (ScheduledFuture<?>) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
        }
    }
}
