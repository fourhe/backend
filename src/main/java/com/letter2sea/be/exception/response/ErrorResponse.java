package com.letter2sea.be.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.letter2sea.be.exception.Letter2SeaException;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_EMPTY)
public class ErrorResponse {

    private final String errorCode;
    private final String message;

    private final Map<String, String> detail;

    public ErrorResponse(Letter2SeaException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
        this.detail = new HashMap<>();
    }

    @Builder
    public ErrorResponse(String errorCode, String message, Map<String, String> detail) {
        this.errorCode = errorCode;
        this.message = message;
        this.detail = detail;
    }

    public void addDetail(String fieldName, String detailMessage) {
        this.detail.put(fieldName, detailMessage);
    }
}
