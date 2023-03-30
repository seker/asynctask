package seker.asynctask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import seker.asynctask.logger.Log;

/**
 * @author xinjian
 */
public class TaskPoolTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    void test1() {
        ScheduledFuture<?> scheduledFuture = null;
        for (int i = 0; i < 1000; i += 5) {
            scheduledFuture = AsyncTaskExecutor.INSTANCE.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 1000; i += 1) {
                        AsyncTaskExecutor.INSTANCE.executeDelay(new Runnable() {
                            @Override
                            public void run() {
//                            Log.d("");
                            }
                        }, "sss", i, TimeUnit.MILLISECONDS);
                    }
                }
            }, "aaa", i, 5, TimeUnit.MILLISECONDS);
        }
        try {
            scheduledFuture.get(1, TimeUnit.MINUTES);
        } catch (Throwable e) {
//            Log.e(e);
//            throw new RuntimeException(e);
        }
    }
}
