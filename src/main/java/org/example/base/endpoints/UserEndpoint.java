package org.example.base.endpoints;


import org.example.base.models.entity.User;
import org.example.base.services.CrudService;
import org.example.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:28 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */
@RestController
@RequestMapping("/api/user")
public class UserEndpoint extends CrudEndpoint<User , Long> {
    private UserService userService;

    @Autowired
    public UserEndpoint(UserService service) {
        super(service);
        this.service = userService = service;
        this.entityClass = User.class;
    }
}
