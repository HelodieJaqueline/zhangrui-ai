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
@ConfigurationProperties(prefix = "video")
public class VideoProperties {

    private String resolution = "1280:720";

    private String logLevel = "info";

}
