package com.bookend.security.config;

import com.bookend.security.service.OAuth2UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserServiceImpl oAuth2UserServiceImpl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/css/**", "/images/**", "/js/**", "/join/**").permitAll()
                                .anyRequest().authenticated())
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/login/page").permitAll()
                                .loginProcessingUrl("/login"))
                .oauth2Login((oauth2Login) ->
                        oauth2Login
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(oAuth2UserServiceImpl)));


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
