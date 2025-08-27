package com.adlifehub.adlife.service;

import com.adlifehub.adlife.dto.FavoriteItemDto;
import com.adlifehub.adlife.mapper.FavoriteMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;

    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }

    public void addListing(Long userId, Long listingId) {
        favoriteMapper.insertListingFav(userId, listingId, OffsetDateTime.now());
    }

    public void removeListing(Long userId, Long listingId) {
        favoriteMapper.deleteListingFav(userId, listingId);
    }

    public void addPost(Long userId, Long postId) {
        favoriteMapper.insertPostFav(userId, postId, OffsetDateTime.now());
    }

    public void removePost(Long userId, Long postId) {
        favoriteMapper.deletePostFav(userId, postId);
    }

    public List<FavoriteItemDto> list(Long userId, String targetType, String listingType, int offset, int limit) {
        return favoriteMapper.listFavorites(userId, targetType, listingType, offset, limit);
    }

    public int count(Long userId, String targetType, String listingType) {
        return favoriteMapper.countFavorites(userId, targetType, listingType);
    }
}
