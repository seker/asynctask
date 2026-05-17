package seker.asynctask.android.logger

import android.os.Build
import android.util.Log

import seker.asynctask.logger.Logger

class AndroidLogger : Logger() {

    override fun println(priority: Int, tag: String?, msg: String?): Int {
        val currentThread = Thread.currentThread()
        @Suppress("DEPRECATION")
        val threadId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA)
            currentThread.threadId() else currentThread.id
        return Log.println(priority, tag, "[${threadId}|${currentThread.name}] ${msg!!}")
    }
}