package org.example.base.services.token;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.AccessToken;
import org.example.base.models.entity.token.RefreshToken;
import org.example.base.models.entity.user.User;
import org.example.base.repositories.token.AccessTokenRepository;
import org.example.base.repositories.token.RefreshTokenRepository;
import org.example.base.services.cache.UserCacheService;
import org.example.base.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.UUID;

/**
 * Created by hungtd
 * Date: 17/07/2025
 * Time: 2:05 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class AccessTokenServiceImpl implements ITokenService {
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private ITokenStore tokenStore;
    private UserCacheService userCacheService;

    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository, ITokenStore tokenStore, UserCacheService userCacheService) {
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenStore = tokenStore;
        this.userCacheService = userCacheService;
    }

    @Override
    public AccessToken createToken(User user, TokenRequest tokenRequest) {
        if (ObjectUtils.isEmpty(tokenRequest.getUserId())) {
            tokenRequest.setUserId(user.getId());
        }
        AccessToken existingToken = this.tokenStore.getAccessToken(tokenRequest);
        if (!ObjectUtils.isEmpty(existingToken)) {
            if (existingToken.isExpired()){
                tokenStore.removeAccessToken(existingToken);
                return createAccessToken(tokenRequest, createRefreshToken(user), user);
            }
            existingToken.setExpiredTime(System.currentTimeMillis() - existingToken.getExpiredTime());
            return existingToken;
        }
        return createAccessToken(tokenRequest, createRefreshToken(user), user);
    }

    @Override
    public AccessToken refreshToken(String refreshToken, TokenRequest tokenRequest) {
        RefreshToken refreshTokenEntity = tokenStore.readRefreshToken(refreshToken);
        if (ObjectUtils.isEmpty(refreshTokenEntity)) {
            throw new InvalidParameterException("Invalid refresh token: " + refreshToken);
        }
        AccessToken oldAccessToken = tokenStore.removeAccessTokenUsingRefreshToken(refreshTokenEntity);
        if (this.isExpired(refreshTokenEntity)) {
            this.tokenStore.removeRefreshToken(refreshTokenEntity);
            throw new InvalidParameterException("Invalid refresh token (expired): " + refreshToken);
        } else {
            tokenStore.removeRefreshToken(refreshTokenEntity);
            if (!ObjectUtils.isEmpty(oldAccessToken)) {
                tokenRequest.setUserId(refreshTokenEntity.getUserId());
            }
            User user = userCacheService.get(refreshTokenEntity.getUserId());
            if (ObjectUtils.isEmpty(user)) {
                throw new InvalidParameterException("Account isn't exists userId: " + refreshTokenEntity.getUserId());
            }
            refreshTokenEntity = createRefreshToken(user);
            tokenRequest.setUsername(user.getUsername());
            AccessToken accessToken = createAccessToken(tokenRequest, refreshTokenEntity, user);
            tokenStore.storeAccessToken(accessToken, tokenRequest);
            if (!ObjectUtils.isEmpty(accessToken.getRefreshToken())) {
                tokenStore.storeRefreshToken(refreshTokenEntity, tokenRequest);
            }
            return accessToken;
        }
    }

    @Override
    public AccessToken getToken(TokenRequest tokenRequest) {
        return tokenStore.getAccessToken(tokenRequest);
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(user.getId() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis());
        refreshToken.setUserId(user.getId());
        refreshToken.setCreated(System.currentTimeMillis());
        refreshToken.setCreatedBy(user.getUsername());
        return refreshTokenRepository.save(refreshToken);
    }

    private AccessToken createAccessToken(TokenRequest tokenRequest,RefreshToken refreshToken, User user) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(refreshToken.getUserId() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis());
        accessToken.setRefreshToken(refreshToken);
        accessToken.setExpiredTime(tokenRequest.getExpireIn() * 1000L);
        accessToken.setAuthorities(user.getAuthorities());
        accessToken.setActive(user.getActive());
        accessToken.setUsername(user.getUsername());
        accessToken.setUserId(user.getId());
        accessToken.setCreated(System.currentTimeMillis());
        accessToken.setCreatedBy(refreshToken.getCreatedBy());
        return accessToken;
    }

    private boolean isExpired(RefreshToken refreshToken) {
        return System.currentTimeMillis() > refreshToken.getExpiredTime();
    }
}
