package com.webid.webid.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.webid.webid.config.JwtAuthFilter;

import jakarta.ws.rs.HttpMethod;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final AuthenticationProvider authProvider;

    public SecurityConfig(JwtAuthFilter jwtFilter, AuthenticationProvider authProvider) {
        this.jwtFilter = jwtFilter;
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // allow requests for users and admins
                    .requestMatchers("/","/auth/**").permitAll()
                    .requestMatchers("/","/authentication/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("ROLE_ADMIN")
                    // add all the other routes to request matchers for user access?
                    .requestMatchers("/", "/api/auctions/**").permitAll()
                    .requestMatchers("/", "/api/bid/**").hasAuthority("ROLE_USER")
                    .requestMatchers("/", "/api/notification/**").hasAuthority("ROLE_USER")
                    .requestMatchers("/", "/api/payments/**").hasAuthority("ROLE_USER")
                    .requestMatchers("/", "/checkout/**").hasAuthority("ROLE_USER")
                    // change to permitall for testing 
                    .requestMatchers("/", "/admin/**").hasAuthority("ROLE_ADMIN")
                    // add all the other routes to request matchers for user access?
                    
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
