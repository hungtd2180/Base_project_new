package org.example.base.services.cache;

import org.example.base.models.entity.token.AccessToken;
import org.example.base.repositories.token.AccessTokenRepository;
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
public class AccessTokenCacheService extends MemoryCacheService<AccessToken, Long> {
    private final Logger logger = LoggerFactory.getLogger(AccessTokenCacheService.class);
    private AccessTokenRepository accessTokenRepository;
    private final Map<String, AccessToken> tokenMap = new ConcurrentHashMap<>();
    public AccessTokenCacheService(AccessTokenRepository repository) {
        this.repository = accessTokenRepository = repository;
    }

    @Override
    public void initData() {
        logger.info("Init data for AccessTokenCacheService");
        tokenMap.putAll(repository.findAll().stream().collect(ConcurrentHashMap::new, (m, v) -> m.put(v.getToken(), v), ConcurrentHashMap::putAll));
        super.initData();
    }

    public AccessToken getByToken(String token){
        AccessToken data = tokenMap.get(token);
        if (data == null) {
            data = accessTokenRepository.findFirstByToken(token);
            if (data != null) {
                tokenMap.put(token, data);
            }
        }
        return data;
    }
}
