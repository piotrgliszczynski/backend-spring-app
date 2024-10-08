package com.training.security;

import com.training.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final static String AUTHORIZATION_HEADER = "Authorization";
  private final static String BEARER = "Bearer ";

  private final JwtService jwtService;
  private final HandlerExceptionResolver exceptionResolver;

  public JwtAuthenticationFilter(
      JwtService jwtService,
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.jwtService = jwtService;
    this.exceptionResolver = exceptionResolver;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (authHeader == null || !authHeader.startsWith(BEARER)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = authHeader.substring(7);
      if (jwtService.verify(token)) {
        UserDetails userDetails = jwtService.buildUser(token);
        UsernamePasswordAuthenticationToken authToken
            = new UsernamePasswordAuthenticationToken(
            userDetails, null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
      filterChain.doFilter(request, response);
    } catch (Exception ex) {
      exceptionResolver.resolveException(request, response, null, ex);
    }
  }
}
