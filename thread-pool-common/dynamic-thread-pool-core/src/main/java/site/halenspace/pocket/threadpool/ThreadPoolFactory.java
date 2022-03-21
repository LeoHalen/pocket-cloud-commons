package site.halenspace.pocket.threadpool;

/**
 * 线程池工厂接口
 *
 * @author Halen Leo · 2021/7/2
 * @blogger 后起小生
 * @github https://github.com/LeoHalen
 */
public interface ThreadPoolFactory<E, K, P> {

    E getThreadPoolExecutor(K k, P p);

    E getThreadPoolExecutor(K k, P p, ExecutorListener listener);
}
