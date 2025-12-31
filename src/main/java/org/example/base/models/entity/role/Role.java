package org.example.base.models.entity.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;
import org.example.base.models.entity.privilege.Privilege;
import org.example.base.models.entity.user.User;

import java.util.Set;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 9:34 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */

@Entity
@Getter@Setter
public class Role extends IdEntity {
    private String name;
    private String description;
    private Integer type;

    @ManyToMany
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="privilege_id",referencedColumnName = "id")
    )
    private Set<Privilege> privileges;
    @Transient
    private Set<User> users;
}

