package com.adlifehub.adlife.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Component
public class JwtUtil {
  private final SecretKey key; private final long expireSeconds;
  public JwtUtil(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expireSeconds}") long expireSeconds) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes()); this.expireSeconds = expireSeconds;
  }
  public String generateToken(String subject, List<String> roles) {
    Instant now = Instant.now();
    return Jwts.builder().setSubject(subject).setIssuedAt(Date.from(now))
      .setExpiration(Date.from(now.plusSeconds(expireSeconds)))
      .addClaims(Map.of("roles", roles)).signWith(key, SignatureAlgorithm.HS256).compact();
  }
  public Jws<Claims> parse(String jwt) { return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt); }
}
