package org.example.base.models.entity.token;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;

/**
 * Created by hungtd
 * Date: 06/10/2025
 * Time: 11:14 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Entity
@Table(name = "refresh_token")
@NoArgsConstructor@AllArgsConstructor@Getter@Setter
public class RefreshToken extends IdEntity {
    private Long userId;
    private String token;
    private Long expiredTime;

    public boolean isExpired(){
        return System.currentTimeMillis() > expiredTime;
    }
}
