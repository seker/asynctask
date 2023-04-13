package seker.asynctask.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seker.asynctask.ConsoleLogger;
import seker.asynctask.logger.Log;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author seker
 * @since 2022.11.05
 */
public class PriorityBlockingQueueTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    public void test1() {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>(5, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        priorityBlockingQueue.offer(1);
        dumpPriorityBlockingQueue(priorityBlockingQueue);

        priorityBlockingQueue.offer(1);
        priorityBlockingQueue.offer(2);
        dumpPriorityBlockingQueue(priorityBlockingQueue);

        priorityBlockingQueue.offer(1);
        priorityBlockingQueue.offer(2);
        priorityBlockingQueue.offer(3);
        dumpPriorityBlockingQueue(priorityBlockingQueue);

        priorityBlockingQueue.offer(1);
        priorityBlockingQueue.offer(2);
        priorityBlockingQueue.offer(3);
        priorityBlockingQueue.offer(4);
        dumpPriorityBlockingQueue(priorityBlockingQueue);

        priorityBlockingQueue.offer(1);
        priorityBlockingQueue.offer(2);
        priorityBlockingQueue.offer(3);
        priorityBlockingQueue.offer(4);
        priorityBlockingQueue.offer(5);
        dumpPriorityBlockingQueue(priorityBlockingQueue);
    }

    private void dumpPriorityBlockingQueue(PriorityBlockingQueue<Integer> priorityBlockingQueue) {
        StringBuilder buf = new StringBuilder();
        while (!priorityBlockingQueue.isEmpty()) {
            if (0 == buf.length()) {
                buf.append(priorityBlockingQueue.poll());
            } else {
                buf.append(",").append(priorityBlockingQueue.poll());
            }
        }
        Log.d(buf.toString());
    }
}
