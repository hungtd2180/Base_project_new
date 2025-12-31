package org.example.base.services.token;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.AccessToken;
import org.example.base.models.entity.token.RefreshToken;
import org.example.base.repositories.token.AccessTokenRepository;
import org.example.base.repositories.token.RefreshTokenRepository;
import org.example.base.services.cache.AccessTokenCacheService;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 9:12 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class TokenStore implements ITokenStore {
    private AccessTokenCacheService accessTokenCacheService;
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public void setAccessTokenCacheService(AccessTokenCacheService accessTokenCacheService) {
        this.accessTokenCacheService = accessTokenCacheService;
    }
    @Autowired
    public void setAccessTokenRepository(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setRefreshTokenRepository(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void storeAccessToken(AccessToken token, TokenRequest tokenRequest) {
        if (!ObjectUtils.isEmpty(token.getRefreshToken())){
            token.setRefreshTokenId(token.getRefreshToken().getToken());
        }
        token = accessTokenRepository.save(token);
        accessTokenCacheService.put(token.getId(), token);
    }

    @Override
    public AccessToken readAccessToken(String token, Long userId) {
        AccessToken tokenEntity;
        if (!ObjectUtils.isEmpty(userId)) {
            tokenEntity = accessTokenRepository.findByTokenAndUserId(token, userId);
        } else {
            tokenEntity = accessTokenRepository.findFirstByToken(token);
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
    public void removeAccessToken(AccessToken token) {
        AccessToken tokenCache = accessTokenCacheService.getByToken(token.getToken());
        if (!ObjectUtils.isEmpty(tokenCache)) {
            accessTokenCacheService.remove(tokenCache.getId());
        }
        accessTokenRepository.delete(token);
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
    public AccessToken removeAccessTokenUsingRefreshToken(RefreshToken refreshToken) {
        AccessToken token = accessTokenRepository.findByUserIdAndRefreshTokenId(refreshToken.getUserId(), refreshToken.getToken());
        if (ObjectUtils.isEmpty(token)) {
            return null;
        }
        AccessToken tokenCache = accessTokenCacheService.getByToken(token.getToken());
        if (!ObjectUtils.isEmpty(tokenCache)) {
            accessTokenCacheService.remove(tokenCache.getId());
        }
        accessTokenRepository.delete(token);
        return token;
    }

    @Override
    public AccessToken getAccessToken(TokenRequest tokenRequest) {
        if (tokenRequest.getUserId() == null) {
            return null;
        }
        AccessToken token = accessTokenRepository.findFirstByUserId(tokenRequest.getUserId());
        if (!ObjectUtils.isEmpty(token) && ObjectUtils.isEmpty(token.getRefreshTokenId())) {
            RefreshToken refreshToken = refreshTokenRepository.findByToken(token.getRefreshTokenId());
            token.setRefreshToken(refreshToken);
            token.setRefreshTokenId(refreshToken.getToken());
        }
        return token;
    }
}
