package com.zhangrui.ai.video.service;

import com.google.common.collect.Lists;
import com.zhangrui.ai.video.model.AudioSegment;
import com.zhangrui.ai.video.properties.VideoProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrui
 */
@Component
@Slf4j
public class FfmpegExecutor {


    private static final String FFMPEG = "ffmpeg";

    @Resource
    private VideoProperties videoProperties;


    /* ================= 音频相关 ================= */


    /**
     * 拼接多段音频（保持原编码，速度最快）
     */
    public Path concatAudios(List<AudioSegment> segments, Path workDir) {
        try {
            Path listFile = workDir.resolve("audio_list.txt");

            List<String> lines = new ArrayList<>();
            for (AudioSegment s : segments) {
                double duration = getAudioDuration(s.getLocalAudioPath());
                log.info("concatAudios,audio:{}, duration: {}", s.getLocalAudioPath(), duration);
                lines.add("file '" + s.getLocalAudioPath().toString().replace("\\", "/") + "'");
            }
            Files.write(listFile, lines, StandardCharsets.UTF_8);

            Path output = workDir.resolve("audio_all.wav");

            exec(Lists.newArrayList(
                    FFMPEG, "-y",
                    "-f", "concat",
                    "-safe", "0",
                    "-i", listFile.toString(),
                    "-c", "copy",
                    output.toString()
            ));
            return output;
        } catch (Exception e) {
            log.error("concatAudios failed:", e);
            throw new RuntimeException("concatAudios failed", e);
        }
    }

    /* ================= 图片 → 视频 ================= */


    /**
     * 图片 + 音频 + 字幕 → 视频（标准教学视频）
     */
    public Path imageToVideo(Path image, Path audio, Path subtitlePath, Path workDir) {
        try {
            Path output = workDir.resolve("page_" + System.nanoTime() + ".mp4");
            String subtitle = subtitlePath
                    .toAbsolutePath()
                    .normalize()
                    .toString()
                    .replace("\\", "/")
                    .replace(":", "\\\\:");

            double duration = getAudioDuration(audio);
            log.info("audio:{}, duration: {}", audio, duration);

            String vf = "subtitles=filename=" + subtitle +
                    ",scale=" + videoProperties.getResolution() +
                    ":force_original_aspect_ratio=decrease,pad=" + videoProperties.getResolution() +
                    ":(ow-iw)/2:(oh-ih)/2";

            exec(Lists.newArrayList(
                    FFMPEG, "-y",
                    "-loop", "1",
                    "-i", image.toAbsolutePath().toString(),
                    "-i", audio.toAbsolutePath().toString(),
                    "-filter_complex",
                    "[1:a]atrim=0:" + duration + ",asetpts=PTS-STARTPTS[a]",
                    "-map", "0:v",
                    "-map", "[a]",
                    "-t", String.valueOf(duration),
                    "-vf", vf,
                    "-c:v", "libx264",
                    "-pix_fmt", "yuv420p",
                    "-c:a", "aac",
                    "-ar", "44100",
                    "-ac", "2",
                    "-b:a", "128k",
                    "-shortest",
                    output.toString()
            ));
            return output;
        } catch (Exception e) {
            log.error("imageToVideo failed:", e);
            throw new RuntimeException("imageToVideo failed", e);
        }
    }


    /* ================= 视频处理 ================= */


