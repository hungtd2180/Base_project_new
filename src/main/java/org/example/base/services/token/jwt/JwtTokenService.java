package org.example.base.services.token.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.base.constants.Constant;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.entity.token.Token;
import org.example.base.models.entity.user.User;
import org.example.base.services.token.ITokenService;
import org.example.base.services.token.ITokenStore;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by hungtd
 * Date: 05/01/2026
 * Time: 9:40 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
@ConditionalOnProperty(name = "auth.type", havingValue = Constant.GrantTypeToken.JWT, matchIfMissing = true)
public class JwtTokenService implements ITokenService {
    private ITokenStore tokenStore;
    @Value("${auth.jwt.token-expire}")
    private Long tokenExpired;
    @Value("${auth.jwt.secret}")
    private String secretKey;
    public JwtTokenService(ITokenStore tokenStore) {
        this.tokenStore = tokenStore;
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
                return createAccessToken(tokenRequest, user);
            }
            existingToken.setExpiredTime(System.currentTimeMillis() - existingToken.getExpiredTime());
            return existingToken;
        }
        return createAccessToken(tokenRequest, user);
    }

    @Override
    public Token refreshToken(String refreshToken, TokenRequest tokenRequest) {
        return null;
    }

    @Override
    public Token getToken(TokenRequest tokenRequest) {
        return tokenStore.getToken(tokenRequest);
    }

    Token createAccessToken(TokenRequest tokenRequest, User user) {
        Token token = new Token();
        Date expire = new Date((new Date()).getTime() + tokenExpired);
        String jwtToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(expire)
                .claim(Constant.GrantTypeToken.JWT_USER_ID, user.getId())
                .claim(Constant.GrantTypeToken.JWT_SCOPE, user.getAuthorities())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        token.setToken(jwtToken);
        token.setUserId(user.getId());
        token.setUsername(user.getUsername());
        token.setExpiredTime(expire.getTime());
        token.setAuthorities(user.getAuthorities());
        token.setActive(user.getActive());
        token.setCreated(System.currentTimeMillis());
        token.setCreatedBy(user.getUsername());
        return token;
    }
}
