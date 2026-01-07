package org.example.base.services.cache;

import org.example.base.models.entity.user.User;
import org.example.base.repositories.user.UserRepository;
import org.example.base.services.MemoryCacheService;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 1:45 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class UserCacheService extends MemoryCacheService<User, Long> {

    public UserCacheService(UserRepository repository) {
        this.repository = repository;
    }
}
