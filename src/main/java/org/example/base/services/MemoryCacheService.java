package org.example.base.services;

import org.example.base.models.dto.IdEntity;
import org.example.base.repositories.CustomJpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hungtd
 * Date: 30/12/2025
 * Time: 11:01 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */

public abstract class MemoryCacheService<T extends IdEntity, ID extends Serializable> {
    protected CustomJpaRepository<T, ID> repository;
    private final Map<ID, T> cache = new ConcurrentHashMap<>();

    public void initData(){
        List<T> datas = repository.findAll();
        datas.forEach(d -> put((ID) d.getId(), d));
    }

    public T get(ID id){
        T data = cache.get(id);
        if (data == null) {
            data = repository.findById(id).orElse(null);
            if (data != null) {
                put(id, data);
            }
        }
        return data;
    }

    public void put(ID id, T entity){
        cache.put(id, entity);
    }

    public boolean contains(ID id){
        return cache.containsKey(id);
    }

    public void remove(ID id){
        if(contains(id)){
            cache.remove(id);
        }
    }

    public void clear(){
        cache.clear();
    }
}
