package seker.asynctask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seker.asynctask.logger.Log;

import java.util.concurrent.TimeUnit;

/**
 * @author seker
 * @since 2022.11.05
 */
public class TimeoutTaskQueueTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    void test1() {
        TimeoutTaskQueue timeoutTaskQueue = new TimeoutTaskQueue("testTimeoutTaskQueue", true);
        timeoutTaskQueue.setTimeout(TimeUnit.SECONDS.toMillis(2));

        timeoutTaskQueue.addTask(new SleepRunnable(1), "test1");
        timeoutTaskQueue.addTask(new SleepRunnable(2), "test2");
        timeoutTaskQueue.addTask(new SleepRunnable(3), "test3");
        timeoutTaskQueue.addTask(new SleepRunnable(4), "test4");

        timeoutTaskQueue.start();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        } catch (Throwable e) {
            Log.w(e);
        }
    }

}
