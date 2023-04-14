package seker.asynctask.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seker.asynctask.ConsoleLogger;
import seker.asynctask.PassiveTaskQueue;
import seker.asynctask.SleepRunnable;
import seker.asynctask.logger.Log;

import java.util.concurrent.TimeUnit;

/**
 * @author xinjian
 */
public class PassiveTaskQueueTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    /**
     * 运行结果是无序的。
     * 它只能保证Task的被执行的起始时间是线性的，但并不能保证：前一个Task完成后，才执行下一个Task
     */
    @Test
    void test1() {
        PassiveTaskQueue passiveTaskQueue = new PassiveTaskQueue("testPassiveTaskQueue", false);

//        passiveTaskQueue.executeNext();

        passiveTaskQueue.addTask(() -> Log.d("run in runnable test1"), 1);
        passiveTaskQueue.addTask(() -> Log.d("run in runnable test2"), 2);
        passiveTaskQueue.addTask(() -> Log.d("run in runnable test3"), 3);
        passiveTaskQueue.addTask(() -> Log.d("run in runnable test4"), 4);

        passiveTaskQueue.executeNext();
        passiveTaskQueue.executeNext();
        passiveTaskQueue.executeNext();
        passiveTaskQueue.executeNext();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (Throwable e) {
            Log.w(e);
        }
    }

    @Test
    void test2() {
        PassiveTaskQueue passiveTaskQueue = new PassiveTaskQueue("testPassiveTaskQueue", false);

//        passiveTaskQueue.executeNext();

        passiveTaskQueue.addTask(new SleepRunnable("test1", 1), 1);
        passiveTaskQueue.addTask(new SleepRunnable("test2", 1), 1);
        passiveTaskQueue.addTask(new SleepRunnable("test3", 1), 1);
        passiveTaskQueue.addTask(new SleepRunnable("test4", 1), 1);

        passiveTaskQueue.executeNext();
        passiveTaskQueue.executeNext();
        passiveTaskQueue.executeNext();
        passiveTaskQueue.executeNext();

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (Throwable e) {
            Log.w(e);
        }
    }
}
