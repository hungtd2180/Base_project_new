package org.example.base.configurations;

import org.example.base.services.cache.TokenCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by hungtd
 * Date: 29/12/2025
 * Time: 10:03 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(InitialDataLoader.class);
    private TokenCacheService tokenCacheService;
    @Autowired
    public void setAccessTokenCacheService(TokenCacheService tokenCacheService) {
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        tokenCacheService.initData();
    }
}
