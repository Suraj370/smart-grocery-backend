package com.suraj.smartgroceryapp.utils;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTRequestFilter.class);

    private JWTUtil jwtUtil;

    public JWTRequestFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                log.info("JWT token extracted for user: {}", username);
            } catch (Exception e) {
                log.error("Error extracting username from token", e);
            }
        }

        // This section is a duplicate of the code below, let's merge it for cleaner logic
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Log that the token is valid before setting the authentication
            log.debug("Token is valid, setting authentication for user: {}", username);
            if (jwtUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                log.warn("Failed to validate JWT token for user: {}", username);
            }
        }


        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("No JWT token found in request headers.");
            chain.doFilter(request, response);
            return;
        }

        jwt = authorizationHeader.substring(7);
        if (jwt.isBlank()) {
            log.warn("JWT token is blank.");
            chain.doFilter(request, response);
            return;
        }

        try {
            username = jwtUtil.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Authenticated user '{}' with JWT.", username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.error("Failed to set user authentication from JWT token. Error: {}", e.getMessage(), e);
        } finally {
            chain.doFilter(request, response);
        }
    }


}
