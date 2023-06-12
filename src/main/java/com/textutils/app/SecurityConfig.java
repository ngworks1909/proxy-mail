package com.textutils.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception{

        http    
                .authorizeHttpRequests((authz) -> authz
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/Register").permitAll()
                            .requestMatchers("/Otp").permitAll()
                            .requestMatchers("/Login").permitAll()
                            .requestMatchers("/Email").permitAll()
                            .requestMatchers("/Success").permitAll()
                            .requestMatchers("/Forgot").permitAll()
                            .requestMatchers("/VerifyOtp").permitAll()
                            .requestMatchers("/ChangePassword").permitAll()
                                       
                );
            return http.build();
        
    }

}
