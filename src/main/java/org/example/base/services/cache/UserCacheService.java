package org.example.base.services.cache;

import org.example.base.models.entity.user.User;
import org.example.base.repositories.user.UserRepository;
import org.example.base.services.MemoryCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 1:45 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class UserCacheService extends MemoryCacheService<User, Long> {
    private final Logger logger = LoggerFactory.getLogger(TokenCacheService.class);
    private UserRepository userRepository;
    private final Map<String, User> userMap = new ConcurrentHashMap<>();
    public UserCacheService(UserRepository repository) {
        this.repository = userRepository = repository;
    }
}
