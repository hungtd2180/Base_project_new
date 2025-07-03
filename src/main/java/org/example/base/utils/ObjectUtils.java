package org.example.base.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ObjectUtils {
    public ObjectUtils() {
    }

    public static Object[] wrapToArray(Object obj) {
        if (obj == null) {
            return new Object[0];
        } else if (obj instanceof Object[]) {
            return (Object[]) ((Object[]) obj);
        } else {
            return obj.getClass().isArray() ? org.springframework.util.ObjectUtils.toObjectArray(obj) : new Object[]{obj};
        }
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof String) {
            return "".equals(((String)object).trim());
        } else if (object instanceof Collection) {
            return ((Collection)object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map)object).isEmpty();
        } else if (object instanceof StringBuilder) {
            return ((StringBuilder)object).length() == 0;
        } else if (object instanceof StringBuffer) {
            return ((StringBuffer)object).length() == 0;
        } else {
            return false;
        }
    }

    public static Object mergePartialEntity(Object src, Object dest) {
        BeanUtils.copyProperties(src, dest, getNullPropertyNames(src));
        return dest;
    }

    public static String[] getNullPropertyNames(Object source) {
        List<String> nullValuePropertyNames = new ArrayList<>();
        for (Field f : FieldUtils.getAllFields(source.getClass())) {
            f.setAccessible(true);
            try {
                if (f.get(source) instanceof Map || org.springframework.util.ObjectUtils.isEmpty(f.get(source))) {
                    nullValuePropertyNames.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return nullValuePropertyNames.toArray(new String[0]);
    }
}
