package site.halenspace.pocket.threadpool.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

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

    public static void mainOld(String[] args) throws InterruptedException {
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
    public static void main2(String[] args) throws InterruptedException {
        MetricsRegistry registry = new MetricsRegistry();
        registry.newGauge(
                new MetricName("group", "type", "name", "scope", "mBeanName"),
                new com.yammer.metrics.core.Gauge<Integer>() {
                    @Override
                    public Integer value() {
                        return 10;
                    }
                }
        );

        com.yammer.metrics.reporting.ConsoleReporter.enable(registry, 1, TimeUnit.SECONDS);

        Thread.sleep(5000);
    }
    public static void main(String[] args) throws InterruptedException {
        Metrics.defaultRegistry().newGauge(
                new MetricName("group", "type", "name", "scope", "mBeanName"),
                new com.yammer.metrics.core.Gauge<Integer>() {
                    @Override
                    public Integer value() {
                        return 10;
                    }
                }
        );

        Thread.sleep(60000 * 10);
    }
}
