package org.example.base.services.session;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.session.Session;
import org.example.base.models.entity.user.User;

/**
 * Created by hungtd
 * Date: 17/07/2025
 * Time: 1:52 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */

public interface TokenService {
    Session createToken(TokenRequest tokenRequest, User user);
    Session refreshToken(String refreshToken, TokenRequest tokenRequest);
    Session getToken(TokenRequest tokenRequest);
}
