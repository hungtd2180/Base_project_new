package org.example.base.models.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:09 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdEntity extends AbstractEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    public IdEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
