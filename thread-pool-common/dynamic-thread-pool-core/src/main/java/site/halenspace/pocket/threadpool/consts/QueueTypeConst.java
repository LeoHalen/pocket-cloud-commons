package site.halenspace.pocket.threadpool.consts;

/**
 * 线程池队列类型
 *
 * @author Halen Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
public interface QueueTypeConst {

    String LinkedBlockingQueue = "LinkedBlockingQueue";

    String ArrayBlockingQueue = "ArrayBlockingQueue";

    String LinkedBlockingDeque = "LinkedBlockingDeque";

    String SynchronousQueue = "SynchronousQueue";

    String DelayQueue = "DelayQueue";

    String LinkedTransferQueue = "LinkedTransferQueue";

    String PriorityBlockingQueue = "PriorityBlockingQueue";

    static boolean isBoundedQueue(String queueType) {
        return LinkedBlockingQueue.equals(queueType) || ArrayBlockingQueue.equals(queueType) || LinkedBlockingDeque.equals(queueType);
    }
}
