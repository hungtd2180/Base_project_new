package org.example.base.models.entity.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @JsonIgnore
    public void setEncryptedPassword(String password){
        this.password = passwordEncoder.encode(password);
    }
    public Boolean authenticate(String password){
        return passwordEncoder.matches(password, this.password);
    }

}
