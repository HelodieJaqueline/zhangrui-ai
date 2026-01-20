package com.zhangrui.ai.video.service;

import com.zhangrui.ai.video.enums.ClipType;
import com.zhangrui.ai.video.model.Clip;
import com.zhangrui.ai.video.model.ImageClip;
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
public class ClipRenderService {


    @Autowired
    private ImageClipRenderService imageClipRenderService;
    @Autowired
    private VideoClipRenderService videoClipRenderService;
    @Autowired
    private FfmpegExecutor ffmpegExecutor;


    public void render(Clip clip, RenderContext context) {
        if (clip.getType() == ClipType.IMAGE) {
            imageClipRenderService.render((ImageClip) clip, context);
        } else {
            videoClipRenderService.render((VideoClip) clip, context);
        }
    }


    public Path concatAll(RenderContext context) {
        return ffmpegExecutor.concatVideos(context.getRenderedClips(), context.getWorkDir());
    }
}
