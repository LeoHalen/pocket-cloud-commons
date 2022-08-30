package site.halenspace.pocket.threadpool.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 * @author Halen Leo · 2022/8/30
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class MetricsManager {

    public static void main(String[] args) {
        MetricRegistry registry = new MetricRegistry();
        registry.register(
                MetricRegistry.name("", "queue", "size"),
                (Gauge<Integer>) () -> 10
        );
    }
}
