package com.adlifehub.adlife.web;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> bad(IllegalArgumentException e) { return ResponseEntity.badRequest().body(Map.of("code","VALIDATION_ERROR","message", e.getMessage())); }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> valid(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldErrors().stream().findFirst().map(f->f.getField()+": "+f.getDefaultMessage()).orElse("invalid");
    return ResponseEntity.badRequest().body(Map.of("code","VALIDATION_ERROR","message", msg));
  }
  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<?> forbidden(SecurityException e) { return ResponseEntity.status(403).body(Map.of("code","FORBIDDEN","message", e.getMessage())); }
}
