package com.davidlin54.chemistry.exceptions;

/**
 * Created by David on 2017-01-04.
 */

public class ReactantException extends Exception {
    private CompoundParsingException mException;

    public ReactantException(CompoundParsingException exception) {
        mException = exception;
    }

    public CompoundParsingException getException() {
        return mException;
    }
}