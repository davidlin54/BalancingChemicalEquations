package com.davidlin54.chemistry.exceptions;

/**
 * Created by David on 2017-01-04.
 */

public class ProductException extends Exception {
    private CompoundParsingException mException;

    public ProductException(CompoundParsingException exception) {
        mException = exception;
    }

    public CompoundParsingException getException() {
        return mException;
    }
}
