package seker.asynctask.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seker.asynctask.ActiveTaskQueue;
import seker.asynctask.ConsoleLogger;
import seker.asynctask.SleepRunnable;
import seker.asynctask.logger.Log;

import java.util.concurrent.TimeUnit;

/**
 * @author seker
 * @since 2022.11.05
 */
public class ActiveTaskQueueTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    public void test() {
        ActiveTaskQueue activeTaskQueue = new ActiveTaskQueue("testActiveTaskQueue", true);

        activeTaskQueue.addTask(new SleepRunnable(1), "test1", 1);
        activeTaskQueue.addTask(new SleepRunnable(1), "test2", 2);
        activeTaskQueue.addTask(new SleepRunnable(1), "test3", 3);
        activeTaskQueue.addTask(new SleepRunnable(1), "test4", 4);

        activeTaskQueue.start();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (Throwable e) {
            Log.w(e);
        }
        activeTaskQueue.stop();
    }

    @Test
    public void test1() {
        ActiveTaskQueue activeTaskQueue = new ActiveTaskQueue("testActiveTaskQueue", true);

        activeTaskQueue.addTask(new SleepRunnable(1), "test1");
        activeTaskQueue.addTask(new SleepRunnable(1), "test2");
        activeTaskQueue.addTask(new SleepRunnable(1), "test3");
        activeTaskQueue.addTask(new SleepRunnable(1), "test4");

        activeTaskQueue.start();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (Throwable e) {
            Log.w(e);
        }
        activeTaskQueue.stop();
    }

    @Test
    public void test2() {
        ActiveTaskQueue activeTaskQueue = new ActiveTaskQueue("testActiveTaskQueue", false);

        activeTaskQueue.addTask(new SleepRunnable(1), "test1", 1);
        activeTaskQueue.addTask(new SleepRunnable(1), "test2", 2);
        activeTaskQueue.addTask(new SleepRunnable(1), "test3", 3);
        activeTaskQueue.addTask(new SleepRunnable(1), "test4", 4);

        activeTaskQueue.start();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (Throwable e) {
            Log.w(e);
        }
        activeTaskQueue.stop();
    }
}
