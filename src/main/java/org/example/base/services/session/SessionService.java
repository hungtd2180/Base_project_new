package org.example.base.services.session;

import org.example.base.constants.Constant;
import org.example.base.constants.ErrorKey;
import org.example.base.models.dto.TokenRequest;
import org.example.base.models.dto.TokenResponse;
import org.example.base.models.dto.Event;
import org.example.base.models.entity.session.Session;
import org.example.base.models.entity.user.User;
import org.example.base.repositories.session.SessionRepository;
import org.example.base.services.CrudService;
import org.example.base.services.user.UserAttemptService;
import org.example.base.services.user.UserService;
import org.example.base.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 14/07/2025
 * Time: 2:23 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Service
public class SessionService extends CrudService<Session, Long> {
    private SessionRepository sessionRepository;
    @Value("${tokenTime.accessToken}")
    private Long accessTokenExpired;
    @Value("${tokenTime.refreshToken}")
    private Long refreshTokenExpired;

    @Autowired
    private UserAttemptService userAttemptService;
    @Autowired
    private UserService userService;

    @Autowired
    public SessionService(SessionRepository repository) {
        super(Session.class);
        this.repository = sessionRepository = repository;
    }

    private Event processGetToken(Event event) {
        TokenRequest tokenRequest = (TokenRequest) event.payload;
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setExpiredIn(accessTokenExpired);
        if (Constant.GrantTypeToken.PASSWORD.equals(tokenRequest.getGrantType())) {
            if (ObjectUtils.isEmpty(tokenRequest.getUsername())) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.ACCOUNT_INVALID);
            }
            Event checkAttempt = userAttemptService.checkUserAttemptGetAccessToken(tokenRequest.getUsername());
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
            if (ObjectUtils.isEmpty(user.getActive()) || !user.getActive().equals(Constant.EntityStatus.ACTIVE)) {
                return handleErrorMessage(event, ErrorKey.AuthErrorKey.ACCOUNT_INACTIVE);
            }


        }
        return event;

    }


}
