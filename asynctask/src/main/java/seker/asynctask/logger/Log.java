package seker.asynctask.logger;

import static seker.asynctask.AsyncTaskExecutor.TAG;
import static seker.asynctask.logger.Logger.*;

/**
 * @author xinjian
 */
public class Log {

    private static Logger targetLogger = new InnerLogger();

    private Log() {
    }

    public static void setPriority(int priority) {
        targetLogger.setLogPriority(priority);
    }

    public static void setLogger(Logger logger) {
        targetLogger = logger;
    }

    public static int v(String msg) {
        return targetLogger.log(VERBOSE, TAG, msg);
    }

    public static int v(String tag, String msg) {
        return targetLogger.log(VERBOSE, tag, msg);
    }

    public static int v(Throwable tr) {
        return targetLogger.log(VERBOSE, TAG, tr);
    }

    public static int v(String tag, Throwable tr) {
        return targetLogger.log(VERBOSE, tag, tr);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return targetLogger.log(VERBOSE, tag, msg, tr);
    }

    public static int d(String msg) {
        return targetLogger.log(DEBUG, TAG, msg);
    }

    public static int d(String tag, String msg) {
        return targetLogger.log(DEBUG, tag, msg);
    }

    public static int d(Throwable tr) {
        return targetLogger.log(DEBUG, TAG, tr);
    }

    public static int d(String tag, Throwable tr) {
        return targetLogger.log(DEBUG, tag, tr);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return targetLogger.log(DEBUG, tag, msg, tr);
    }

    public static int i(String msg) {
        return targetLogger.log(INFO, TAG, msg);
    }

    public static int i(String tag, String msg) {
        return targetLogger.log(INFO, tag, msg);
    }

    public static int i(Throwable tr) {
        return targetLogger.log(INFO, TAG, tr);
    }

    public static int i(String tag, Throwable tr) {
        return targetLogger.log(INFO, tag, tr);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return targetLogger.log(INFO, tag, msg, tr);
    }

    public static int w(String msg) {
        return targetLogger.log(WARN, TAG, msg);
    }

    public static int w(String tag, String msg) {
        return targetLogger.log(WARN, tag, msg);
    }

    public static int w(Throwable tr) {
        return targetLogger.log(WARN, TAG, tr);
    }

    public static int w(String tag, Throwable tr) {
        return targetLogger.log(WARN, tag, tr);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return targetLogger.log(WARN, tag, msg, tr);
    }

    public static int e(String msg) {
        return targetLogger.log(ERROR, TAG, msg);
    }

    public static int e(String tag, String msg) {
        return targetLogger.log(ERROR, tag, msg);
    }

    public static int e(Throwable tr) {
        return targetLogger.log(ERROR, TAG, tr);
    }

    public static int e(String tag, Throwable tr) {
        return targetLogger.log(ERROR, tag, tr);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return targetLogger.log(ERROR, tag, msg, tr);
    }

    public static int f(String msg) {
        return targetLogger.log(ASSERT, TAG, msg);
    }

    public static int f(String tag, String msg) {
        return targetLogger.log(ASSERT, tag, msg);
    }

    public static int f(Throwable tr) {
        return targetLogger.log(ASSERT, TAG, tr);
    }

    public static int f(String tag, Throwable tr) {
        return targetLogger.log(ASSERT, tag, tr);
    }

    public static int f(String tag, String msg, Throwable tr) {
        return targetLogger.log(ASSERT, tag, msg, tr);
    }
}
