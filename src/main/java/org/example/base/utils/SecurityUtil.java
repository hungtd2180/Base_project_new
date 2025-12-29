package org.example.base.utils;

import org.example.base.models.dto.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:16 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class SecurityUtil {
    private SecurityUtil() {}

    public static String getCurrentUsernameLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String username = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserPrincipal) {
                UserPrincipal springSecurityUser = (UserPrincipal) authentication.getPrincipal();
                username = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                username = (String) authentication.getPrincipal();
            }
        }
        return username;
    }

    public static Long getCurrentUserId(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Long userId = null;

        if(authentication != null){
            if(authentication.getPrincipal() instanceof UserPrincipal){
                userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
            }
        }
        return  userId;
    }
}
