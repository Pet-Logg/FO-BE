package com.petlog.common.exception;

import lombok.Getter;

@Getter
public class BizRuntimeException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public BizRuntimeException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public BizRuntimeException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizRuntimeException(String errorCode, String errorMessage, Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BizRuntimeException(String errorMessage, Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorMessage = errorMessage;
    }

    public BizRuntimeException(Throwable cause) {
        super(cause);
    }

}