    /**
     * 视频规格统一（分辨率 / 编码 / 音频）
     */
    public Path normalizeVideo(Path video, Path workDir) {
        try {
            Path output = workDir.resolve(
                    "video_std_" + System.nanoTime() + ".mp4"
            );
            double duration = getVideoDuration(video);
            log.info("video:{}, duration: {}", video, duration);
            exec(Lists.newArrayList(
                    FFMPEG,
                    "-y",
                    "-i", video.toString(),
                    "-vf", "scale=" + videoProperties.getResolution() +
                           ":force_original_aspect_ratio=decrease,pad=" +
                            videoProperties.getResolution()+ ":(ow-iw)/2:(oh-ih)/2," +
                            "setsar=1",
                    "-filter_complex",
                    "[0:a]atrim=0:" + duration + ",asetpts=PTS-STARTPTS[a]",
                    "-map", "0:v",
                    "-map", "[a]",
                    "-c:v", "libx264",
                    "-profile:v", "high",
                    "-level", "4.0",
                    "-pix_fmt", "yuv420p",
                    "-c:a", "aac",
                    "-ac", "2",
                    "-ar", "44100",
                    "-b:a", "128k",
                    "-shortest",
                    "-movflags", "+faststart",
                    output.toString()
            ));
            return output;
        } catch (Exception e) {
            log.error("normalizeVideo failed:", e);
            throw new RuntimeException("normalizeVideo failed", e);
        }
    }



    /* ================= 视频拼接 ================= */


    /**
     * 拼接多个 MP4 Clip（无重新编码，速度最快）
     */
    public Path concatVideos(List<Path> videos, Path workDir) {
        try {
            Path output = workDir.resolve("final.mp4");

            List<String> cmd = new ArrayList<>();
            cmd.add("ffmpeg");
            cmd.add("-y");

            // 输入文件
            for (Path v : videos) {
                cmd.add("-i");
                cmd.add(v.toAbsolutePath().toString());
            }

            // 构建 filter_complex
            StringBuilder filter = new StringBuilder();
            for (int i = 0; i < videos.size(); i++) {
                filter.append("[").append(i).append(":v:0]")
                        .append("[").append(i).append(":a:0]");
            }
            filter.append("concat=n=").append(videos.size())
                    .append(":v=1:a=1[outv][outa]");

            cmd.add("-filter_complex");
            cmd.add(filter.toString());

            cmd.add("-map");
            cmd.add("[outv]");
            cmd.add("-map");
            cmd.add("[outa]");

            // 强制统一编码
            cmd.add("-c:v");
            cmd.add("libx264");
            cmd.add("-pix_fmt");
            cmd.add("yuv420p");
            cmd.add("-profile:v");
            cmd.add("high");

            cmd.add("-c:a");
            cmd.add("aac");
            cmd.add("-ar");
            cmd.add("44100");
            cmd.add("-ac");
            cmd.add("2");

            cmd.add("-movflags");
            cmd.add("+faststart");

            cmd.add(output.toString());

            exec(cmd);
            return output;
        } catch (Exception e) {
            log.error("concatVideos failed:", e);
            throw new RuntimeException("concatVideos failed", e);
        }
    }

    private double getVideoDuration(Path video) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                video.toString()
        );
        Process p = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(p.getInputStream()))) {
            String line = reader.readLine();
            if (line == null) {
                throw new RuntimeException("Failed to read video duration");
            }
            return Double.parseDouble(line.trim());
        }
    }

    private double getAudioDuration(Path audio) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-select_streams", "a:0",
                    "-show_entries", "stream=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    audio.toAbsolutePath().toString()
            );

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                String line = reader.readLine();
                if (line == null || line.isBlank()) {
                    throw new RuntimeException("ffprobe returned empty duration for " + audio);
                }
                return Double.parseDouble(line.trim());
            }
        } catch (Exception e) {
            throw new RuntimeException("getAudioDuration failed: " + audio, e);
        }
    }



    /* ================= 底层执行 ================= */
    private void exec(List<String> cmd) throws IOException, InterruptedException {
        cmd.add("-loglevel");
        cmd.add(videoProperties.getLogLevel());
        log.info("FFmpeg cmd: {}", String.join(" ", cmd));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        // 吞掉输出（避免阻塞）
        try (InputStream in = p.getInputStream()) {
            // 使用 BufferedReader 按行读取并打印输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                // 打印输出到日志中
                log.debug("FFmpeg output: {}", line);
            }
        }
        int code = p.waitFor();
        if (code != 0) {
            throw new RuntimeException("FFmpeg failed, exitCode=" + code);
        }
    }
}