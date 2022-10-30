package seker.asynctask.android.logger

import android.util.Log

import seker.asynctask.logger.Logger

class AndroidInnerLogger : Logger() {

    override fun println(priority: Int, tag: String?, msg: String?): Int {
        val currentThread = Thread.currentThread()
        return Log.println(priority, tag, "[${currentThread.id}|${currentThread.name}] ${msg!!}")
    }
}