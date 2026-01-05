package org.example.base.repositories.token;

import org.example.base.models.entity.token.Token;
import org.example.base.repositories.CustomJpaRepository;

public interface TokenRepository extends CustomJpaRepository<Token, Long> {
    Token findFirstByToken(String token);
    Token findFirstByUserId(Long userId);
    Token findByTokenAndUserId(String token, Long userId);
    Token findByUserIdAndRefreshTokenId(Long token, String refreshTokenId);
}
