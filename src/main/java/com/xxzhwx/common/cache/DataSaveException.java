package com.xxzhwx.common.cache;

public class DataSaveException extends RuntimeException {
    private static final long serialVersionUID = 645157992655518417L;

    public DataSaveException(String message) {
        super(message);
    }

    public DataSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
