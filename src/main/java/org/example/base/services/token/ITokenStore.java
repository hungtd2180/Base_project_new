package org.example.base.services.token;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.Token;
import org.example.base.models.entity.token.RefreshToken;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 9:10 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */

public interface ITokenStore {
    void storeToken(Token token, TokenRequest tokenRequest);
    Token readToken(String token, Long userId);
    void removeToken(Token token);
    void storeRefreshToken(RefreshToken refreshToken, TokenRequest tokenRequest);
    RefreshToken readRefreshToken(String refreshToken);
    void removeRefreshToken(RefreshToken refreshToken);
    Token removeAccessTokenUsingRefreshToken(RefreshToken refreshToken);
    Token getToken(TokenRequest tokenRequest);
}
