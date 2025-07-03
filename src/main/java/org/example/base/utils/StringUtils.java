package org.example.base.utils;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public StringUtils() {
    }

    public static String repeat(String str, String separator, int count) {
        StringBuilder sb = new StringBuilder(str.length() + separator.length() * Math.max(count, 0));
        for (int n = 0; n < count; n++) {
            if (n > 0) {
                sb.append(separator);
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public static String toSnakeCase(String input) {
        if (input == null) {
            return "";
        } else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < input.length(); ++i) {
                char c = input.charAt(i);
                if (Character.isUpperCase(c)) {
                    if (i == 0) {
                        result.append(Character.toLowerCase(c));
                    } else {
                        result.append('_').append(Character.toLowerCase(c));
                    }
                } else {
                    result.append(c);
                }
            }
            return result.toString();
        }
    }
    public static Sort toSort(String input){
        List<Sort.Order> orders = new ArrayList<>();
        String[] sorts = input.split(",\\s*");
        for (String s : sorts) {
            String[] tmp = s.split(":");
            if (tmp[1].toUpperCase().contains("ASC")) {
                orders.add(new Sort.Order(Sort.Direction.ASC, tmp[0].trim()));
            } else {
                orders.add(new Sort.Order(Sort.Direction.DESC, tmp[0].trim()));
            }
        }
        return Sort.by(orders);
    }
}
