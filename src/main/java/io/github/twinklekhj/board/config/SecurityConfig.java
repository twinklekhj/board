package io.github.twinklekhj.board.config;

import io.github.twinklekhj.board.jwt.JwtAuthenticationFilter;
import io.github.twinklekhj.board.login.CustomAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenticationProvider authenticationProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(authenticationProvider);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                           CorsProperties properties, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.cors(configurer -> configurer.configurationSource(source -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(properties.getAllowedOrigins());
            config.setAllowedMethods(properties.getAllowedMethods());
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setMaxAge(3600L);
            return config;
        }))
        .csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(configurer -> {
            configurer
                    .requestMatchers("/**").permitAll()
                    .requestMatchers("/api/authenticate").permitAll()
                    .requestMatchers("/api/**").authenticated();
        });
        http.formLogin(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager)
                .logout(configurer -> {
                    configurer.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"));
                    configurer.invalidateHttpSession(true);
                });

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
