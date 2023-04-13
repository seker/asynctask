package seker.asynctask.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import seker.asynctask.AsyncTaskExecutor;
import seker.asynctask.ConsoleLogger;
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
        ScheduledFuture<?> scheduledFuture = AsyncTaskExecutor.getInstance().executeDelay(new Runnable() {
            @Override
            public void run() {
                Log.d("executeDelay: 1+2 start.");
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(2));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d("executeDelay: 1+2 end.");
            }
        }, 1, TimeUnit.SECONDS);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
            scheduledFuture.cancel(true);
        } catch (Throwable e) {
            Log.e(e);
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(2));
        } catch (Throwable e) {
            Log.w(e);
        }
    }
}
