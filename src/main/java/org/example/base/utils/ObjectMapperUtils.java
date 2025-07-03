package org.example.base.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:15 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class ObjectMapperUtils {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private ObjectMapperUtils(){}

    public static <T> T objectMapper(String json, Class<?> type){
        try{
            Object o =  mapper.readValue(json, type);
            return (T) o;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            return null;
        }
    }

    public static <T> List<T> listMapper (String json, Class<?> type){
        try{
            List<Object> o = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
            return (List<T>) o;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public static <T> String toJsonString(T obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

}
