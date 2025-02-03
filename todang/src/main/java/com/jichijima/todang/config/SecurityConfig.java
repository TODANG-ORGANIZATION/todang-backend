package com.jichijima.todang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/check-nickname",
                                "/api/users/check-email"
                        ).permitAll() // 🔥 회원가입, 중복 체크 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                )
                .formLogin(login -> login.disable()) // 기본 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP Basic 인증 비활성화

        return http.build();
    }
}
