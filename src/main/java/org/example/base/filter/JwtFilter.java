package org.example.base.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.base.configuration.SpringContext;
import org.example.base.constants.Constant;
import org.example.base.models.dto.UserPrincipal;
import org.example.base.models.entity.token.AccessToken;
import org.example.base.repositories.token.AccessTokenRepository;
import org.example.base.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JwtFilter extends OncePerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private AccessTokenRepository accessTokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("User-Agent: {}", request.getHeader("User-Agent"));
        logger.info("CORS: {}", request.getHeader("Origin"));
        String uri = request.getRequestURI();
        logger.info("CORS-Origin: {}, {}, {}, {}", uri, request.getHeader("Origin"), request.getServerName(), request.getServerPort());
        String authorization = request.getHeader(Constant.Header.AUTH_HEADER_STRING);
        if (!ObjectUtils.isEmpty(authorization) && authorization.startsWith(Constant.Header.AUTH_TOKEN_PREFIX)
                && !request.getMethod().equalsIgnoreCase("OPTIONS") && !authorization.startsWith(Constant.Header.AUTH_TOKEN_PREFIX + "undefined")) {
            String token = authorization.substring(Constant.Header.AUTH_TOKEN_PREFIX.length());
            if (ObjectUtils.isEmpty(accessTokenRepository)) {
                accessTokenRepository = SpringContext.getBean(AccessTokenRepository.class);
            }
            AccessToken accessToken = accessTokenRepository.findFirstByToken(token);
            if (!ObjectUtils.isEmpty(accessToken)) {
                if (!ObjectUtils.isEmpty(accessToken.getExpiredTime()) && accessToken.getExpiredTime() - System.currentTimeMillis() < 0) {
                    logger.info("Invalid JWT expired");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                if (!accessToken.getActive().equals(Constant.EntityStatus.ACTIVE)) {
                    logger.info("User deactive");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                initSecurityInfo(accessToken, request.getHeader("User-Agent"));
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private void initSecurityInfo(AccessToken token, String userAgent){
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (token.getAuthorities() != null) {
            for (String scope : token.getAuthorities()) {
                authorities.add(new SimpleGrantedAuthority(scope));
            }
        }
        UserPrincipal principal = new UserPrincipal(token.getUsername(), "", authorities, token.getUserId(), userAgent);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, token, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
