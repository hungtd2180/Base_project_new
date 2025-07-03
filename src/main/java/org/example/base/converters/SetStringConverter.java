package org.example.base.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SetStringConverter implements AttributeConverter<Set<String>, String> {
    private static final Logger logger = LoggerFactory.getLogger(SetStringConverter.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<String> strings) {
        try {
            return this.mapper.writeValueAsString(strings);
        } catch (JsonProcessingException e) {
            logger.error("Error when converting to database column ", e);
            return "";
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String s) {
        Set<String> res = new HashSet<>();
        if(!StringUtils.isEmpty(s)) {
            try {
                TypeReference<Set<String>> typeRef = new TypeReference<Set<String>>() {
                };
                res = mapper.readValue(s,typeRef);
            } catch (IOException e) {
                logger.error("Error when converting to entity attribute",e);
            }
        }
        return res;
    }
}
