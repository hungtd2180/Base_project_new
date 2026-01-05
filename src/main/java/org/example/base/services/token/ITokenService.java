package org.example.base.services.token;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.Token;
import org.example.base.models.entity.user.User;

/**
 * Created by hungtd
 * Date: 17/07/2025
 * Time: 1:52 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */

public interface ITokenService {
    Token createToken(User user, TokenRequest tokenRequest);
    Token refreshToken(String refreshToken, TokenRequest tokenRequest);
    Token getToken(TokenRequest tokenRequest);
}
