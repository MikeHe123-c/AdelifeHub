package com.adlifehub.adlife.model;

import lombok.Data;

@Data
public class Report {
    public Long id;
    public String targetType;
    public Long targetId;
    public Long reporterId;
    public String reasonCode;
    public String reasonText;
    public String status;
    public java.time.OffsetDateTime createdAt;
}
