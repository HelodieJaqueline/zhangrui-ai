package com.zhangrui.ai.video.util;


import com.zhangrui.ai.video.model.AudioSegment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author zhangrui
 * @date 2025/12/16 9:56
 */
public class SubtitleUtil {


    public static Path generateSrt(List<AudioSegment> segments, Path workDir) {
        try {
            Path srt = workDir.resolve("subtitle.srt");

            StringBuilder sb = new StringBuilder();
            double current = 0.0;
            int index = 1;

            for (AudioSegment seg : segments) {
                double duration = AudioDurationUtil.getDuration(seg.getLocalAudioPath());

                sb.append(index++).append("\n");
                sb.append(format(current))
                        .append(" --> ")
                        .append(format(current + duration))
                        .append("\n");
                sb.append(seg.getText()).append("\n\n");

                current += duration;
            }

            Files.writeString(srt, sb.toString(), StandardCharsets.UTF_8);
            return srt;
        } catch (Exception e) {
            throw new RuntimeException("generate srt failed", e);
        }
    }



    private static String format(double sec) {
        int h = (int) (sec / 3600);
        int m = (int) (sec % 3600 / 60);
        double s = sec % 60;
        return String.format("%02d:%02d:%06.3f", h, m, s)
                .replace('.', ',');
    }
}
