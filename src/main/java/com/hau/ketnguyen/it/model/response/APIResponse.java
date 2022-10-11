package com.hau.ketnguyen.it.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hau.ketnguyen.it.common.constant.Constants;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"status", "message", "data"})
public class APIResponse<T> {
    @JsonProperty(value = "status")
    private String statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("dataErrorMessages")
    private T dataErrorMessages;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("data")
    private T data;
    @JsonProperty("error_code")
    private Integer errorCode;
    @JsonProperty("error_code_custom")
    private Integer errorCodeCustom;
    @JsonProperty("dataError")
    private List<String> dataError;

    public List<String> getDataError() {
        return dataError;
    }

    public void setDataError(List<String> dataError) {
        this.dataError = dataError;
    }

    public Integer getErrorCodeCustom() {
        return errorCodeCustom;
    }

    public void setErrorCodeCustom(Integer errorCodeCustom) {
        this.errorCodeCustom = errorCodeCustom;
    }

    public static <T> APIResponse<T> success(T data) {
        APIResponse<T> ret = new APIResponse<>();
        ret.statusCode = Constants.StatusCode.SUCCESS;
        ret.data = data;
        return ret;
    }

    public static <T> APIResponse<T> success() {
        APIResponse<T> ret = new APIResponse<>();
        ret.statusCode = Constants.StatusCode.SUCCESS;
        return ret;
    }

    public static <T> APIResponse<T> success(T data, String message) {
        APIResponse<T> ret = new APIResponse<>();
        ret.message = message;
        ret.statusCode = Constants.StatusCode.SUCCESS;
        ret.data = data;
        return ret;
    }

    public static <T> APIResponse<T> failed(int errorCode, T dataErrorMessage, String errorMessage) {
        APIResponse<T> ret = new APIResponse<>();
        ret.statusCode = Constants.StatusCode.FAILED;
        ret.dataErrorMessages = dataErrorMessage;
        ret.errorMessage = errorMessage;
        ret.errorCode = errorCode;
        return ret;
    }

    public static <T> APIResponse<T> failedCustom(Integer errorCode,
                          T dataErrorMessage, String errorMessage, Integer errorCodeCustom, List<String> dataError) {
        APIResponse<T> ret = new APIResponse<>();
        ret.statusCode = Constants.StatusCode.FAILED;
        ret.dataErrorMessages = dataErrorMessage;
        ret.errorMessage = errorMessage;
        ret.errorCode = errorCode;
        ret.errorCodeCustom = errorCodeCustom;
        ret.dataError = dataError;
        return ret;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getDataErrorMessages() {
        return dataErrorMessages;
    }

    public void setDataErrorMessages(T dataErrorMessages) {
        this.dataErrorMessages = dataErrorMessages;
    }
}
