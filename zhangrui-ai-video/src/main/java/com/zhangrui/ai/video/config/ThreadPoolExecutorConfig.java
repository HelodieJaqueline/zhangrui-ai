package com.zhangrui.ai.video.config;

import com.zhangrui.ai.video.properties.ThreadPoolProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置管理
 * @author zhangrui
 * @date 2025/4/24 18:18
 */
@Configuration
@Slf4j
public class ThreadPoolExecutorConfig {

    @Resource
    private ThreadPoolProperties threadPoolProperties;

    @Bean(name = "asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        long keepAliveTime = 60;
        return new ThreadPoolExecutor(
                threadPoolProperties.getCoreSize(),
                threadPoolProperties .getMaxSize(),
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(threadPoolProperties.getQueueSize()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

}
