package com.adlifehub.adlife.controller;

import com.adlifehub.adlife.mapper.UserMapper;
import com.adlifehub.adlife.model.Post;
import com.adlifehub.adlife.model.User;
import com.adlifehub.adlife.service.FavoriteService;
import com.adlifehub.adlife.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService posts;
    private final FavoriteService favorites;
    private final UserMapper users;

    public PostController(PostService posts, FavoriteService favorites, UserMapper users) {
        this.posts = posts;
        this.favorites = favorites;
        this.users = users;
    }

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        int offset = (safePage - 1) * safeSize;

        var data = posts.list(offset, safeSize);
        int total = posts.countActive();

        return ResponseEntity.ok(
                Map.of(
                        "data", data,
                        "meta", Map.of(
                                "page", safePage,
                                "size", safeSize,
                                "total", total
                        )
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Post p = posts.getVisible(id);
        if (p == null) return ResponseEntity.status(404).build();

        User au = users.findById(p.getAuthorId());
        Map<String, Object> author = new HashMap<>();
        if (au != null) {
            author.put("id", au.getId());
            author.put("nickname", au.getNickname());
            author.put("username", au.getUsername());
            author.put("avatarUrl", au.getAvatarUrl());
        }

        Map<String, Object> out = new HashMap<>();
        out.put("id", p.getId());
        out.put("title", p.getTitle());
        out.put("content", p.getContent());
        out.put("images", p.getImages());
        out.put("authorId", p.getAuthorId());
        out.put("author", author);
        out.put("likes", p.getLikes());
        out.put("createdAt", p.getCreatedAt());
        out.put("updatedAt", p.getUpdatedAt());

        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Post body, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();

        User u = users.findByUsername(auth.getName());
        Post created = posts.create(body, u.getId());
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Post patch, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        if (u == null) return ResponseEntity.status(401).build();
        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        try {
            Post updated = posts.update(id, patch, u.getId(), isAdmin);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("code", "FORBIDDEN", "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @RequestParam(defaultValue = "user") String reason, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        if (u == null) return ResponseEntity.status(401).build();
        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        try {
            posts.softDelete(id, u.getId(), reason, isAdmin);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("code", "FORBIDDEN", "message", e.getMessage()));
        }
    }
}