package seker.asynctask

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import seker.asynctask.logger.Log

private val mainHandler: Handler = Handler(Looper.getMainLooper())

fun AsyncTaskExecutor.runOnMainThread(runnable: Runnable) {
    runOnMainThread(runnable, 0L)
}

fun AsyncTaskExecutor.runOnMainThread(runnable: Runnable, delayMillis: Long) {
    if (delayMillis <= 0) {
        mainHandler.post(runnable)
    } else {
        mainHandler.postDelayed(runnable, delayMillis)
    }
}

fun AsyncTaskExecutor.runOnMainThreadWhenIdle(runnable: Runnable) {
    val idleHandler = MessageQueue.IdleHandler {
        try {
            runnable.run()
        } catch (tr: Throwable) {
            Log.e(AsyncTaskExecutor.TAG, tr)
        }
        false
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Looper.getMainLooper().queue.addIdleHandler(idleHandler)
    } else {
        mainHandler.postAtFrontOfQueue {
            Looper.myQueue().addIdleHandler(idleHandler)
        }
    }
}