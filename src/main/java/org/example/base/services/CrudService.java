package org.example.base.services;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.base.constants.Constant;
import org.example.base.models.dto.Event;
import org.example.base.models.dto.IdEntity;
import org.example.base.models.error.ErrorInfo;
import org.example.base.models.error.ErrorKey;
import org.example.base.repositories.CustomJpaRepository;
import org.example.base.utils.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:22 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

@SuppressWarnings({"Duplicates", "unchecked"})
@Transactional
public abstract class CrudService<T extends IdEntity, ID extends Serializable> {
    private static final Logger logger = LoggerFactory.getLogger(CrudService.class);
    protected CustomJpaRepository<T, ID> repository;
    protected final Class<T> typeParameterClass;

    @PersistenceContext
    protected EntityManager entityManager;
    protected MessageSource messageSource;


    public CrudService(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public Event process(Event event) {
        event.errorCode = Constant.ResultStatus.SUCCESS;
        switch (event.method) {
            case Constant.Method.CREATE:
                event = processCreate(event);
                break;
            case Constant.Method.UPDATE:
                event = processUpdate(event);
                break;
            case Constant.Method.DELETE:
                event = processDelete(event);
                break;
            case Constant.Method.GET_ONE:
                event = processGet(event);
                break;
            case Constant.Method.GET_ALL:
                event = processGetAll(event);
                break;
            case Constant.Method.ACTIVE:
                event = processActive(event);
                break;
            case Constant.Method.DEACTIVE:
                event = processDeactive(event);
                break;
            default:
                event.errorCode = Constant.ResultStatus.ERROR;
        }
        return event;
    }

    public Event processCreate(Event event) {
        T entity = ObjectMapperUtil.objectMapper(event.payload, typeParameterClass);
        event.payload = ObjectMapperUtil.toJsonString(create(entity));
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processUpdate(Event event) {
        T entity = ObjectMapperUtil.objectMapper(event.payload, typeParameterClass);
        if (entity.getId() == null || get((ID) entity.getId()) == null ) {
            return handleErrorMessage(event, ErrorKey.CommonErrorKey.NOT_FOUND_ID);
        }
        event.payload = ObjectMapperUtil.toJsonString(update((ID) entity.getId(), entity));
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processDelete(Event event) {
        deleteById((ID) event.payload);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processGet(Event event) {
        T data = get((ID) event.payload);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        event.payload = ObjectMapperUtil.toJsonString(data);
        return event;
    }

    public Event processGetAll(Event event) {
        List<T> data = getAll();
        event.errorCode = Constant.ResultStatus.SUCCESS;
        event.payload = ObjectMapperUtil.toJsonString(data);
        return event;
    }

    public Event processActive(Event event) {
        active((ID) event.payload);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processDeactive(Event event) {
        deactive((ID) event.payload);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public T get(ID id) {
        return repository.findById(id).orElse(null);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T create(T entity) {
        beforeCreate(entity);
        repository.save(entity);
        afterCreate(entity);
        return entity;
    }

    public T update(ID id, T entity) {
        beforeUpdate(entity);
        T old = get(id);
        if (old == null) {
            throw new EntityNotFoundException("No entity with id: " + id);
        }
        if (entity.getCreated() == null) {
            entity.setCreated(old.getCreated());
        }
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(old.getCreatedBy());
        }
        repository.save(entity);
        afterUpdate(old, entity);
        return entity;
    }

    public void delete(T entity) {
        beforeDelete(entity);
        repository.delete(entity);
        afterDelete(entity);
    }

    public void deleteById(ID id) {
        T entity = get(id);
        delete(entity);
    }

    public void active(ID id) {
        T entity = get(id);
        entity.setActive(Constant.EntityStatus.ACTIVE);
        repository.save(entity);
    }

    public void deactive(ID id) {
        T entity = get(id);
        entity.setActive(Constant.EntityStatus.DEACTIVE);
        repository.save(entity);
    }
    protected void beforeCreate(T entity) {
        entity.setCreated(System.currentTimeMillis());
//        if (entity.getCreatedBy() == null) {
//            String currentUsername = SecurityUtil.getCurrentUserLogin();
//            entity.setCreatedBy(currentUsername);
//        }
        if (entity.getActive() == null) {
            entity.setActive(Constant.EntityStatus.ACTIVE);
        }
    }

    protected void afterCreate(T entity) {
        // Làm gì đó thì override
    }

    protected void beforeUpdate(T entity) {
        entity.setUpdated(System.currentTimeMillis());
//        entity.setUpdatedBy(SecurityUtil.getCurrentUserLogin());
        if (entity.getActive() == null) {
            entity.setActive(Constant.EntityStatus.ACTIVE);
        }
    }
    protected void afterUpdate(T old, T entity) {
        // Làm gì đó thì override
    }

    protected void beforeDelete(T entity) {
        // Làm gì đó thì override
    }

    protected void afterDelete(T entity) {
        // Làm gì đó thì override
    }

    public T findFirstById(ID id) {
        return repository.findFirstById(id);
    }

    public Event handleErrorMessage(Event event, String key) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorKey(key);
        event.errorCode = Constant.ResultStatus.ERROR;
        event.payload = ObjectMapperUtil.toJsonString(errorInfo);
        return event;
    }
}

