package org.example.base.endpoints;


import org.example.base.constants.Constant;
import org.example.base.models.dto.ApiOutput;
import org.example.base.models.dto.Event;
import org.example.base.models.dto.IdEntity;
import org.example.base.services.CrudService;
import org.example.base.utils.CommonUtil;
import org.example.base.utils.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:26 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public abstract class CrudEndpoint<T extends IdEntity, ID extends Serializable> {
    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(CrudEndpoint.class);

    protected Class<T> entityClass;
    protected CrudService<T , ID> service;

    protected String baseUrl;

    public CrudEndpoint(Class<T> entityClass, CrudService<T , ID> service) {
        this.entityClass = entityClass;
        this.service = service;
    }
    @GetMapping(value = "{id}")
    public ApiOutput get(@PathVariable ID id) {
        Event event = new Event();
        event.method = Constant.Method.GET_ONE;
        event.payload = id.toString();
        service.process(event);
        ApiOutput apiOutput = CommonUtil.packing(event);
        apiOutput.setData(ObjectMapperUtil.objectMapper(event.payload, entityClass));
        return apiOutput;
    }

    @GetMapping
    public ApiOutput getAll() {
        Event event = new Event();
        event.method = Constant.Method.GET_ALL;
        service.process(event);
        ApiOutput apiOutput = CommonUtil.packing(event);
        apiOutput.setData(ObjectMapperUtil.listMapper(event.payload, entityClass));
        return apiOutput;
    }

    @PostMapping
    public ApiOutput create(@RequestBody T entity) {
        Event event = new Event();
        event.method = Constant.Method.CREATE;
        event.payload = ObjectMapperUtil.toJsonString(entity);
        service.process(event);
        return CommonUtil.packing(event);
    }

    @PutMapping
    public ApiOutput update(@RequestBody T entity) {
        Event event = new Event();
        event.method = Constant.Method.UPDATE;
        event.payload = ObjectMapperUtil.toJsonString(entity);
        service.process(event);
        return CommonUtil.packing(event);
    }

    @DeleteMapping(value = "{id}")
    public ApiOutput delete(@PathVariable ID id) {
        Event event = new Event();
        event.method = Constant.Method.DELETE;
        event.payload = id.toString();
        service.process(event);
        return CommonUtil.packing(event);
    }

    @PutMapping(value = "/active/{id}")
    public ApiOutput active(@PathVariable ID id) {
        Event event = new Event();
        event.method = Constant.Method.ACTIVE;
        event.payload = id.toString();
        service.process(event);
        return CommonUtil.packing(event);
    }

    @PutMapping(value = "/deactive/{id}")
    public ApiOutput deactive(@PathVariable ID id) {
        Event event = new Event();
        event.method = Constant.Method.DEACTIVE;
        event.payload = id.toString();
        service.process(event);
        return CommonUtil.packing(event);
    }
}
