package com.adlifehub.adlife.controller;

import com.adlifehub.adlife.mapper.UserMapper;
import com.adlifehub.adlife.model.Comment;
import com.adlifehub.adlife.model.User;
import com.adlifehub.adlife.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CommentController {
    private final CommentService comments;
    private final UserMapper users;

    public CommentController(CommentService comments, UserMapper users) {
        this.comments = comments;
        this.users = users;
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<?> listPostComments(@PathVariable Long id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        int offset = (Math.max(page, 1) - 1) * Math.max(size, 1);
        var data = comments.list("post", id, offset, size);
        int total = comments.count("post", id);
        return ResponseEntity.ok(Map.of("data", data, "meta", Map.of("page", page, "size", size, "total", total)));
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> createPostComment(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        String content = body.get("content");
        if (content == null || content.isBlank())
            throw new IllegalArgumentException("content required");
        User u = users.findByUsername(auth.getName());
        Comment c = comments.create("post", id, u.getId(), content);
        return ResponseEntity.status(201).body(c);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> update(@PathVariable Long commentId, @RequestBody Map<String, String> body, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        String content = body.get("content");
        if (content == null || content.isBlank())
            throw new IllegalArgumentException("content required");
        Comment c = comments.update(commentId, content);
        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        comments.softDelete(commentId);
        return ResponseEntity.noContent().build();
    }
}
