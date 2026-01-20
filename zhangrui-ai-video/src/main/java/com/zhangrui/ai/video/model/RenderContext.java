package com.zhangrui.ai.video.model;

import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrui
 * @date 2025/12/16 9:52
 */
@Data
public class RenderContext {
    private Path workDir;
    private List<Path> renderedClips = new ArrayList<>();
    private boolean keepTempFiles = false;

    public void cleanup() {
        if (keepTempFiles || workDir == null) {
            return;
        }
        FileUtils.deleteQuietly(workDir.toFile());
    }
}


