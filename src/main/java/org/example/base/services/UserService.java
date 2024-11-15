package org.example.base.services;

import org.example.base.models.entity.User;
import org.example.base.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends CrudService <User, Long>{
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository repository) {
        super(User.class);
        this.repository = userRepository = repository;
    }
}
