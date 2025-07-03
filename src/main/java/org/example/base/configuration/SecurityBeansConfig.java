package org.example.base.configuration;

import org.example.base.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfig {
    public SecurityBeansConfig() {
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}
