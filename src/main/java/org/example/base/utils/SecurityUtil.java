//package org.example.base.utils;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//
///**
// * Created by hungtd
// * Date: 15/11/2024
// * Time: 2:16 CH
// * for all issues, contact me: hungtd2180@gmail.com
// */
//
//public class SecurityUtil {
//    /*Phần này chưa đầy đủ do chưa học hoàn chỉnh phần security */
//
//    private SecurityUtil() {}
//
//    public static String getCurrentUserLogin() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        String username = null;
//        if (authentication != null) {
//            if (authentication.getPrincipal() instanceof UserDetails) {
//                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
//                username = springSecurityUser.getUsername();
//            } else if (authentication.getPrincipal() instanceof String) {
//                username = (String) authentication.getPrincipal();
//            }
//        }
//        return username;
//    }
//}
