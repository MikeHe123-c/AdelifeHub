package com.adlifehub.adlife.dto; import lombok.Data; @Data
public class FavoriteItemDto { private String kind; private String listingType; private java.time.OffsetDateTime favCreatedAt; private Long id; private String title; private Double price; private String priceUnit; private String location; private String image; private Integer likes; private String excerpt; }
