package com.zhangrui.ai.video.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author zhangrui
 * @date 2025/12/16 14:26
 */
@Service
public class MediaDownloadService {

    public Path download(String url, Path workDir) {
        try {
            String fileName = extractFileName(url);
            Path target = workDir.resolve(fileName);

            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("download failed: " + url, e);
        }
    }

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1)
                .split("\\?")[0]; // 去掉 OSS query
    }
}
