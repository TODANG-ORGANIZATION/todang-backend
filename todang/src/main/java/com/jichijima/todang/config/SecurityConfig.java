package com.jichijima.todang.config;

import com.jichijima.todang.filter.JwtAuthenticationFilter;
import com.jichijima.todang.service.CustomUserDetailsService;
import com.jichijima.todang.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화 (테스트용)
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)) // ✅ 세션 사용 안 함
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup",  // ✅ 회원가입 허용
                                "/api/users/login",   // ✅ 로그인 허용
                                "/api/users/check-nickname",
                                "/api/users/check-email",
                                "/api/users/logout",
                                "/error"              // ✅ 에러 페이지도 허용 (403 방지)
                        ).permitAll()
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class) // ✅ JWT 필터 추가
                .formLogin(login -> login.disable()) // 기본 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        return http.build();
    }
}
