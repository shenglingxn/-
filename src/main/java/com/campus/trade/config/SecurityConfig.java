package com.campus.trade.config;

import com.campus.trade.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers("/", "/index.html", "/favicon.ico").permitAll()
<<<<<<< HEAD
                .antMatchers("/css/**", "/js/**", "/img/**", "/uploads/**").permitAll()
                .antMatchers("/api/user/register", "/api/user/login", "/api/user/send-code").permitAll()
                .antMatchers("/api/goods/list", "/api/goods/{id:[0-9]+}").permitAll()
                .antMatchers("/api/upload/**").permitAll()
                .antMatchers("/api/goods/pending", "/api/goods/review", "/api/goods/onsale", "/api/goods/delist").hasRole("admin")
=======
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/api/user/register", "/api/user/login", "/api/user/send-code").permitAll()
                .antMatchers("/api/goods/list", "/api/goods/**").permitAll()
>>>>>>> 464492e47d40cf433f66cc94246af5cfd132a45b
                .antMatchers("/api/admin/**").hasRole("admin")
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
