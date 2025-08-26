package com.adlifehub.adlife.model;

import lombok.Data;

@Data
public class ModerationNote {
    public Long id;
    public String targetType;
    public Long targetId;
    public Long actorId;
    public String note;
    public java.time.OffsetDateTime createdAt;
}
