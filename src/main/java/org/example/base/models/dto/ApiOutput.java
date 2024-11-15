package org.example.base.models.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:10 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiOutput {
    private Integer status;
    private String message;
    private Object data;
}
