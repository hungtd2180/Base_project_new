package org.example.base.services.cache;

import org.example.base.models.entity.token.Token;
import org.example.base.repositories.token.TokenRepository;
import org.example.base.services.MemoryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hungtd
 * Date: 30/12/2025
 * Time: 11:25 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class TokenCacheService extends MemoryCacheService<Token, Long> {
    private final Logger logger = LoggerFactory.getLogger(TokenCacheService.class);
    private TokenRepository tokenRepository;
    private final Map<String, Token> tokenMap = new ConcurrentHashMap<>();
    public TokenCacheService(TokenRepository repository) {
        this.repository = tokenRepository = repository;
    }

    @Override
    public void initData() {
        logger.info("Init data for AccessTokenCacheService");
        tokenMap.putAll(repository.findAll().stream().collect(ConcurrentHashMap::new, (m, v) -> m.put(v.getToken(), v), ConcurrentHashMap::putAll));
        super.initData();
    }

    public Token getByToken(String token){
        Token data = tokenMap.get(token);
        if (data == null) {
            data = tokenRepository.findFirstByToken(token);
            if (data != null) {
                tokenMap.put(token, data);
            }
        }
        return data;
    }
}
