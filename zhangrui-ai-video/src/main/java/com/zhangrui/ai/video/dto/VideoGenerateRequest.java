package com.zhangrui.ai.video.dto;

import com.zhangrui.ai.video.model.Clip;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhangrui
 * @date 2025/12/16 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoGenerateRequest {

    /**
     * 请求id
     */
    @NotBlank(message = "请求id不能为空")
    private String requestId;

    /**
     * 课程id
     */
    @NotBlank(message = "课程id不能为空")
    private String courseId;

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    /**
     * 课堂id
     */
    @NotBlank(message = "课堂id不能为空")
    private String classId;

    /**
     * 智能体教学名称
     */
    @NotBlank(message = "智能体教学名称不能为空")
    private String agentClassName;

    @Valid
    @NotEmpty(message = "课程内容不能为空")
    private List<Clip> clips;

}
