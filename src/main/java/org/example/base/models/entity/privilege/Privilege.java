package org.example.base.models.entity.privilege;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.base.models.dto.IdEntity;

/**
 * Created by hungtd
 * Date: 31/12/2025
 * Time: 9:35 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */

@Entity
@Getter@Setter
public class Privilege extends IdEntity {
    private String name;
    private String description;
    private String displayName;
    private String categoryName;

}
