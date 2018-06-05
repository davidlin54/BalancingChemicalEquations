package com.davidlin54.chemistry.exceptions;

public class CompoundParsingException extends Exception {

    private CompoundParsingExceptionType mExceptionType;
    private String mExceptionText;

    public enum CompoundParsingExceptionType {
        COMPOUND_FORMATTING, NON_EXISTENT_ELEMENT
    }

    public CompoundParsingException(CompoundParsingExceptionType e, String exceptionText) {
        mExceptionType = e;
        mExceptionText = exceptionText;
    }

    public CompoundParsingExceptionType getExceptionType() {
        return mExceptionType;
    }

    public String getExceptionText() {
        return mExceptionText;
    }
}
