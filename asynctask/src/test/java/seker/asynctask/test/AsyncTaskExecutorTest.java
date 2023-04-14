package seker.asynctask.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import seker.asynctask.AsyncTaskExecutor;
import seker.asynctask.ConsoleLogger;
import seker.asynctask.SleepRunnable;
import seker.asynctask.logger.Log;

/**
 * @author xinjian
 */
public class AsyncTaskExecutorTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    void test1() {
        AsyncTaskExecutor asyncTaskExecutor = AsyncTaskExecutor.getInstance();
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test1"));
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test2"));
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test3"));
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test4"));

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (Throwable e) {
            Log.w(e);
        }
    }

    /**
     * 执行顺序：1，4，3，2
     * 因为AsyncTaskExecutor.executeSerially()，它里面的ActiveTaskQueue默认是running的
     */
    @Test
    void test2() {
        AsyncTaskExecutor asyncTaskExecutor = AsyncTaskExecutor.getInstance();
        asyncTaskExecutor.executeSerially(new SleepRunnable("1", 1), 1);
        asyncTaskExecutor.executeSerially(new SleepRunnable("2", 1), 2);
        asyncTaskExecutor.executeSerially(new SleepRunnable("3", 1), 3);
        asyncTaskExecutor.executeSerially(new SleepRunnable("4", 1), 4);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (Throwable e) {
            Log.w(e);
        }
    }

    @Test
    void test3() {
        AsyncTaskExecutor asyncTaskExecutor = AsyncTaskExecutor.getInstance();
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test1"));
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test2"));
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test3"));
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test4"));
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (Throwable e) {
            Log.w(e);
        }
    }
}
