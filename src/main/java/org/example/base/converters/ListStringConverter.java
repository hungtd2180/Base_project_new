package org.example.base.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:07 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class ListStringConverter implements AttributeConverter<List<String>, String> {
    private static final Logger log = LoggerFactory.getLogger(ListStringConverter.class);
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List<String> stringObjectMapper) {
        try {
            return mapper.writeValueAsString(stringObjectMapper);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        List<String> res = new LinkedList<>();
        if (!StringUtils.isEmpty(s)) {
            try {
                TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
                res = mapper.readValue(s, typeRef);
            } catch (IOException e){
                log.error("Error while converting attribute", e);
            }
        }
        return res;
    }
}

