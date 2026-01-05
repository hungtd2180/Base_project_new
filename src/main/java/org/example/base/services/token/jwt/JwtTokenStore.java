package org.example.base.services.token.jwt;

import org.example.base.constants.Constant;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.RefreshToken;
import org.example.base.models.entity.token.Token;
import org.example.base.repositories.token.TokenRepository;
import org.example.base.services.cache.TokenCacheService;
import org.example.base.services.token.ITokenStore;
import org.example.base.utils.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 05/01/2026
 * Time: 10:31 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
@ConditionalOnProperty(name = "auth.type", havingValue = Constant.GrantTypeToken.JWT, matchIfMissing = true)
public class JwtTokenStore implements ITokenStore {
    private TokenCacheService tokenCacheService;
    private TokenRepository tokenRepository;

    public JwtTokenStore(TokenCacheService tokenCacheService, TokenRepository tokenRepository) {
        this.tokenCacheService = tokenCacheService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void storeToken(Token token, TokenRequest tokenRequest) {
        token = tokenRepository.save(token);
        tokenCacheService.put(token.getId(), token);
    }

    @Override
    public Token readToken(String token, Long userId) {
        Token tokenEntity;
        if (!ObjectUtils.isEmpty(userId)) {
            tokenEntity = tokenRepository.findByTokenAndUserId(token, userId);
        } else {
            tokenEntity = tokenRepository.findFirstByToken(token);
        }
        return tokenEntity;
    }

    @Override
    public void removeToken(Token token) {
        Token tokenCache = tokenCacheService.getByToken(token.getToken());
        if (!ObjectUtils.isEmpty(tokenCache)) {
            tokenCacheService.remove(tokenCache.getId());
        }
        tokenRepository.delete(token);
    }

    @Override
    public void storeRefreshToken(RefreshToken refreshToken, TokenRequest tokenRequest) {
    }

    @Override
    public RefreshToken readRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
    }

    @Override
    public Token removeAccessTokenUsingRefreshToken(RefreshToken refreshToken) {
        return null;
    }

    @Override
    public Token getToken(TokenRequest tokenRequest) {
        if (tokenRequest.getUserId() == null) {
            return null;
        }
        Token token = tokenRepository.findFirstByUserId(tokenRequest.getUserId());
        return token;
    }
}
