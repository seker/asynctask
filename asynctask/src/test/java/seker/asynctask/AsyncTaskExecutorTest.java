package seker.asynctask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seker.asynctask.logger.Log;

/**
 * @author xinjian
 */
public class AsyncTaskExecutorTest {

    @BeforeAll
    public static void init() {
        Log.setLogger(new ConsoleLogger());
    }

    @Test
    void test1() {
        AsyncTaskExecutor asyncTaskExecutor = AsyncTaskExecutor.getInstance();
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test1"), "test1");
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test2"), "test2");
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test3"), "test3");
        asyncTaskExecutor.execute(()-> Log.d("run in runnable test4"), "test4");
    }

    @Test
    void test2() {
        AsyncTaskExecutor asyncTaskExecutor = AsyncTaskExecutor.getInstance();
        asyncTaskExecutor.executeSerially(()-> Log.d("run in runnable test1"), "test1", 1);
        asyncTaskExecutor.executeSerially(()-> Log.d("run in runnable test2"), "test2", 2);
        asyncTaskExecutor.executeSerially(()-> Log.d("run in runnable test3"), "test3", 3);
        asyncTaskExecutor.executeSerially(()-> Log.d("run in runnable test4"), "test4", 4);
    }

    @Test
    void test3() {
        AsyncTaskExecutor asyncTaskExecutor = AsyncTaskExecutor.getInstance();
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test1"), "test1");
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test2"), "test2");
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test3"), "test3");
        asyncTaskExecutor.executeSerially("framework", ()-> Log.d("run in runnable test4"), "test4");
    }
}
