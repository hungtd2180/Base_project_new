package org.example.base.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter@Setter
public class UserPrincipal extends User {
    private Long userId;
    private String userAgent;

    public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId, String userAgent) {
        super(username, password, authorities);
        this.userId = userId;
        this.userAgent = userAgent;
    }
}
