package com.adlifehub.adlife.security;

import com.adlifehub.adlife.service.UserService;
import com.adlifehub.adlife.model.User;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserService users;

  public JwtAuthFilter(JwtUtil jwtUtil, UserService users) {
    this.jwtUtil = jwtUtil;
    this.users = users;
  }

  // 这些路径/方法直接跳过，不做 JWT 校验
  @Override
  protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
    String path = req.getRequestURI();
    return HttpMethod.OPTIONS.matches(req.getMethod())
            || path.startsWith("/auth/")
            || path.startsWith("/swagger-ui/")
            || path.startsWith("/v3/api-docs/");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws ServletException, IOException {

    String header = req.getHeader(HttpHeaders.AUTHORIZATION);

    // 没有 Authorization 就直接放行，让后续 Security 规则决定是否需要登录
    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(req, res);
      return;
    }

    String token = header.substring(7);
    try {
      var jws = jwtUtil.parse(token);
      String username = jws.getBody().getSubject();

      User u = users.findByUsername(username);
      if (u != null) {
        var auths = u.getRoleList().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        var auth = new UsernamePasswordAuthenticationToken(username, null, auths);
        SecurityContextHolder.getContext().setAuthentication(auth);
      } else {
        SecurityContextHolder.clearContext();
      }

      // 不论 token 是否有效，都不要在这里返回 401/403，交给后续链处理
      chain.doFilter(req, res);

    } catch (JwtException | IllegalArgumentException e) {
      // token 非法：清空上下文并放行，后续规则会按需要返回 401
      SecurityContextHolder.clearContext();
      chain.doFilter(req, res);
    }
  }
}
