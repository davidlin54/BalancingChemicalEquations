package com.davidlin54.chemistry;

import android.app.Application;
import android.content.Context;

/**
 * Created by David on 2016-11-15.
 */

public class BalancingChemicalEquations extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
