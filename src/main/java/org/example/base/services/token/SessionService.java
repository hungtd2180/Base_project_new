package org.example.base.services.token;

import org.example.base.constants.Constant;
import org.example.base.constants.ErrorKey;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.dto.TokenResponse;
import org.example.base.models.dto.Event;
import org.example.base.models.entity.token.AccessToken;
import org.example.base.models.entity.user.User;
import org.example.base.repositories.token.AccessTokenRepository;
import org.example.base.services.CrudService;
import org.example.base.services.user.UserAttemptService;
import org.example.base.services.user.UserService;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

/**
 * Created by hungtd
 * Date: 14/07/2025
 * Time: 2:23 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class SessionService extends CrudService<AccessToken, Long> {
    private AccessTokenRepository accessTokenRepository;
    @Value("${tokenTime.accessToken}")
    private Long accessTokenExpired;
    @Value("${tokenTime.refreshToken}")
    private Long refreshTokenExpired;

    private UserAttemptService userAttemptService;
    private UserService userService;
    private ITokenService tokenService;
    @Autowired
    public void setUserAttemptService(UserAttemptService userAttemptService) {
        this.userAttemptService = userAttemptService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setTokenService(ITokenService ITokenService) {
        this.tokenService = ITokenService;
    }

    public SessionService(AccessTokenRepository repository) {
        super(AccessToken.class);
        this.repository = accessTokenRepository = repository;
    }

    @Override
    public Event processCustomEvent(Event event) {
        switch (event.method) {
            case Constant.Methods.GET_TOKEN:
                return processGetToken(event);
            default:
                return super.processCustomEvent(event);
        }
    }

    private Event processGetToken(Event event) {
        TokenRequest tokenRequest = (TokenRequest) event.payload;
        TokenResponse tokenResponse = new TokenResponse();
        if (Constant.GrantTypeToken.PASSWORD.equals(tokenRequest.getGrantType())) {
            if (ObjectUtils.isEmpty(tokenRequest.getUsername())) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.ACCOUNT_INVALID);
            }
            Event checkAttempt = userAttemptService.checkUserAttempt(tokenRequest.getUsername(), Constant.UserAttempt.LOGIN);
            if (checkAttempt.errorCode.equals(Constant.ResultStatus.ERROR)) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.LOGIN_LIMIT);
            }

            User user = userService.getUserByUsername(tokenRequest.getUsername());
            if (ObjectUtils.isEmpty(user)) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.ACCOUNT_INVALID);
            }
            if (ObjectUtils.isEmpty(tokenRequest.getPassword()) || !user.authenticate(tokenRequest.getPassword())) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.ACCOUNT_INVALID);
            }
            if (ObjectUtils.isEmpty(user.getActive()) || user.getActive().equals(Constant.EntityStatus.INACTIVE)) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.ACCOUNT_INACTIVE);
            }
            if (user.getActive().equals(Constant.EntityStatus.DELETED)) {
                user.setActive(Constant.EntityStatus.ACTIVE);
            }
            AccessToken accessToken = tokenService.createToken(user, tokenRequest);
            tokenResponse.setToken(accessToken.getToken());
            if (!ObjectUtils.isEmpty(accessToken.getRefreshToken())) {
                tokenResponse.setRefreshToken(accessToken.getRefreshToken().getToken());
            }
            tokenResponse.setExpiredIn(accessToken.getExpiredTime());
            event.payload = tokenResponse;
        } else {
            try {
                String refreshToken = tokenRequest.getRefreshToken();
                AccessToken newToken = tokenService.refreshToken(refreshToken, tokenRequest);
                tokenResponse.setToken(newToken.getToken());
                tokenResponse.setRefreshToken(newToken.getRefreshToken().getToken());
                tokenResponse.setExpiredIn(newToken.getExpiredTime());
                event.payload = tokenResponse;
            } catch (InvalidParameterException e) {
                if (e.getMessage().contains("expired")){
                    return handleErrorMessage(event, ErrorKey.TokenErrorKey.REFRESH_TOKEN_EXPIRED);
                } else {
                    return handleErrorMessage(event, ErrorKey.TokenErrorKey.REFRESH_TOKEN_INVALID);
                }
            }
        }

        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;

    }
}
