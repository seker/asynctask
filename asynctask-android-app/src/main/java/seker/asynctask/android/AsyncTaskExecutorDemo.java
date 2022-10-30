package seker.asynctask.android;

import seker.asynctask.AsyncTaskExecutor;
import seker.asynctask.AsyncTaskExecutorKt;
import seker.asynctask.logger.Log;

public class AsyncTaskExecutorDemo {

    public void demo() {
        AsyncTaskExecutorKt.runOnMainThread(AsyncTaskExecutor.getInstance(), () ->{
            Log.d("AsyncTaskExecutorKt.runOnMainThread)");
        });
    }
}
