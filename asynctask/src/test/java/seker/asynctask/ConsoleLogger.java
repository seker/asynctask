package seker.asynctask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import seker.asynctask.logger.Logger;

/**
 * @author xinjian
 */
public class ConsoleLogger extends Logger {
    private static final char[] PRIORITIES = new char[]{'V', 'D', 'I', 'W', 'E', 'F'};
    private final DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS", Locale.US);

    @Override
    protected int println(int priority, String tag, String msg) {
        StringBuilder buf = new StringBuilder();
        Thread currentThread = Thread.currentThread();
        buf
                .append(sDateFormat.format(new Date()))
                .append(" [")
                .append(PRIORITIES[priority - VERBOSE])
                .append("][")
                .append(currentThread.getId())
                .append("|")
                .append(currentThread.getName())
                .append("][")
                .append(tag)
                .append("] : ");
        if (null != msg && msg.length() > 0) {
            buf.append(msg);
        }

        System.out.println(buf);

        int length = buf.length();
        buf.delete(0, length);
        return length;
    }
}
