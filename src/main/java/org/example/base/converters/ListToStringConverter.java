package org.example.base.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:08 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class ListToStringConverter <T> implements AttributeConverter<List<T>, String> {

    private static final Logger log = LoggerFactory.getLogger(ListToStringConverter.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<T> stringObject) {
        try {
            return mapper.writeValueAsString(stringObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<T> convertToEntityAttribute(String s) {
        List<T> res = new LinkedList<>();
        if (!s.isEmpty()) {
            try {
                TypeReference<List<T>> typeRef = new TypeReference<List<T>>() {};
                res = mapper.readValue(s, typeRef);
            } catch (IOException e) {
                log.error("Error converting attribute", e);
            }
        }
        return res;
    }
}
