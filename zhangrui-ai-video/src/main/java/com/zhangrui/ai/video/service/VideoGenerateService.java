package com.zhangrui.ai.video.service;

import com.zhangrui.ai.video.dto.VideoGenerateRequest;
import com.zhangrui.ai.video.model.Clip;
import com.zhangrui.ai.video.model.RenderContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author zhangrui
 * @date 2025/12/16 9:53
 */
@Service
@Slf4j
public class VideoGenerateService {


    @Resource
    private ClipRenderService clipRenderService;

    @Resource
    private Executor asyncTaskExecutor;

    @Resource
    private RenderContextFactory renderContextFactory;


    public Boolean generate(VideoGenerateRequest request) {
        //TODO 保存任务记录
        asyncTaskExecutor.execute(() -> {
            doGenerate(request);
        });
        return true;
    }

    private void doGenerate(VideoGenerateRequest request) {
        RenderContext context = renderContextFactory.create();
        if (context == null) {
            log.error("create render context failed");
            return;
        }
        Path path = null;
        try {
            List<Clip> clips = request.getClips();
            for (Clip clip : clips) {
                clipRenderService.render(clip, context);
            }
            path = clipRenderService.concatAll(context);
            log.info("concat all clips success, video path:{}", path.toString());
            context.setKeepTempFiles(true);
        } catch (Exception e) {
            context.setKeepTempFiles(true);
            log.error("generate video failed", e);
        } finally {
            // 清理临时文件
            context.cleanup();
            //清理最终生成的视频文件
            if (path != null) {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    log.error("delete file failed:{}", path, e);
                }
            }
        }
    }
}
