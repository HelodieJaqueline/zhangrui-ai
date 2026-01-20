package com.zhangrui.ai.video.model;

import com.zhangrui.ai.video.enums.ClipType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author zhangrui
 * @date 2025/12/16 9:50
 */
public class ImageClip extends Clip {
    /** PPT 页图片 OSS 地址 */
    @NotBlank(message = "PPT图片url不能为空")
    private String url;

    /** 该页的讲稿分段 */
    @NotEmpty(message = "该页的讲稿分段不能为空")
    private List<AudioSegment> segments;

    public ImageClip() {
    }

    public ImageClip(String url, List<AudioSegment> segments) {
        this.url = url;
        this.segments = segments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<AudioSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<AudioSegment> segments) {
        this.segments = segments;
    }

    @Override
    public ClipType getType() {
        return ClipType.IMAGE;
    }
}
