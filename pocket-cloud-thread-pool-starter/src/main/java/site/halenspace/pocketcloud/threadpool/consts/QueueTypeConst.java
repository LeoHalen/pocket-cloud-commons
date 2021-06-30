package site.halenspace.pocketcloud.threadpool.consts;

/**
 * 线程池队列类型
 *
 * @Author Halen.Leo · 2021/6/30
 * @Blogger 后起小生
 * @Github https://github.com/LeoHalen
 */
public interface QueueTypeConst {

    String LinkedBlockingQueue = "LinkedBlockingQueue";

    String SynchronousQueue = "SynchronousQueue";

    String ArrayBlockingQueue = "ArrayBlockingQueue";

    String DelayQueue = "DelayQueue";

    String LinkedTransferQueue = "LinkedTransferQueue";

    String LinkedBlockingDeque = "LinkedBlockingDeque";

    String PriorityBlockingQueue = "PriorityBlockingQueue";
}
