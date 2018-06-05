package com.davidlin54.chemistry.exceptions;

/**
 * Created by David on 2016-11-21.
 */

public class BalancedEquationException extends Exception {

    private BalancedEquationExceptionType mExceptionType;

    public enum BalancedEquationExceptionType {
        INFINITE_SOLUTIONS, CAN_NOT_BALANCE
    }

    public BalancedEquationException(BalancedEquationExceptionType type) {
        mExceptionType = type;
    }

    public BalancedEquationExceptionType getExceptionType() {
        return mExceptionType;
    }
}
