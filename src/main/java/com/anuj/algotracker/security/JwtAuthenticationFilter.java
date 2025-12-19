package com.anuj.algotracker.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JWTService jwtService,
            CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1) Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // If no header or doesn't start with Bearer, just continue
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Extract token (without "Bearer ")
        String token = authHeader.substring(7);

        // 3) Extract username (email) from token
        String username = jwtService.extractUsername(token);

        // 4) If we got a username and no auth yet in context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token against this user
            if (jwtService.isTokenValid(token, userDetails.getUsername())) {

                // Create authentication object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Attach request details (IP, session, etc.)
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // 5) Put auth into SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6) Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
