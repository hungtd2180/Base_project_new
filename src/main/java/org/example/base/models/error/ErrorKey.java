package org.example.base.models.error;


/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:11 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class ErrorKey {
    public static final class AuthErrorKey {
        public static final String INVALID_TOKEN = "error.userAndPermission.tokenInvalid";
        public static final String INVALID_USER = "error.userAndPermission.userInvalid";
    }

    public static final class CommonErrorKey {
        public static final String NOT_FOUND_ID = "error.common.notFoundId";
    }
}
