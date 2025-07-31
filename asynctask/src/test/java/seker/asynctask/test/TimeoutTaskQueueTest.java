package seker.asynctask.test;

import org.junit.BeforeClass;
import org.junit.Test;

import seker.asynctask.ConsoleLogger;
import seker.asynctask.SleepRunnable;
import seker.asynctask.TimeoutTaskQueue;
import seker.asynctask.logger.Log;

import java.util.concurrent.TimeUnit;

/**
 * @author seker
 * @since 2022.11.05
 */
public class TimeoutTaskQueueTest {

    @BeforeClass
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    public void test1() {
        TimeoutTaskQueue timeoutTaskQueue = new TimeoutTaskQueue("testTimeoutTaskQueue", true);
        timeoutTaskQueue.setTimeout(TimeUnit.SECONDS.toMillis(2));

        timeoutTaskQueue.addTask(new SleepRunnable("1", 1));
        timeoutTaskQueue.addTask(new SleepRunnable("2", 2));
        timeoutTaskQueue.addTask(new SleepRunnable("3", 3));
        timeoutTaskQueue.addTask(new SleepRunnable("4", 3));
        timeoutTaskQueue.addTask(new SleepRunnable("5", 3));
        timeoutTaskQueue.addTask(new SleepRunnable("6", 3));

        timeoutTaskQueue.start();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(15));
        } catch (Throwable e) {
            Log.w(e);
        }
    }

}
