package seker.asynctask;

import seker.asynctask.pool.Pool;

import java.util.List;

/**
 * {@link Task} Pool
 *
 * @author xinjian
 */
final class TaskPool extends Pool<Task> {

    static final int INITIAL_CAPACITY = 8;
    private static final int MAX_CAPACITY = 16;

    /**
     * 对象池
     */
    static final TaskPool INSTANCE = new TaskPool(INITIAL_CAPACITY, MAX_CAPACITY);

    /**
     * @param initialCapacity 初始化对象熟量
     * @param max             线程池中最大对象数量
     */
    private TaskPool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override
    @Deprecated
    protected Task newObject() {
        throw new RuntimeException("call obtain(Runnable, String) method instead.");
    }

    @Deprecated
    @Override
    public Task obtain() {
        throw new RuntimeException("call obtain(Runnable, String) method instead.");
    }

    public Task obtain(Runnable runnable) {
        return obtain(runnable, 0);
    }

    public Task obtain(Runnable runnable, int priority) {
        Task task;
        synchronized (freeObjects) {
            if (freeObjects.isEmpty()) {
                task = new Task();
            } else {
                task = freeObjects.pop();
            }
        }
        task.init(runnable, priority);
        return task;
    }

    @Override
    public void free(Task object) {
        synchronized (freeObjects) {
            super.free(object);
        }
    }

    @Override
    public void freeAll(List<Task> objects) {
        synchronized (freeObjects) {
            super.freeAll(objects);
        }
    }

    @Override
    public void clear() {
        synchronized (freeObjects) {
            super.clear();
        }
    }
}

