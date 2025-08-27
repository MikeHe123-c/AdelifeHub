package com.adlifehub.adlife.service;

import com.adlifehub.adlife.mapper.FavoriteMapper;
import com.adlifehub.adlife.mapper.PostMapper;
import com.adlifehub.adlife.model.Post;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostMapper postMapper;
    private final FavoriteMapper favoriteMapper;

    public PostService(PostMapper postMapper, FavoriteMapper favoriteMapper) {
        this.postMapper = postMapper;
        this.favoriteMapper = favoriteMapper;
    }

    public List<Post> list(int offset, int limit) {
        return postMapper.listActive(offset, limit);
    }

    public int countActive() {
        return postMapper.countActive();
    }

    public List<Post> listByAuthor(Long authorId, String status, int offset, int limit) {
        return postMapper.listByAuthor(authorId, status, offset, limit);
    }

    public int countByAuthor(Long authorId, String status) {
        return postMapper.countByAuthor(authorId, status);
    }

    public Post getVisible(Long id) {
        Post p = postMapper.findById(id);
        if (p == null) return null;
        if (!"active".equals(p.getStatus())) return null;
        return p;
    }

    public Post create(Post p, Long authorId) {
        p.setAuthorId(authorId);
        p.setStatus("active");
        p.setLikes(0);
        p.setCreatedAt(OffsetDateTime.now());
        p.setUpdatedAt(OffsetDateTime.now());
        postMapper.insert(p);
        return postMapper.findById(p.getId());
    }

    public Post update(Long id, Post patch, Long actorId, boolean isAdmin) {
        Post db = postMapper.findById(id);
        if (db == null) return null;
        if (!isAdmin && !db.getAuthorId().equals(actorId)) throw new SecurityException("forbidden");
        if (patch.getTitle() != null) db.setTitle(patch.getTitle());
        if (patch.getContent() != null) db.setContent(patch.getContent());
        if (patch.getImages() != null) db.setImages(patch.getImages());
        db.setUpdatedAt(OffsetDateTime.now());
        postMapper.update(db);
        return postMapper.findById(id);
    }

    public void softDelete(Long id, Long actorId, String reason, boolean isAdmin) {
        Post db = postMapper.findById(id);
        if (db == null) return;
        if (!isAdmin && !db.getAuthorId().equals(actorId)) throw new SecurityException("forbidden");
        postMapper.softDelete(id, OffsetDateTime.now(), actorId, reason);
        favoriteMapper.deleteFavoritesByPost(id);
    }

    public void restore(Long id) {
        postMapper.restore(id);
    }
}
