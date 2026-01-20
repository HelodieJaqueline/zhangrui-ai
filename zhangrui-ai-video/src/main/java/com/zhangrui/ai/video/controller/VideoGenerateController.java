package com.zhangrui.ai.video.controller;

import com.zhangrui.ai.video.dto.VideoGenerateRequest;
import com.zhangrui.ai.video.service.VideoGenerateService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangrui
 * @date 2025/12/16 9:41
 */
@RestController
@RequestMapping("/agent/class")
public class VideoGenerateController {

    @Resource
    private VideoGenerateService videoGenerateService;

    @PostMapping("/generate")
    public Boolean generate(@Valid @RequestBody VideoGenerateRequest request) {
        return videoGenerateService.generate(request);
    }

}
