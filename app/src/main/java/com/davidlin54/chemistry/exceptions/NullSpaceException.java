package com.davidlin54.chemistry.exceptions;

public class NullSpaceException extends Exception {

    private int mNullity;

    public NullSpaceException(int nullity) {
        mNullity = nullity;
    }

    public int getNullity() {
        return mNullity;
    }
}
