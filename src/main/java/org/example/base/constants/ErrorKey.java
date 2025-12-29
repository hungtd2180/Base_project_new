package org.example.base.constants;


/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:11 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class ErrorKey {
    public static final class AuthErrorKey {
        public static final String TOKEN_INVALID = "error.userAndPermission.tokenInvalid";
        public static final String ACCOUNT_INVALID = "error.userAndPermission.accountInvalid";
        public static final String LOGIN_LIMIT = "error.userAndPermission.loginLimit";
        public static final String ACCOUNT_INACTIVE = "error.userAndPermission.accountInactive";
    }

    public static final class CRUDErrorKey {
        public static final String NOT_FOUND_ID = "error.crud.notFoundId";
        public static final String SEARCH_FAIL = "error.crud.searchFail;";
    }

    public static final class ApiErrorKey {
        public static String NOT_EXIST = "error.api.notExist";
        public static String OUT_OF_DATE = "error.api.outOfDate";
    }

    public static final class UserErrorKey {
        public static final String USER_EXIST = "error.userAndPermission.userExist";
        public static final String USER_INVALID = "error.userAndPermission.userInvalid";
    }
}
