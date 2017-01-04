package com.davidlin54.chemistry.exceptions;

/**
 * Created by David on 2016-11-21.
 */

public class BalancedEquationException extends Exception {

    private String mMessage;

    public BalancedEquationException(String message) {
        mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
