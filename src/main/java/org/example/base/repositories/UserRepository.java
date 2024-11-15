package org.example.base.repositories;

import org.example.base.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CustomJpaRepository<User, Long> {
}
