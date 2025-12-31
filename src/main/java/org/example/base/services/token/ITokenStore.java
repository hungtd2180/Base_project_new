package org.example.base.services.token;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.AccessToken;
import org.example.base.models.entity.token.RefreshToken;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 9:10 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */

public interface ITokenStore {
    void storeAccessToken(AccessToken token, TokenRequest tokenRequest);
    AccessToken readAccessToken(String token, Long userId);
    void removeAccessToken(AccessToken token);
    void storeRefreshToken(RefreshToken refreshToken, TokenRequest tokenRequest);
    RefreshToken readRefreshToken(String refreshToken);
    void removeRefreshToken(RefreshToken refreshToken);
    AccessToken removeAccessTokenUsingRefreshToken(RefreshToken refreshToken);
    AccessToken getAccessToken(TokenRequest tokenRequest);
}
