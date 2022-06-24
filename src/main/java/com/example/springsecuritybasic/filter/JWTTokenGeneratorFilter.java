package com.example.springsecuritybasic.filter;

import com.example.springsecuritybasic.constant.MySecurityConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springsecuritybasic.constant.MySecurityConstant.TIME_TO_EXPIRED;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (null != authentication) {
      // Generate JWT Token
      // secret key use for signature
      SecretKey key =
          Keys.hmacShaKeyFor(MySecurityConstant.JWT_KEY.getBytes(StandardCharsets.UTF_8));
      String jwtToken =
          Jwts.builder()
              .setIssuer("Duyen_Pham")
              .setSubject(("JWT TOKEN"))
              .claim("username", authentication.getName())
              .claim(
                  "authorities",
                  populateAuthorities((List<GrantedAuthority>) authentication.getAuthorities()))
              .setIssuedAt(new Date())
              .setExpiration(new Date(new Date().getTime() + TIME_TO_EXPIRED))
              .signWith(key)
              .compact();

      // Set JWT Token int AUTHORIZATION header
      response.setHeader(HttpHeaders.AUTHORIZATION, jwtToken);
    }

    filterChain.doFilter(request, response);
  }

  // Only jumpping to the generate token when login
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return !request.getServletPath().equals("/login");
  }

  private String populateAuthorities(List<GrantedAuthority> authorities) {
    return authorities.stream()
        .map(auth -> auth.getAuthority().toString())
        .collect(Collectors.joining(","));
  }
}
