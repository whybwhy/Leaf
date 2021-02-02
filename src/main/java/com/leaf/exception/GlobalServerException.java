package com.leaf.exception;

import java.text.MessageFormat;

public class GlobalServerException extends RuntimeException {

    final private static String pattern = "[{0}] {1}";

    public GlobalServerException(GlobalExceptionMessage em) {
        super(MessageFormat.format(pattern, em.getCode(), em.getMessage()));
    }

    public GlobalServerException(GlobalExceptionMessage em, Throwable throwable) {
        super(MessageFormat.format(pattern, em.getCode(), em.getMessage()), throwable);
    }
}
