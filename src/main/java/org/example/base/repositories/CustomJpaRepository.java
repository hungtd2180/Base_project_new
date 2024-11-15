package org.example.base.repositories;


import org.example.base.models.dto.IdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:13 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */
@NoRepositoryBean
public interface CustomJpaRepository <T extends IdEntity, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    T findFirstById(ID id);
}
