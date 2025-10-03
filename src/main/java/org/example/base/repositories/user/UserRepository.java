package org.example.base.repositories.user;


import org.example.base.models.entity.user.User;
import org.example.base.repositories.CustomJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hungtd
 * Date: 11/18/2024
 * Time: 9:12 AM
 * for all issues, contact me: hungtd2180@gmail.com
 */

@Repository
public interface UserRepository extends CustomJpaRepository<User, Long> {
    User findFirstByUsername(String username);
}
