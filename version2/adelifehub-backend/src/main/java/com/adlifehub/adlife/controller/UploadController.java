package com.adlifehub.adlife.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
public class UploadController {
    @Value("${app.upload.dir}")
    private String uploadDir;

    private Path ensureDir() throws IOException {
        Path p = Paths.get(uploadDir).toAbsolutePath();
        Files.createDirectories(p);
        return p;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, Authentication auth) throws IOException {
        if (auth == null) return ResponseEntity.status(401).build();
        Path dir = ensureDir();
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = UUID.randomUUID().toString().replace("-", "") + (ext.isEmpty() ? "" : "." + ext);
        Files.write(dir.resolve(name), file.getBytes());
        return ResponseEntity.status(201).body(Map.of("url", "/uploads/" + name));
    }

    @PostMapping("/upload/batch")
    public ResponseEntity<?> uploadBatch(@RequestParam("files") List<MultipartFile> files, Authentication auth) throws IOException {
        if (auth == null) return ResponseEntity.status(401).build();
        List<Map<String, String>> out = new ArrayList<>();
        Path dir = ensureDir();
        for (MultipartFile f : files) {
            String ext = FilenameUtils.getExtension(f.getOriginalFilename());
            String name = UUID.randomUUID().toString().replace("-", "") + (ext.isEmpty() ? "" : "." + ext);
            Files.write(dir.resolve(name), f.getBytes());
            out.add(Map.of("url", "/uploads/" + name));
        }
        return ResponseEntity.status(201).body(Map.of("files", out));
    }

    @DeleteMapping("/files/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id, Authentication auth) throws IOException {
        if (auth == null) return ResponseEntity.status(401).build();
        Path p = Paths.get(uploadDir).toAbsolutePath().resolve(id);
        if (Files.exists(p)) Files.delete(p);
        return ResponseEntity.noContent().build();
    }
}
