package site.halenspace.pocketcloud.threadpool.consts;

/**
 * 队列类型枚举
 *
 * @Author Halen.Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
public enum QueueTypeEnum {

    LINKED_BLOCKING_QUEUE("LinkedBlockingQueue"),
    SYNCHRONOUS_QUEUE("SynchronousQueue"),
    ARRAY_BLOCKING_QUEUE("ArrayBlockingQueue"),
    DELAY_QUEUE("DelayQueue"),
    LINKED_TRANSFER_DEQUE("LinkedTransferQueue"),
    LINKED_BLOCKING_DEQUE("LinkedBlockingDeque"),
    PRIORITY_BLOCKING_QUEUE("PriorityBlockingQueue");

    private String typeName;

    QueueTypeEnum(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
