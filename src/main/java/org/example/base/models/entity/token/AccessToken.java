package org.example.base.models.entity.token;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.base.converters.SetStringConverter;
import org.example.base.models.dto.IdEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "access_token")
@NoArgsConstructor@Getter@Setter
public class AccessToken extends IdEntity {
    private Long userId;
    private String token;
    private Long expiredTime;
    private String username;
    @Convert(converter = SetStringConverter.class)
    private Set<String> authorities = new HashSet<>();
    private String refreshTokenId;
}
