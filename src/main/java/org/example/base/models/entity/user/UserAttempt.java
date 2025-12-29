package org.example.base.models.entity.user;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;

/**
 * Created by hungtd
 * Date: 15/07/2025
 * Time: 9:58 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Entity
@Getter@Setter
public class UserAttempt extends IdEntity {
    private String username;
    private Integer attempts;
    private Long time;
    private Integer action; // 0: sendOTP, 1: enterOTP, 2: login, 3: register, 4: forgotPassword

    public UserAttempt() {}

    public UserAttempt(String username, Integer attempts){
        this.username = username;
        this.attempts = attempts;
        this.time = System.currentTimeMillis();
    }

}
