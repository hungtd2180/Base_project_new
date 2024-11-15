package org.example.base.models.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.List;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:11 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorInfo {
    private URI type;
    private String defaultMessage;
    private String errorKey;
    private List<Long> idsFail;
    private String params;
}
