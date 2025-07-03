package org.example.base.repositories.token;

import org.example.base.models.entity.token.AccessToken;
import org.example.base.repositories.CustomJpaRepository;

public interface AccessTokenRepository extends CustomJpaRepository<AccessToken, Long> {
    AccessToken findFirstByToken(String token);
}
