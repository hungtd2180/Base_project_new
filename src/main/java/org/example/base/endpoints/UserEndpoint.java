package org.example.base.endpoints;


import org.example.base.models.entity.user.User;
import org.example.base.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hungtd
 * Date: 11/18/2024
 * Time: 9:13 AM
 * for all issues, contact me: hungtd2180@gmail.com
 */

@RestController
@RequestMapping("/api/user")
public class UserEndpoint extends CrudEndpoint<User, Long> {
    private UserService userService;

    @Autowired
    public UserEndpoint(UserService service) {
        super(User.class, service);
        this.userService = service;
    }
}
