package org.example.base.services.token.accesstoken;

import org.example.base.constants.Constant;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.RefreshToken;
import org.example.base.models.entity.token.Token;
import org.example.base.repositories.token.RefreshTokenRepository;
import org.example.base.repositories.token.TokenRepository;
import org.example.base.services.cache.TokenCacheService;
import org.example.base.services.token.ITokenStore;
import org.example.base.utils.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 9:12 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
@ConditionalOnProperty(name = "auth.type", havingValue = Constant.GrantTypeToken.ACCESS_TOKEN)
public class AccessTokenStore implements ITokenStore {
    private TokenCacheService tokenCacheService;
    private TokenRepository tokenRepository;
    private RefreshTokenRepository refreshTokenRepository;

    public AccessTokenStore(TokenCacheService tokenCacheService, TokenRepository tokenRepository, RefreshTokenRepository refreshTokenRepository) {
        this.tokenCacheService = tokenCacheService;
        this.tokenRepository = tokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void storeToken(Token token, TokenRequest tokenRequest) {
        if (!ObjectUtils.isEmpty(token.getRefreshToken())){
            token.setRefreshTokenId(token.getRefreshToken().getToken());
        }
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
        if (!ObjectUtils.isEmpty(tokenEntity.getRefreshTokenId())) {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenEntity.getRefreshTokenId());
            if (!ObjectUtils.isEmpty(refreshToken)) {
                tokenEntity.setRefreshToken(refreshToken);
            }
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
        if (!ObjectUtils.isEmpty(token.getRefreshTokenId())) {
            removeRefreshToken(refreshTokenRepository.findByToken(token.getRefreshTokenId()));
        }
    }

    @Override
    public void storeRefreshToken(RefreshToken refreshToken, TokenRequest tokenRequest) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken readRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }

    @Override
    public Token removeAccessTokenUsingRefreshToken(RefreshToken refreshToken) {
        Token token = tokenRepository.findByUserIdAndRefreshTokenId(refreshToken.getUserId(), refreshToken.getToken());
        if (ObjectUtils.isEmpty(token)) {
            return null;
        }
        Token tokenCache = tokenCacheService.getByToken(token.getToken());
        if (!ObjectUtils.isEmpty(tokenCache)) {
            tokenCacheService.remove(tokenCache.getId());
        }
        tokenRepository.delete(token);
        return token;
    }

    @Override
    public Token getToken(TokenRequest tokenRequest) {
        if (tokenRequest.getUserId() == null) {
            return null;
        }
        Token token = tokenRepository.findFirstByUserId(tokenRequest.getUserId());
        if (!ObjectUtils.isEmpty(token) && !ObjectUtils.isEmpty(token.getRefreshTokenId())) {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token.getRefreshTokenId());
            token.setRefreshToken(refreshToken);
            token.setRefreshTokenId(refreshToken.getToken());
        }
        return token;
    }
}
