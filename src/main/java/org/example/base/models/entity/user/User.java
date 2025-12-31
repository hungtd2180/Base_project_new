package org.example.base.models.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;
import org.example.base.models.entity.privilege.Privilege;
import org.example.base.models.entity.role.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hungtd
 * Date: 11/18/2024
 * Time: 9:11 AM
 * for all issues, contact me: hungtd2180@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User extends IdEntity {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();
    @Transient
    @JsonProperty
    private Set<String> authorities;

    @JsonIgnore
    public void setEncryptedPassword(String password){
        this.password = passwordEncoder.encode(password);
    }

    public Boolean authenticate(String password){
        return passwordEncoder.matches(password, this.password);
    }

    public Set<String> getAuthorities() {
        if (this.authorities != null && !this.authorities.isEmpty()) {
            return this.authorities;
        }

        this.authorities = new HashSet<>();
        for (Role role : this.getRoles()) {
            this.authorities.add(role.getName());
            for (Privilege privilege : role.getPrivileges()) {
                this.authorities.add(privilege.getName());
            }
        }

        return authorities;
    }
}
