package com.zhangrui.ai.video.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangrui
 * @date 2025/12/17 14:05
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {

    private Integer coreSize = Runtime.getRuntime().availableProcessors();

    private Integer maxSize = Runtime.getRuntime().availableProcessors();

    private Integer queueSize = 50;

}
