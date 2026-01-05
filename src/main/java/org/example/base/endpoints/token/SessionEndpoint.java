package org.example.base.endpoints.token;

import org.example.base.constants.ErrorKey;
import org.example.base.endpoints.CrudEndpoint;
import org.example.base.models.dto.ApiOutput;
import org.example.base.models.entity.token.Token;
import org.example.base.services.token.SessionService;
import org.example.base.utils.CommonUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hungtd
 * Date: 14/07/2025
 * Time: 2:28 PM
 * For all issues, contact me: hungtd2180@gmail.com
 */

@RestController
@RequestMapping("/api/sessions")
public class SessionEndpoint extends CrudEndpoint<Token, Long> {
    private SessionService sessionService;

    public SessionEndpoint(SessionService service) {
        super(service);
        this.sessionService = service;
    }

    @GetMapping(value = "{id}")
    public ApiOutput get(@PathVariable(value = "id") Long id) {
        return CommonUtils.apiException(ErrorKey.ApiErrorKey.NOT_EXIST);
    }

    @GetMapping
    public ApiOutput getAll() {
        return CommonUtils.apiException(ErrorKey.ApiErrorKey.NOT_EXIST);
    }

    @PostMapping
    public ApiOutput create(@RequestBody Token entity) {
        return CommonUtils.apiException(ErrorKey.ApiErrorKey.NOT_EXIST);
    }

    @PutMapping
    public ApiOutput update(@RequestBody Token entity) {
        return CommonUtils.apiException(ErrorKey.ApiErrorKey.NOT_EXIST);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_SYSTEM_ADMIN')")
    public ApiOutput delete(@PathVariable(value = "id") Long id) {
        return this.delete(id);
    }

    @PutMapping(value = "/active/{id}")
    public ApiOutput active(@PathVariable(value = "id") Long id) {
        return CommonUtils.apiException(ErrorKey.ApiErrorKey.NOT_EXIST);
    }

    @PutMapping(value = "/deactive/{id}")
    public ApiOutput deactive(@PathVariable(value = "id") Long id) {
        return CommonUtils.apiException(ErrorKey.ApiErrorKey.NOT_EXIST);
    }

}
