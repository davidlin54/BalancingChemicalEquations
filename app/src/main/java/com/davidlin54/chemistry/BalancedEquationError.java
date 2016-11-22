package com.davidlin54.chemistry;

/**
 * Created by David on 2016-11-21.
 */

public class BalancedEquationError extends Exception {

    private String mMessage;

    public BalancedEquationError(String message) {
        mMessage = message;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }
}
