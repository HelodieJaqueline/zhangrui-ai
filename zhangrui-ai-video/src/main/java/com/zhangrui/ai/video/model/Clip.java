package com.zhangrui.ai.video.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zhangrui.ai.video.enums.ClipType;

/**
 * @author zhangrui
 * @date 2025/12/16 9:49
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ImageClip.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = VideoClip.class, name = "VIDEO")
})
public abstract class Clip {

    private ClipType type;

    public ClipType getType() {
        return type;
    }

    public void setType(ClipType type) {
        this.type = type;
    }
}
