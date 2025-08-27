package com.adlifehub.adlife.service;

import com.adlifehub.adlife.mapper.CommentMapper;
import com.adlifehub.adlife.model.Comment;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<Comment> list(String targetType, Long targetId, int offset, int limit) {
        return commentMapper.listByTarget(targetType, targetId, offset, limit);
    }

    public int count(String targetType, Long targetId) {
        return commentMapper.countByTarget(targetType, targetId);
    }

    public Comment create(String targetType, Long id, Long authorId, String content) {
        Comment c = new Comment();
        c.setTargetType(targetType);
        c.setTargetId(id);
        c.setAuthorId(authorId);
        c.setContent(content);
        c.setCreatedAt(OffsetDateTime.now());
        commentMapper.insert(c);
        return c;
    }

    public Comment update(Long id, String content) {
        Comment c = new Comment();
        c.setId(id);
        c.setContent(content);
        c.setEditedAt(OffsetDateTime.now());
        commentMapper.update(c);
        return c;
    }

    public void softDelete(Long id) {
        commentMapper.softDelete(id);
    }


}
