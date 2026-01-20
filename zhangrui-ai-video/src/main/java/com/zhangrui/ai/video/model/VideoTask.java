package com.zhangrui.ai.video.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangrui
 * @date 2025/12/16 17:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoTask {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课堂id
     */
    private String classId;

    /**
     * 智能体教学名称
     */
    private String agentClassName;

}
