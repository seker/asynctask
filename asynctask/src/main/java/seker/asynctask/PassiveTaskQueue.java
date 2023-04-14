package seker.asynctask;

/**
 * 被动/懒散的TaskQueue，需要主动调用它的{@link  super#executeNext()}函数，它才执行下一个任务
 *
 * 因此，它只能保证Task的被执行的起始时间是线性的，但并不能保证：前一个Task完成后，才执行下一个Task
 *
 * @author xinjian
 */
public class PassiveTaskQueue extends TaskQueue {

    public PassiveTaskQueue(String name, boolean priority) {
        super(name, priority);
    }
}
