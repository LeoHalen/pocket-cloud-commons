package site.halenspace.pocket.threadpool.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * @author Halen Leo · 2022/8/30
 * @blogger 后起小生
 * @github <a href="https://github.com/LeoHalen"/>
 */
public class MetricsManager {

    /* Singleton instance */
    public static final MetricsManager INSTANCE = new MetricsManager();
    /* Default metric registry */
    private final MetricRegistry registry = new MetricRegistry();

    private MetricsManager() {}

    public static MetricRegistry registry() {
        return INSTANCE.registry;
    }

    public void test() {
        MetricRegistry registry = new MetricRegistry();
        registry.register(
                MetricRegistry.name("test", "queue", "size"),
                (Gauge<Integer>) () -> 10
        );
    }

    public static void main(String[] args) throws InterruptedException {
        MetricRegistry registry = new MetricRegistry();
        registry.register(
                MetricRegistry.name("test", "queue", "size"),
                (Gauge<Integer>) () -> 10
        );

        Gauge<Integer> gauge = registry.gauge(MetricRegistry.name("test", "queue", "size"));

        ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build()
                .start(1, TimeUnit.SECONDS);

        Thread.sleep(5000);
    }
}
