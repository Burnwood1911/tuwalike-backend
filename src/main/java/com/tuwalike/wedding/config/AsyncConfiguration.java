package com.tuwalike.wedding.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // Set core pool size
        executor.setMaxPoolSize(10); // Set max pool size
        executor.setQueueCapacity(100); // Set queue capacity
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }

}
