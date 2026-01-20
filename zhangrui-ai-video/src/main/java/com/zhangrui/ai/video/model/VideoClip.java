package com.zhangrui.ai.video.model;

import com.zhangrui.ai.video.enums.ClipType;
import jakarta.validation.constraints.NotBlank;

/**
 * @author zhangrui
 * @date 2025/12/16 9:52
 */
public class VideoClip extends Clip {

    /**
     * OSS 上的视频地址（请求参数传这个）
     */
    @NotBlank(message = "视频url不能为空")
    private String url;

    /**
     * 本地临时路径（运行期生成）
     */
    private String localVideoPath;

    public VideoClip() {
    }

    public VideoClip(String url, String localVideoPath) {
        this.url = url;
        this.localVideoPath = localVideoPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalVideoPath() {
        return localVideoPath;
    }

    public void setLocalVideoPath(String localVideoPath) {
        this.localVideoPath = localVideoPath;
    }

    public void withLocalPath(String localPath) {
        this.localVideoPath = localPath;
    }

    @Override
    public ClipType getType() {
        return ClipType.VIDEO;
    }
}

