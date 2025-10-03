package org.example.base.repositories.session;

import org.example.base.models.entity.session.Session;
import org.example.base.repositories.CustomJpaRepository;

public interface SessionRepository extends CustomJpaRepository<Session, Long> {
    Session findFirstByToken(String token);
}
