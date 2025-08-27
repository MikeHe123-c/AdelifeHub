package com.adlifehub.adlife.controller;

import com.adlifehub.adlife.mapper.UserMapper;
import com.adlifehub.adlife.model.Comment;
import com.adlifehub.adlife.model.Listing;
import com.adlifehub.adlife.model.User;
import com.adlifehub.adlife.service.CommentService;
import com.adlifehub.adlife.service.FavoriteService;
import com.adlifehub.adlife.service.ListingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ListingController {

    private final ListingService listings;
    private final FavoriteService favorites;
    private final UserMapper users;
    private final CommentService comments;

    public ListingController(ListingService listings,
                             FavoriteService favorites,
                             UserMapper users,
                             CommentService comments) {
        this.listings = listings;
        this.favorites = favorites;
        this.users = users;
        this.comments = comments;
    }

    // ======================
    // 列表 & 详情（匿名可读）
    // ======================

    @GetMapping("/listings")
    public ResponseEntity<?> list(@RequestParam(required = false) String type,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        int p = Math.max(page, 1);
        int s = Math.max(size, 1);
        int offset = (p - 1) * s;

        List<Listing> data = listings.list(type, offset, s);
        int total = listings.count(type);

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("page", p);
        meta.put("size", s);
        meta.put("total", total);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("data", data);
        out.put("meta", meta);

        return ResponseEntity.ok(out);
    }

    @GetMapping("/listings/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        Listing l = listings.getVisible(id);
        if (l == null) return ResponseEntity.status(404).build();

        User au = users.findById(l.getAuthorId());
        Map<String, Object> author = new HashMap<>();
        if (au != null) {
            author.put("id", au.getId());
            author.put("nickname", au.getNickname());
            author.put("username", au.getUsername());
            author.put("avatarUrl", au.getAvatarUrl());
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("id", l.getId());
        out.put("type", l.getType());
        out.put("title", l.getTitle());
        out.put("content", l.getContent());
        out.put("price", l.getPrice());
        out.put("priceUnit", l.getPriceUnit());
        out.put("location", l.getLocation());
        out.put("latitude", l.getLatitude());
        out.put("longitude", l.getLongitude());
        out.put("images", l.getImages());
        out.put("authorId", l.getAuthorId());
        out.put("author", author);
        out.put("createdAt", l.getCreatedAt());
        out.put("updatedAt", l.getUpdatedAt());
        out.put("rental", l.getRental());
        out.put("job", l.getJob());

        return ResponseEntity.ok(out);
    }

    // ======================
    // 创建 / 修改 / 软删 / 恢复
    // ======================

    @PostMapping("/listings")
    public ResponseEntity<?> create(@RequestBody Listing body, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        Listing created = listings.create(body, u.getId());
        return ResponseEntity.status(201).body(created);
    }

    @PatchMapping("/listings/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Listing patch,
                                    Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        try {
            Listing updated = listings.update(id, patch, u.getId(), isAdmin);
            return ResponseEntity.ok(updated);
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("code", "FORBIDDEN", "message", e.getMessage()));
        }
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam(defaultValue = "user") String reason,
                                    Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        try {
            listings.softDelete(id, u.getId(), reason, isAdmin);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("code", "FORBIDDEN", "message", e.getMessage()));
        }
    }

    @PostMapping("/listings/{id}/restore")
    public ResponseEntity<?> restore(@PathVariable Long id, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        if (!isAdmin) {
            return ResponseEntity.status(403)
                    .body(Map.of("code", "FORBIDDEN", "message", "admin only"));
        }
        listings.restore(id);
        return ResponseEntity.ok().build();
    }

    // ======================
    // 收藏 / 取消收藏
    // ======================

    @PostMapping("/listings/{id}/favorite")
    public ResponseEntity<?> favListing(@PathVariable Long id, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        favorites.addListing(u.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/listings/{id}/favorite")
    public ResponseEntity<?> unfavListing(@PathVariable Long id, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        favorites.removeListing(u.getId(), id);
        return ResponseEntity.noContent().build();
    }

    // ======================
    // 评论（列表匿名、创建需登录）
    // ======================

    @GetMapping("/listings/{id}/comments")
    public ResponseEntity<?> listComments(@PathVariable Long id,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        int p = Math.max(page, 1);
        int s = Math.max(size, 1);
        int offset = (p - 1) * s;

        List<Comment> data = comments.list("listing", id, offset, s);
        int total = comments.count("listing", id);

        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("page", p);
        meta.put("size", s);
        meta.put("total", total);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("data", data);
        out.put("meta", meta);

        return ResponseEntity.ok(out);
    }

    @PostMapping("/listings/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody Map<String, String> body,
                                        Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("code", "VALIDATION_ERROR", "message", "content required"));
        }
        User u = users.findByUsername(auth.getName());
        Comment created = comments.create("listing", id, u.getId(), content);
        return ResponseEntity.status(201).body(created);
    }
}
