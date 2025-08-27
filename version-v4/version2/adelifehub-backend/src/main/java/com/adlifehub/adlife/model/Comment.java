package com.adlifehub.adlife.model;

import lombok.Data;

@Data
public class Comment {
    public Long id;
    public String targetType;
    public Long targetId;
    public Long authorId;
    public String content;
    public java.time.OffsetDateTime createdAt;
    public java.time.OffsetDateTime editedAt;
    public java.time.OffsetDateTime deletedAt;
}
