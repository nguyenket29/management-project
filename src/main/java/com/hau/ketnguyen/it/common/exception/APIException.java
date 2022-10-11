package com.hau.ketnguyen.it.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class APIException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;
    private String errorCode;
    private Integer inventoryErrorCode;
    private List<String> data;

    private APIException() {
    }

    public static APIException from(HttpStatus httpStatus) {
        APIException ret = new APIException();
        ret.httpStatus = httpStatus;
        return ret;
    }

    public APIException withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public APIException withInventoryErrorCode(Integer inventoryErrorCode) {
        this.inventoryErrorCode = inventoryErrorCode;
        return this;
    }

    public APIException withMessage(String message) {
        this.message = message;
        return this;
    }

    public APIException withMessage(List<String> data) {
        this.data = data;
        return this;
    }
}
