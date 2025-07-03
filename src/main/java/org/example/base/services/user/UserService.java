package org.example.base.services.user;


import org.example.base.models.entity.user.User;
import org.example.base.repositories.user.UserRepository;
import org.example.base.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hungtd
 * Date: 11/18/2024
 * Time: 9:13 AM
 * for all issues, contact me: hungtd2180@gmail.com
 */

@Service
public class UserService extends CrudService<User, Long> {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository repository) {
        super(User.class);
        this.repository = userRepository = repository;
    }
}

