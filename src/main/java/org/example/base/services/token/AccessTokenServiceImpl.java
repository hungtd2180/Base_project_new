package org.example.base.services.token;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.AccessToken;
import org.example.base.models.entity.token.RefreshToken;
import org.example.base.models.entity.user.User;
import org.example.base.repositories.token.AccessTokenRepository;
import org.example.base.repositories.token.RefreshTokenRepository;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by hungtd
 * Date: 17/07/2025
 * Time: 2:05 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class AccessTokenServiceImpl implements TokenService{
    private AccessTokenRepository accessTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    public void setAccessTokenRepository(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }
    @Autowired
    public void setRefreshTokenRepository(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public AccessToken createToken(User user, String ipAddress) {
        AccessToken existAccessToken = accessTokenRepository.findFirstByUserIdAndIpAddress(user.getId(), ipAddress);
        if (!ObjectUtils.isEmpty(existAccessToken)) {
            if (!existAccessToken.isExpired()) {
                existAccessToken.setCreated(System.currentTimeMillis());
                accessTokenRepository.save(existAccessToken);
                return existAccessToken;
            } else {
                if (!ObjectUtils.isEmpty(existAccessToken.getRefreshTokenId())) {
                    refreshTokenRepository.deleteById(existAccessToken.getRefreshTokenId());
                }
                accessTokenRepository.deleteById(existAccessToken.getId());
            }
        }
        RefreshToken refreshToken = createRefreshToken(user);

        return null;
    }

    @Override
    public AccessToken refreshToken(String refreshToken, TokenRequest tokenRequest) {
        return null;
    }

    @Override
    public AccessToken getToken(TokenRequest tokenRequest) {
        return null;
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(user.getId() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis());
        refreshToken.setUserId(user.getId());
        refreshToken.setCreated(System.currentTimeMillis());
        refreshToken.setCreatedBy(user.getUsername());
        return refreshTokenRepository.save(refreshToken);
    }

    private AccessToken createAccessToken(RefreshToken refreshToken, User user) {
        AccessToken accessToken = new AccessToken();
        accessToken.setToken(refreshToken.getUserId() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis());
        accessToken.setRefreshTokenId(refreshToken.getId());
        accessToken.setCreated(System.currentTimeMillis());
        accessToken.setCreatedBy(refreshToken.getCreatedBy());
        return accessTokenRepository.save(accessToken);
    }
}
