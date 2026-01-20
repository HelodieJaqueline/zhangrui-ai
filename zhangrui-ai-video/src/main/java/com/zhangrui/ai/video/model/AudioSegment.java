package com.zhangrui.ai.video.model;

import jakarta.validation.constraints.NotBlank;

import java.nio.file.Path;

/**
 * @author zhangrui
 * @date 2025/12/16 9:51
 */
public class AudioSegment {

    /** 原始讲稿文本 */
    @NotBlank(message = "讲稿文本不能为空")
    private String text;

    /** OSS 音频地址 */
    @NotBlank(message = "音频url不能为空")
    private String url;

    /** 下载后的本地路径（渲染阶段才有） */
    private Path localAudioPath;


    public AudioSegment() {
    }

    public AudioSegment(String text, String url) {
        this(text, url, null);
    }

    private AudioSegment(String text, String url, Path localAudioPath) {
        this.text = text;
        this.url = url;
        this.localAudioPath = localAudioPath;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public Path getLocalAudioPath() {
        return localAudioPath;
    }

    /** 生成一个“带本地路径”的新对象 */
    public AudioSegment withLocalPath(Path localAudioPath) {
        return new AudioSegment(this.text, this.url, localAudioPath);
    }
}

