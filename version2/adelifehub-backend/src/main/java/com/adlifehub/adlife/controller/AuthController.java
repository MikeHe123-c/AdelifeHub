package com.adlifehub.adlife.controller;

import com.adlifehub.adlife.model.User;
import com.adlifehub.adlife.security.JwtUtil;
import com.adlifehub.adlife.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwt;
    private final UserService users;

    public AuthController(JwtUtil jwt, UserService users) {
        this.jwt = jwt;
        this.users = users;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String email = body.get("email");
        String password = body.get("password");

        if (username == null || password == null) {
            throw new IllegalArgumentException("username/password required");
        }

        User u = users.register(username, email, password);
        return ResponseEntity.status(201).body(
                Map.of(
                        "id", u.getId(),
                        "username", u.getUsername(),
                        "email", u.getEmail()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User u = users.findByUsername(username);
        if (u == null || !users.checkPassword(u, password)) {
            return ResponseEntity.status(401).body(
                    Map.of("code", "UNAUTHORIZED", "message", "bad credentials")
            );
        }

        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        var roles = isAdmin ? java.util.List.of("ADMIN", "USER") : java.util.List.of("USER");

        String token = jwt.generateToken(u.getUsername(), roles);
        String refresh = jwt.generateToken(u.getUsername(), roles);

        return ResponseEntity.ok(
                Map.of(
                        "accessToken", token,
                        "refreshToken", refresh,
                        "tokenType", "Bearer",
                        "expiresIn", 3600
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String r = body.get("refreshToken");
        if (r == null) throw new IllegalArgumentException("refreshToken required");

        var jws = jwt.parse(r);
        String username = jws.getBody().getSubject();
        User u = users.findByUsername(username);
        if (u == null) return ResponseEntity.status(401).build();

        boolean isAdmin = u.getRolesJson() != null && u.getRolesJson().contains("ADMIN");
        var roles = isAdmin ? java.util.List.of("ADMIN", "USER") : java.util.List.of("USER");

        String token = jwt.generateToken(u.getUsername(), roles);
        String refresh = jwt.generateToken(u.getUsername(), roles);

        return ResponseEntity.ok(
                Map.of(
                        "accessToken", token,
                        "refreshToken", refresh,
                        "tokenType", "Bearer",
                        "expiresIn", 3600
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody Map<String, String> body) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> body) {
        return ResponseEntity.noContent().build();
    }
}
