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
    public static final ScheduledThreadPoolExecutor SCHEDULED_THREAD_POOL_EXECUTOR = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(CORE_POOL_SIZE, ASYNC_THREAD_FACTORY);
    static {
        SCHEDULED_THREAD_POOL_EXECUTOR.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        SCHEDULED_THREAD_POOL_EXECUTOR.setKeepAliveTime(60L, TimeUnit.SECONDS);
        SCHEDULED_THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
        SCHEDULED_THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
    /**
     */
    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = (ThreadPoolExecutor) Executors.newCachedThreadPool(ASYNC_THREAD_FACTORY);
    static {
        THREAD_POOL_EXECUTOR.setCorePoolSize(CORE_POOL_SIZE);
        THREAD_POOL_EXECUTOR.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        THREAD_POOL_EXECUTOR.setKeepAliveTime(60L, TimeUnit.SECONDS);
        THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }

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
        activeTaskQueue.running = true;
    }

    private static final class InstanceHolder {
        /**
         *
         */
        static final AsyncTaskExecutor INSTANCE = new AsyncTaskExecutor();
    }

    /**
     * @return AsyncTaskExecutor
     */
    public static AsyncTaskExecutor getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     */
    public void executeSerially(Runnable runnable) {
        executeSerially(ACTIVE_TASK_QUEUE, runnable, 0);
    }

    /**
     */
    public void executeSerially(Runnable runnable, int priority) {
        executeSerially(ACTIVE_TASK_QUEUE, runnable, priority);
    }

    /**
     */
    public void executeSerially(String taskQueue, Runnable runnable) {
        executeSerially(taskQueue, runnable, 0);
    }

    /**
     */
    public void executeSerially(String taskQueue, Runnable runnable, int priority) {
        if (TextUtils.isEmpty(taskQueue) || TextUtils.equals(ACTIVE_TASK_QUEUE, taskQueue)) {
            activeTaskQueue.addTask(runnable, priority);
        } else {
            ActiveTaskQueue queue = activeTaskQueues.get(taskQueue);
            if (null == queue) {
                queue = new ActiveTaskQueue(taskQueue, true);
                activeTaskQueues.put(taskQueue, queue);
                queue.running = true;
            }
            queue.addTask(runnable, priority);
        }
    }

    /**
     */
    public void execute(Runnable runnable) {
        Task task = TaskPool.INSTANCE.obtain(runnable);
        THREAD_POOL_EXECUTOR.execute(task);
    }

    /**
     */
    public ScheduledFuture<?> executeDelay(Runnable runnable, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = SCHEDULED_THREAD_POOL_EXECUTOR.schedule(runnable, delay, unit);
        return ScheduledFutureHandler.getProxyObject(future, runnable);
    }

    /**
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> future = SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(runnable, initialDelay, period, unit);
        return ScheduledFutureHandler.getProxyObject(future, runnable);
    }

    /**
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = SCHEDULED_THREAD_POOL_EXECUTOR.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
        return ScheduledFutureHandler.getProxyObject(future, runnable);
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
            String name = "AsyTsk#" + counter.incrementAndGet();
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
        private final Runnable task;

        private ScheduledFutureHandler(ScheduledFuture<?> target, Runnable task) {
            this.target = target;
            this.task = task;
        }

        private String convert(Object[] args) {
            if (null == args) {
                return null;
            } else if (0 == args.length) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder();
                for (Object arg : args) {
                    buf.append(arg).append(",");
                }
                buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object invoke = method.invoke(target, args);
            if (TextUtils.equals(method.getName(), METHOD_NAME_CANCEL)) {
                Log.d(TAG, "ScheduledFuture.cancel(" + convert(args) + ") : task=" + task.getClass().getName() + " : ret=" + invoke);
            }
            return invoke;
        }

        //生成代理类
        static public ScheduledFuture<?> getProxyObject(ScheduledFuture<?> target, Runnable task) {
            ScheduledFutureHandler handler = new ScheduledFutureHandler(target, task);

            // 第一个参数，是类的加载器
            // 第二个参数是委托类的接口类型，证代理类返回的是同一个实现接口下的类型，保持代理类与抽象角色行为的一致
            // 第三个参数就是代理类本身，即告诉代理类，代理类遇到某个委托类的方法时该调用哪个类下的invoke方法
            Class<?> clazz = target.getClass();
            return (ScheduledFuture<?>) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), handler);
        }
    }
}
