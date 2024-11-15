package org.example.base.converters;


/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:06 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public interface EntityConvert <E, D>{
    E toEntity(D d);
    D toDto(E e);
}
