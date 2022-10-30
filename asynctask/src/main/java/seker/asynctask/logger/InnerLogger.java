package seker.asynctask.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author xinjian
 */
class InnerLogger extends Logger {
    private static final char[] PRIORITIES = new char[]{'v', 'd', 'i', 'w', 'e', 'f'};
    private final DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS", Locale.US);

    @Override
    protected int println(int priority, String tag, String msg) {
        StringBuilder buf = new StringBuilder();
        Thread currentThread = Thread.currentThread();
        buf
                .append(sDateFormat.format(new Date()))
                .append(" [")
                .append(currentThread.getId())
                .append("|")
                .append(currentThread.getName())
                .append("] [")
                .append(PRIORITIES[priority - VERBOSE])
                .append("] [")
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
