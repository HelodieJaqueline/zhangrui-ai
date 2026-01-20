package com.zhangrui.ai.video.service;

import com.zhangrui.ai.video.model.AudioSegment;
import com.zhangrui.ai.video.model.ImageClip;
import com.zhangrui.ai.video.model.RenderContext;
import com.zhangrui.ai.video.util.SubtitleUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangrui
 * @date 2025/12/16 9:54
 */
@Service
public class ImageClipRenderService {

    @Autowired
    private FfmpegExecutor ffmpegExecutor;

    @Resource
    private MediaDownloadService downloadService;

    public void render(ImageClip clip, RenderContext context) {

        Path workDir = context.getWorkDir();

        // 1. 下载图片
        Path image = downloadService.download(
                clip.getUrl(), workDir
        );

        // 2. 下载音频
        List<AudioSegment> localSegments = new ArrayList<>();
        for (AudioSegment seg : clip.getSegments()) {
            Path audio = downloadService.download(
                    seg.getUrl(), workDir
            );
            localSegments.add(seg.withLocalPath(audio));
        }

        // 3. 拼接音频
        Path audioAll = ffmpegExecutor.concatAudios(localSegments, workDir);

        // 4. 生成字幕
        Path subtitle = SubtitleUtil.generateSrt(localSegments, workDir);

        // 5. 图片 + 音频 + 字幕 → 视频（传 workDir）
        Path output = ffmpegExecutor.imageToVideo(
                image,
                audioAll,
                subtitle,
                workDir
        );

        context.getRenderedClips().add(output);
    }
}

