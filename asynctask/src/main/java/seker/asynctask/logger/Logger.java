package seker.asynctask.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * @author xinjian
 */
public abstract class Logger {

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    /**
     * log priority
     */
    private int logPriority = VERBOSE;

    public void setLogPriority(int priority) {
        if (priority >= VERBOSE && priority <= ASSERT) {
            logPriority = priority;
        } else {
            throw new RuntimeException("priority should between [" + VERBOSE + " , " + ASSERT + "]");
        }
    }

    final int log(int priority, String tag, Throwable tr) {
        return log(priority, tag, getStackTraceString(tr));
    }

    final int log(int priority, String tag, String msg, Throwable tr) {
        return log(priority, tag, msg + '\n' + getStackTraceString(tr));
    }

    final int log(int priority, String tag, String msg) {
        if (priority >= logPriority) {
            return println(priority, tag, msg);
        } else {
            return -1;
        }
    }

    /**
     * log out
     *
     * @param priority Log Priority
     * @param tag      Tag
     * @param msg      Message
     * @return The number of bytes written.
     */
    protected abstract int println(int priority, String tag, String msg);

    /**
     * Convert Throwable to String
     *
     * @param tr Throwable
     * @return String
     */
    protected String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}
