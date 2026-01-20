package com.zhangrui.ai.video.service;

import com.zhangrui.ai.video.model.RenderContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author zhangrui
 * @date 2025/12/16 17:02
 */
@Component
@Slf4j
public class RenderContextFactory {

    public RenderContext create() {
        try {
            Path workDir = Paths.get(
                    System.getProperty("java.io.tmpdir"),
                    "video-render",
                    UUID.randomUUID().toString()
            );
            Files.createDirectories(workDir);
            RenderContext context = new RenderContext();
            context.setWorkDir(workDir);
            return context;
        } catch (Exception e) {
            log.error("create render context failed", e);
            throw new RuntimeException("create render context failed", e);
        }
    }
}

