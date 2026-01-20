package com.zhangrui.ai.video.service;

import com.zhangrui.ai.video.model.RenderContext;
import com.zhangrui.ai.video.model.VideoClip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

/**
 * @author zhangrui
 * @date 2025/12/16 9:54
 */
@Service
public class VideoClipRenderService {

    @Autowired
    private FfmpegExecutor ffmpegExecutor;

    @Autowired
    private MediaDownloadService downloadService;

    public void render(VideoClip clip, RenderContext context) {

        Path workDir = context.getWorkDir();

        // 1. 下载视频到 workDir
        Path localVideo = downloadService.download(clip.getUrl(), workDir);

        // 2. 规格统一
        Path stdVideo = ffmpegExecutor.normalizeVideo(localVideo, workDir);

        context.getRenderedClips().add(stdVideo);
    }
}

