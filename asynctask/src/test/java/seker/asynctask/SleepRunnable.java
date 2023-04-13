package seker.asynctask;

import seker.asynctask.logger.Log;

import java.util.concurrent.TimeUnit;

/**
 * @author seker
 * @since 2022.11.05
 */
public class SleepRunnable implements Runnable {
    private final long sleep;

    public SleepRunnable(long sleep) {
        this.sleep = sleep;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sleep));
        } catch (InterruptedException e) {
            Log.w(e);
        }
    }
}
