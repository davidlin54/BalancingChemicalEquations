package com.davidlin54.chemistry.exceptions;

/**
 * Created by David on 2017-01-04.
 */

public class ReactantException extends Exception {
    private String mMessage;

    public ReactantException(String message) {
        mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}