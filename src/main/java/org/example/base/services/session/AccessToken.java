package org.example.base.services.session;

import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.session.Session;
import org.example.base.models.entity.user.User;

/**
 * Created by hungtd
 * Date: 17/07/2025
 * Time: 2:05 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */

public class AccessToken implements TokenService{
    @Override
    public Session createToken(TokenRequest tokenRequest, User user) {

        return null;
    }

    @Override
    public Session refreshToken(String refreshToken, TokenRequest tokenRequest) {
        return null;
    }

    @Override
    public Session getToken(TokenRequest tokenRequest) {
        return null;
    }
}
