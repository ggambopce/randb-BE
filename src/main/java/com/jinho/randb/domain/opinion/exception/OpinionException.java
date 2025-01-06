package com.jinho.randb.domain.opinion.exception;

public class OpinionException extends RuntimeException {
    public OpinionException() {
        super();
    }

    public OpinionException(String message) {
        super(message);
    }

    public OpinionException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpinionException(Throwable cause) {
        super(cause);
    }

    protected OpinionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
