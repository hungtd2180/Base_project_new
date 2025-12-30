package org.example.base.configurations;

import org.example.base.filters.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}
