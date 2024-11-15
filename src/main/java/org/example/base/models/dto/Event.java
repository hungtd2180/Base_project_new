package org.example.base.models.dto;


import org.example.base.constants.Constant;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by hungtd
 * Date: 15/11/2024
 * Time: 2:12 CH
 * for all issues, contact me: hungtd2180@gmail.com
 */

public class Event implements Serializable {
    public String id;
    public String method;
    public Integer errorCode;
    public String token;
    public String payload;

    public Event(){
        this.id = UUID.randomUUID().toString();
        this.errorCode = Constant.ResultStatus.SUCCESS;
    }
}
