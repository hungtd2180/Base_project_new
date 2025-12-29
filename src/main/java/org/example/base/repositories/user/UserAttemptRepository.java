package org.example.base.repositories.user;

import org.example.base.models.entity.user.UserAttempt;
import org.example.base.repositories.CustomJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by hungtd
 * Date: 15/07/2025
 * Time: 10:08 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@Repository
public interface UserAttemptRepository extends CustomJpaRepository<UserAttempt, Long> {
    UserAttempt findFirstByUsernameAndAction(String username, Integer action);
}
