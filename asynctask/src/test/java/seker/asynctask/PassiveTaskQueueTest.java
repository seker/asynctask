package seker.asynctask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

    @Test
    void test1() {
        PassiveTaskQueue passiveTaskQueue = new PassiveTaskQueue("testPassiveTaskQueue", true);

//        passiveTaskQueue.executeNext();

        passiveTaskQueue.addTask(() -> Log.d("run in runnable test1"), "test1", 1);
        passiveTaskQueue.addTask(() -> Log.d("run in runnable test2"), "test2", 2);
        passiveTaskQueue.addTask(() -> Log.d("run in runnable test3"), "test3", 3);
        passiveTaskQueue.addTask(() -> Log.d("run in runnable test4"), "test4", 4);

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
