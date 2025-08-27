package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.dto.FavoriteItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    void insertListingFav(@Param("userId") Long userId, @Param("listingId") Long listingId, @Param("createdAt") OffsetDateTime createdAt);

    void deleteListingFav(@Param("userId") Long userId, @Param("listingId") Long listingId);

    void insertPostFav(@Param("userId") Long userId, @Param("postId") Long postId, @Param("createdAt") OffsetDateTime createdAt);

    void deletePostFav(@Param("userId") Long userId, @Param("postId") Long postId);

    List<FavoriteItemDto> listFavorites(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("listingType") String listingType, @Param("offset") int offset, @Param("limit") int limit);

    int countFavorites(@Param("userId") Long userId, @Param("targetType") String targetType, @Param("listingType") String listingType);

    void deleteFavoritesByListing(@Param("listingId") Long listingId);

    void deleteFavoritesByPost(@Param("postId") Long postId);
}
