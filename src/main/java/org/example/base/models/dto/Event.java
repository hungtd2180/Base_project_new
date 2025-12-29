package org.example.base.models.dto;


import lombok.Getter;
import lombok.Setter;
import org.example.base.constants.Constant;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:12 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */
@Setter
@Getter
public class Event implements Serializable {
    public String id;
    public String method;
    public Integer errorCode;
    public String token;
    public Object payload;
    private String ipAddress;

    public Event() {
    }

    public Event(String method, Object payload){
        this.method = method;
        this.payload = payload;
        this.id = UUID.randomUUID().toString();
        this.errorCode = Constant.ResultStatus.SUCCESS;
    }

    public Event(String method){
        this.method = method;
        this.id = UUID.randomUUID().toString();
        this.errorCode = Constant.ResultStatus.SUCCESS;
    }
}
