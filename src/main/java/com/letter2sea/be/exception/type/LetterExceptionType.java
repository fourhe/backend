package com.letter2sea.be.exception.type;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LetterExceptionType implements ExceptionType {
    LETTER_NOT_FOUND("LETTER001", "존재하지 않는 편지입니다.", HttpStatus.NOT_FOUND),
    LETTER_ALREADY_DELETED("LETTER002", "이미 삭제한 편지입니다.", HttpStatus.BAD_REQUEST),
    LETTER_ALREADY_READ("LETTER003", "이미 읽은 편지이거나 자신이 작성한 편지입니다.", HttpStatus.BAD_REQUEST),
    LETTER_ALREADY_REPLY("LETTER004", "이미 답장한 편지이거나 자신이 작성한 편지입니다.", HttpStatus.BAD_REQUEST);

    private final String errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.httpStatus;
    }
}
