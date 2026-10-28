package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                        "/error",
                        "/login",
                        "/register",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**"
                ).permitAll()

                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                .requestMatchers("/quan-ly/**")
                .hasAnyRole("QUAN_LY", "ADMIN")

                .requestMatchers("/user/**", "/nguoi-dung/**")
                .hasAnyRole("USER", "QUAN_LY", "ADMIN")

                .requestMatchers(HttpMethod.POST,
                        "/phieunhap/**",
                        "/phieuxuat/**",
                        "/nha-cung-cap/**",
                        "/san-pham/**",
                        "/lo-hang/**")
                .hasAnyRole("ADMIN", "QUAN_LY")

                .anyRequest()
                .authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl(
                        "/login?logout=true"
                )

                .permitAll()
            );

        return http.build();
    }
}