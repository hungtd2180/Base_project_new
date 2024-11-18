package org.example.base.constants;


/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:04 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class Constant {
    private Constant(){}

    public static final class Time{
        public static final String SECOND = "second";
        public static final String MINUTE = "minute";
        public static final String HOUR = "hour";
        public static final String DATE = "date";
        public static final String MONTH = "month";
        public static final String YEAR = "year";
    }

    public static final class Regex{
        public static final String EMAIL = "(?!.*(?:''|\\.\\.))[a-zA-Z0-9_-]{1,}[\\.a-zA-Z0-9_-]{1,}[^.]@[a-z0-9]{1,}[a-z0-9-]{0,}(\\.[a-z0-9]{2,4}){1,3}$";
        public static final String VN_PHONE = "^(\\+?84|0|84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";
        /**Có ít nhất 1 kí tự chữ hoa hoặc thường, có ít nhất 1 số, không có khoảng trắng và độ dài từ 8 - 32 kí tự*/
        public static final String PASSWORD = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=\\S+$).{8,32}$";
        /**Có ít nhất 1 kí tự chữ thường, 1 kí tự chữ hoa, 1 kí tự đặc biệt, 1 kí tự số, độ dài từ 8 - 32 kí tự*/
        public static final String ATTT_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    }

    public static final class ResultStatus{
        public static final Integer SUCCESS = 0;
        public static final Integer ERROR = 1;
        public static final Integer TIME_OUT = -1;
    }

    //Bổ sung thêm method khi tạo service
    public static final class Method{
        public static final String CREATE = "create";
        public static final String UPDATE = "update";
        public static final String DELETE = "delete";
        public static final String GET_ONE = "get_one";
        public static final String GET_ALL = "get_all";
        public static final String ACTIVE = "active";
        public static final String DEACTIVE = "deactive";
    }

    public static final class EntityStatus {
        public static final Integer DEACTIVE = 0;
        public static final Integer ACTIVE = 1;
        public static final Integer REGISTER = 2;
        public static final Integer DELETED = 3;
    }
}
