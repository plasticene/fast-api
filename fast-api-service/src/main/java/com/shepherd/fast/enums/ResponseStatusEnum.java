package com.shepherd.fast.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 10:44
 */
@ToString
@Getter
public enum ResponseStatusEnum {
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "Forbidden"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "Unauthorized"),
    SUCCESS(HttpStatus.OK, 200, "OK"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "Bad Request"),
    SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "系统异常错误1111");


    /**
     * 返回的HTTP状态码,  符合http请求
     */
    private HttpStatus httpStatus;
    /**
     * 业务异常码
     */
    private Integer code;
    /**
     * 业务异常信息描述
     */
    private String msg;

    ResponseStatusEnum(HttpStatus httpStatus, Integer code, String msg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }


}
