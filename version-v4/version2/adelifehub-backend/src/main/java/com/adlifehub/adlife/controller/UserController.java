package com.adlifehub.adlife.controller;

import com.adlifehub.adlife.mapper.UserMapper;
import com.adlifehub.adlife.model.User;
import com.adlifehub.adlife.service.FavoriteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserMapper userMapper;
    private final FavoriteService favoriteService;

    public UserController(UserMapper userMapper, FavoriteService favoriteService) {
        this.userMapper = userMapper;
        this.favoriteService = favoriteService;
    }

    // ---- 当前登录用户信息 ----
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = userMapper.findByUsername(auth.getName());
        if (u == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(toUserMap(u));
    }

    // ---- 修改我的资料 ----
    @PatchMapping("/me")
    public ResponseEntity<?> updateMe(Authentication auth, @RequestBody Map<String, String> body) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = userMapper.findByUsername(auth.getName());
        if (u == null) return ResponseEntity.status(401).build();

        if (body.containsKey("nickname")) u.setNickname(body.get("nickname"));
        if (body.containsKey("avatarUrl")) u.setAvatarUrl(body.get("avatarUrl"));
        if (body.containsKey("phone")) u.setPhone(body.get("phone"));
        userMapper.updateProfile(u);

        return ResponseEntity.ok(toUserMap(u));
    }

    // ✅ 我的收藏（/users/me/favorites）
    @GetMapping("/me/favorites")
    public ResponseEntity<?> myFavorites(@RequestParam String type,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = userMapper.findByUsername(auth.getName());
        if (u == null) return ResponseEntity.status(401).build();

        // 映射前端 type 到 service 所需 targetType/listingType
        String targetType;       // "listing" 或 "post"
        String listingType = null; // "rental" | "job" | null
        switch (type) {
            case "post" -> targetType = "post";
            case "rental", "job" -> {
                targetType = "listing";
                listingType = type;
            }
            default -> {
                return ResponseEntity.badRequest().body(
                        Map.of("code", "VALIDATION_ERROR", "message", "type must be rental|job|post")
                );
            }
        }

        int p = Math.max(page, 1);
        int s = Math.max(size, 1);
        int offset = (p - 1) * s;

        var data = Optional.ofNullable(
                favoriteService.list(u.getId(), targetType, listingType, offset, s)
        ).orElse(List.of());
        int total = favoriteService.count(u.getId(), targetType, listingType);

        // 用可变 Map，避免 null 触发 Map.of NPE
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("page", p);
        meta.put("size", s);
        meta.put("total", total);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("data", data);
        out.put("meta", meta);

        return ResponseEntity.ok(out);
    }

    // ====== 辅助：把 User 转成可包含 null 的 Map 输出 ======
    private Map<String, Object> toUserMap(User u) {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("id", u.getId());
        out.put("username", u.getUsername());
        out.put("nickname", u.getNickname());
        out.put("avatarUrl", u.getAvatarUrl());
        out.put("phone", u.getPhone());
        out.put("email", u.getEmail());
        out.put("roles", safeRoleList(u));
        return out;
    }

    // 优先使用 User#getRoleList()；没有则从 rolesJson 解析
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private List<String> safeRoleList(User u) {
        try {
            var m = User.class.getMethod("getRoleList");
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) m.invoke(u);
            if (roles != null) return roles;
        } catch (Exception ignore) { }
        String json = u.getRolesJson();
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            if (json.startsWith("[")) {
                return MAPPER.readValue(json, new TypeReference<List<String>>() {});
            } else if (json.contains(",")) {
                return Arrays.asList(json.split("\\s*,\\s*"));
            } else {
                return List.of(json.trim());
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
