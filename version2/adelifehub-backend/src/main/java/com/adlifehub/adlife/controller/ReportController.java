package com.adlifehub.adlife.controller;

import com.adlifehub.adlife.mapper.UserMapper;
import com.adlifehub.adlife.model.Report;
import com.adlifehub.adlife.model.User;
import com.adlifehub.adlife.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ReportController {
    private final ReportService reports;
    private final UserMapper users;

    public ReportController(ReportService reports, UserMapper users) {
        this.reports = reports;
        this.users = users;
    }

    @PostMapping("/listings/{id}/report")
    public ResponseEntity<?> reportListing(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        String reasonCode = body.getOrDefault("reasonCode", "other");
        String reasonText = body.get("reasonText");
        reports.create("listing", id, u.getId(), reasonCode, reasonText);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{id}/report")
    public ResponseEntity<?> reportPost(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).build();
        User u = users.findByUsername(auth.getName());
        String reasonCode = body.getOrDefault("reasonCode", "other");
        String reasonText = body.get("reasonText");
        reports.create("post", id, u.getId(), reasonCode, reasonText);
        return ResponseEntity.noContent().build();
    }
}
