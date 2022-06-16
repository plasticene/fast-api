package com.shepherd.fast.exception;

import com.shepherd.fast.enums.ResponseStatusEnum;
import lombok.Data;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/6/16 10:47
 */

/**
 * 业务异常类
 */
@Data
public class BizException extends RuntimeException {

    private Integer code;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ResponseStatusEnum responseStatusEnum) {
        super(responseStatusEnum.getMsg());
        this.code = responseStatusEnum.getCode();
    }

}