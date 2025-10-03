package org.example.base.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hungtd
 * Date: 15/07/2025
 * Time: 9:00 AM
 * For all issues, contact me: hungtd2180@gmail.com
 */
@NoArgsConstructor@AllArgsConstructor@Data
public class TokenResponse {
    private String token;
    private String refreshToken;
    private Long expiredIn;
}
