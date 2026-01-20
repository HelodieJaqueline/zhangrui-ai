package com.zhangrui.ai.video.util;

import java.nio.file.Path;

/**
 * @author zhangrui
 * @date 2025/12/16 9:57
 */
public class AudioDurationUtil {


    public static double getDuration(Path audioPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-i", audioPath.toAbsolutePath().toString(),
                    "-show_entries", "format=duration",
                    "-v", "quiet",
                    "-of", "csv=p=0"
            );
            Process p = pb.start();
            String out = new String(p.getInputStream().readAllBytes()).trim();
            p.waitFor();
            return Double.parseDouble(out);
        } catch (Exception e) {
            throw new RuntimeException("get audio duration failed", e);
        }
    }
}
