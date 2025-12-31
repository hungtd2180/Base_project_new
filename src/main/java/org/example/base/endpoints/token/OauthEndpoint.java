package org.example.base.endpoints.token;

import org.example.base.constants.Constant;
import org.example.base.models.dto.ApiOutput;
import org.example.base.models.dto.Event;
import org.example.base.models.dto.TokenRequest;
import org.example.base.services.token.SessionService;
import org.example.base.utils.CommonUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hungtd
 * Date: 03/10/2025
 * Time: 10:46 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@RestController
@RequestMapping("/api/auth")
public class OauthEndpoint {
    private SessionService sessionService;
    public OauthEndpoint(SessionService sessionService) {
        this.sessionService = sessionService;
    }
    @PostMapping("/token")
    public ApiOutput getToken(TokenRequest tokenRequest){
        Event event = new Event(Constant.Methods.GET_TOKEN, tokenRequest);
        sessionService.process(event);
        return CommonUtils.packing(event);
    }

}
