package site.halenspace.pocketcloud.threadpool;

/**
 * 线程池工厂接口
 *
 * @author Halen.Leo · 2021/7/2
 * @blogger 后起小生
 * @github https://github.com/LeoHalen
 */
public interface ThreadPoolFactory<E, P> {

    /**
     * 创建线程池
     * @param p
     * @return
     */
    E create(P p);
}
