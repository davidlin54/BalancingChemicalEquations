package com.davidlin54.chemistry;

/**
 * Created by David on 2017-01-02.
 */

public interface MainView {
    void setResults(String result);
    void setError(int resId, int errorTextResId);
    void setError(int resId, String errorText);
}
