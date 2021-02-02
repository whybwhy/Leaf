package com.leaf.exception;

import java.text.MessageFormat;

public class GlobalException extends RuntimeException {

    final private static String pattern = "[{0}] {1}";



    public GlobalException(GlobalExceptionMessage em) {
        super(MessageFormat.format(pattern, em.getCode(), em.getMessage()));
    }

    public GlobalException(GlobalExceptionMessage em, Throwable throwable) {
        super(MessageFormat.format(pattern, em.getCode(), em.getMessage()), throwable);
    }
}
