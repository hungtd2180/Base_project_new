package org.example.base.services;


import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.persistence.EntityNotFoundException;
import org.example.base.constants.Constant;
import org.example.base.models.dto.Event;
import org.example.base.models.dto.IdEntity;
import org.example.base.models.dto.PageInfo;
import org.example.base.models.dto.SearchInfo;
import org.example.base.models.error.ErrorInfo;
import org.example.base.constants.ErrorKey;
import org.example.base.repositories.CustomJpaRepository;
import org.example.base.rsql.sql.CustomRsqlVisitor;
import org.example.base.utils.ObjectUtils;
import org.example.base.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:22 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */
@SuppressWarnings({"Duplicates", "unchecked"})
public abstract class CrudService<T extends IdEntity, ID extends Serializable> {
    private static final Logger logger = LoggerFactory.getLogger(CrudService.class);
    protected CustomJpaRepository<T, ID> repository;
    protected final Class<T> typeParameterClass;

    public CrudService(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }
    public Event process(Event event) {
        event.errorCode = Constant.ResultStatus.SUCCESS;
        switch (event.method) {
            case Constant.Methods.CREATE:
                event = processCreate(event);
                break;
            case Constant.Methods.UPDATE:
                event = processUpdate(event);
                break;
            case Constant.Methods.UPDATE_PARTIAL:
                break;
            case Constant.Methods.DELETE:
                event = processDelete(event);
                break;
            case Constant.Methods.GET_ONE:
                event = processGet(event);
                break;
            case Constant.Methods.GET_ALL:
                event = processGetAll(event);
                break;
            case Constant.Methods.SEARCH:
                event = processSearch(event);
                break;
            case Constant.Methods.ACTIVE:
                event = processActive(event);
                break;
            case Constant.Methods.DEACTIVE:
                event = processDeactive(event);
                break;
            default:
                event.errorCode = Constant.ResultStatus.ERROR;
        }
        return event;
    }

    public Event processCreate(Event event) {
        T entity = (T) event.payload;
        event.payload = create(entity);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }
    /**Cập nhật toàn bộ object*/
    public Event processUpdate(Event event) {
        T entity = (T) event.payload;
        if (entity.getId() == null || get((ID) entity.getId()) == null ) {
            return handleErrorMessage(event, ErrorKey.CRUDErrorKey.NOT_FOUND_ID);
        }
        event.payload = update((ID) entity.getId(), entity);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }
    /**Chỉ cập nhật một số thông tin của object*/
    public Event processUpdatePartial(Event event) {
        T updateEntity = (T) event.payload;
        if (ObjectUtils.isEmpty(updateEntity.getId()) || ObjectUtils.isEmpty(get((ID) updateEntity.getId()))) {
            return handleErrorMessage(event, ErrorKey.CRUDErrorKey.NOT_FOUND_ID);
        }
        event.payload = updatePartial((ID) updateEntity.getId(), updateEntity);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processDelete(Event event) {
        deleteById((ID) event.payload);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processGet(Event event) {
        event.payload = get((ID) event.payload);
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processGetAll(Event event) {
        event.payload = getAll();
        event.errorCode = Constant.ResultStatus.SUCCESS;
        return event;
    }

    public Event processSearch(Event event) {
        SearchInfo searchInfo = (SearchInfo) event.payload;
        String orders = searchInfo.getOrders();
        Pageable pageable;
        if (ObjectUtils.isEmpty(orders)) {
            pageable = PageRequest.of(searchInfo.getPageNumber(), searchInfo.getPageSize());
        } else {
            pageable = PageRequest.of(searchInfo.getPageNumber(), searchInfo.getPageSize(), StringUtils.toSort(orders));
        }
        try {
            Page<T> page = search(searchInfo.getQuery(), pageable);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotalCount(page.getTotalElements());
            pageInfo.setData(page.getContent());
            event.payload = pageInfo;
            event.errorCode = Constant.ResultStatus.SUCCESS;
            return event;
        } catch (Exception e) {
            return handleErrorMessage(event, ErrorKey.CRUDErrorKey.SEARCH_FAIL);
        }
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
        logger.info("Create entity class {}", typeParameterClass.getSimpleName());
        beforeCreate(entity);
        repository.save(entity);
        afterCreate(entity);
        return entity;
    }

    public T update(ID id, T entity) {
        logger.info("Update entity class {} with id {}", typeParameterClass.getSimpleName(), id);
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

    public T updatePartial(ID id, T entity) {
        logger.info("Update partial entity class {} with id {}", typeParameterClass.getSimpleName(), id);
        mergePartialEntity(entity);
        return update(id, entity);
    }

    public void delete(T entity) {
        beforeDelete(entity);
        repository.delete(entity);
        afterDelete(entity);
    }

    public void deleteById(ID id) {
        logger.info("Delete entity class {} with id {}", typeParameterClass.getSimpleName(),id);
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

    public Page<T> search(String query, Pageable pageable) throws Exception {
        return searchByQuery(query, pageable);
    }

    public Page<T> searchByQuery(String query, Pageable pageable) throws Exception {
        if (ObjectUtils.isEmpty(query)) {
            return repository.findAll(pageable);
        }
        try {
            Node rootNode = new RSQLParser().parse(query);
            Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
            return repository.findAll(spec, pageable);
        } catch (Exception e) {
            logger.error("Search fail: {}", query);
            throw e;
        }
    }

    protected void mergePartialEntity(T entity) {
        T entityFromDb = this.findFirstById((ID) entity.getId());
        T updateEntity = (T) ObjectUtils.mergePartialEntity(entity, entityFromDb);
        BeanUtils.copyProperties(updateEntity, entity);
    }
    public Event handleErrorMessage(Event event, String key) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setErrorKey(key);
        event.errorCode = Constant.ResultStatus.ERROR;
        event.payload = errorInfo;
        return event;
    }
}

