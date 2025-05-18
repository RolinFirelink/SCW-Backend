package com.rolin.orangesmart.exception;

import com.rolin.orangesmart.model.common.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    /**
     * 统一结果类
     */
    private Result<Object> result;

    public GlobalException(Result<Object> result) {
        super(result.getMessage());
        this.result = result;
    }

}
