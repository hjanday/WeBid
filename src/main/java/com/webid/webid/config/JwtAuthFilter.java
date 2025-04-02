package com.webid.webid.config;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.webid.webid.model.RoleEnum;
import com.webid.webid.model.User;
import com.webid.webid.service.JwtService;
import com.webid.webid.service.LogoutService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final LogoutService logoutService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService, LogoutService logoutService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.logoutService = logoutService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String token = null;
        String authHeader = request.getHeader("Authorization");
    
        // First, try to get the token from the Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // If not found in the header, check the cookies
        if (token == null && request.getCookies() != null) {
            
            for (Cookie cookie : request.getCookies()) {
                
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        
        if (token != null) {
            
            // Check if the token is blacklisted
            if (logoutService.isTokenBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token has been invalidated. Please log in again.");
                return;
            }
    
            String username = jwtService.extractUsername(token);
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = (User) this.userDetailsService.loadUserByUsername(username);
    
                if (jwtService.isTokenValid(token, user)) {
                    
                    // Extract the roles from the token
                    List<RoleEnum> roles = jwtService.extractRoles(token); 
                    
                    // Map roles to granted authority
                    Collection<? extends GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role.name()))
                            .collect(Collectors.toList());
                    
                    // Create token
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            
            }
        }

        filterChain.doFilter(request, response);
    }
}    
