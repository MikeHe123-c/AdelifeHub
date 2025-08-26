package com.adlifehub.adlife.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String rolesJson;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public List<String> getRoleList() {
        String json = getRolesJson();   // 确保你已有 getRolesJson()/setRolesJson()
        if (json == null || json.isBlank()) {
            // 如果希望给未设置角色的用户默认 USER，可以改成：return List.of("USER");
            return List.of();
        }
        try {
            if (json.startsWith("[")) {
                // 标准 JSON 数组：["ADMIN","USER"]
                return MAPPER.readValue(json, new TypeReference<List<String>>() {});
            } else if (json.contains(",")) {
                // 逗号分隔：ADMIN,USER
                return Arrays.asList(json.split("\\s*,\\s*"));
            } else {
                // 单个角色字符串：ADMIN
                return List.of(json.trim());
            }
        } catch (Exception e) {
            // 兜底（也可以打日志）
            return List.of();
        }
    }
}
