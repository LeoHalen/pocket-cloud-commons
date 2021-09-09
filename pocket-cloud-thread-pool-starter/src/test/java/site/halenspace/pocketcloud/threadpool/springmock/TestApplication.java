package site.halenspace.pocketcloud.threadpool.springmock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Halen Leo · 2021/7/6
 */
@SpringBootApplication
//@Import(DynamicThreadPoolAutoConfiguration.class)
@ComponentScan(value = "site.halenspace.pocketcloud.threadpool")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
