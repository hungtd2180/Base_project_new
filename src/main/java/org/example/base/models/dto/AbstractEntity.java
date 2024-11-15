package org.example.base.models.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.MappedSuperclass;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:09 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

@MappedSuperclass
@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class AbstractEntity implements Serializable {
    private Long created;
    @DiffIgnore
    private Long updated;
    private String createdBy;
    @DiffIgnore
    private String updatedBy;
    private Integer active;

    public AbstractEntity() {
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getCreated() {
        return this.created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return this.updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Integer getActive() {
        return this.active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}

