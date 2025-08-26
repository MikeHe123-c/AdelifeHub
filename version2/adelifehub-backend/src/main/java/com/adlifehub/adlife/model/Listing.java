package com.adlifehub.adlife.model;

import lombok.Data;

@Data
public class Listing {
    public Long id;
    public String type;
    public String title;
    public String content;
    public Double price;
    public String priceUnit;
    public String location;
    public Double latitude;
    public Double longitude;
    public java.util.List<String> images;
    public String status;
    public Long authorId;
    public java.time.OffsetDateTime createdAt;
    public java.time.OffsetDateTime updatedAt;
    public java.time.OffsetDateTime deletedAt;
    public Long deletedBy;
    public String deleteReason;
    public RentalDetails rental;
    public JobDetails job;
}
