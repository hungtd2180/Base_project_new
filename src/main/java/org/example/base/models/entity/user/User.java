package org.example.base.models.entity.user;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;

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
    private String username;

}
