package org.example.base.services.token.accesstoken;

import org.example.base.constants.Constant;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.Token;
import org.example.base.models.entity.token.RefreshToken;
import org.example.base.models.entity.user.User;
import org.example.base.repositories.token.RefreshTokenRepository;
import org.example.base.services.cache.UserCacheService;
import org.example.base.services.token.ITokenService;
import org.example.base.services.token.ITokenStore;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "auth.type", havingValue = Constant.GrantTypeToken.ACCESS_TOKEN)
public class AccessTokenService implements ITokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private ITokenStore tokenStore;
    private UserCacheService userCacheService;
    @Value("${auth.access-token.token-expire}")
    private Long tokenExpired;
    @Value("${auth.access-token.refresh-token-expire}")
    private Long refreshTokenExpired;

    public AccessTokenService(RefreshTokenRepository refreshTokenRepository, ITokenStore tokenStore, UserCacheService userCacheService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenStore = tokenStore;
        this.userCacheService = userCacheService;
    }

    @Override
    public Token createToken(User user, TokenRequest tokenRequest) {
        if (ObjectUtils.isEmpty(tokenRequest.getUserId())) {
            tokenRequest.setUserId(user.getId());
        }
        Token existingToken = this.tokenStore.getToken(tokenRequest);
        if (!ObjectUtils.isEmpty(existingToken)) {
            if (existingToken.isExpired()){
                tokenStore.removeToken(existingToken);
                return createAccessToken(tokenRequest, createRefreshToken(user), user);
            }
            existingToken.setExpiredTime(System.currentTimeMillis() - existingToken.getExpiredTime());
            return existingToken;
        }
        return createAccessToken(tokenRequest, createRefreshToken(user), user);
    }

    @Override
    public Token refreshToken(String refreshToken, TokenRequest tokenRequest) {
        RefreshToken refreshTokenEntity = tokenStore.readRefreshToken(refreshToken);
        if (ObjectUtils.isEmpty(refreshTokenEntity)) {
            throw new InvalidParameterException("Invalid refresh token: " + refreshToken);
        }
        Token oldToken = tokenStore.removeAccessTokenUsingRefreshToken(refreshTokenEntity);
        if (this.isExpired(refreshTokenEntity)) {
            this.tokenStore.removeRefreshToken(refreshTokenEntity);
            throw new InvalidParameterException("Invalid refresh token (expired): " + refreshToken);
        } else {
            tokenStore.removeRefreshToken(refreshTokenEntity);
            if (!ObjectUtils.isEmpty(oldToken)) {
                tokenRequest.setUserId(refreshTokenEntity.getUserId());
            }
            User user = userCacheService.get(refreshTokenEntity.getUserId());
            if (ObjectUtils.isEmpty(user)) {
                throw new InvalidParameterException("Account isn't exists userId: " + refreshTokenEntity.getUserId());
            }
            refreshTokenEntity = createRefreshToken(user);
            tokenRequest.setUsername(user.getUsername());
            Token token = createAccessToken(tokenRequest, refreshTokenEntity, user);
            tokenStore.storeToken(token, tokenRequest);
            if (!ObjectUtils.isEmpty(token.getRefreshToken())) {
                tokenStore.storeRefreshToken(refreshTokenEntity, tokenRequest);
            }
            return token;
        }
    }

    @Override
    public Token getToken(TokenRequest tokenRequest) {
        return tokenStore.getToken(tokenRequest);
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(user.getId() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis());
        refreshToken.setUserId(user.getId());
        refreshToken.setExpiredTime(System.currentTimeMillis() + refreshTokenExpired);
        refreshToken.setCreated(System.currentTimeMillis());
        refreshToken.setCreatedBy(user.getUsername());
        return refreshTokenRepository.save(refreshToken);
    }

    private Token createAccessToken(TokenRequest tokenRequest, RefreshToken refreshToken, User user) {
        Token token = new Token();
        token.setToken(refreshToken.getUserId() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis());
        token.setRefreshToken(refreshToken);
        token.setExpiredTime(System.currentTimeMillis() + tokenExpired);
        token.setAuthorities(user.getAuthorities());
        token.setActive(user.getActive());
        token.setUsername(user.getUsername());
        token.setUserId(user.getId());
        token.setCreated(System.currentTimeMillis());
        token.setCreatedBy(refreshToken.getCreatedBy());
        return token;
    }

    private boolean isExpired(RefreshToken refreshToken) {
        return System.currentTimeMillis() > refreshToken.getExpiredTime();
    }
}
