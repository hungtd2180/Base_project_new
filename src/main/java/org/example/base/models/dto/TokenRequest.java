package org.example.base.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hungtd
 * Date: 15/07/2025
 * Time: 8:52 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */

@NoArgsConstructor@AllArgsConstructor@Data
public class TokenRequest {
    private String refreshToken;
    private String username;
    private String password;
    private String grantType;
    private Integer expireIn;
    private Long userId;
}
