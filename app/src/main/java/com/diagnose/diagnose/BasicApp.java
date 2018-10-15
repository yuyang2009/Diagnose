package com.diagnose.diagnose;

import android.app.Application;

import com.diagnose.diagnose.db.DiagResRepository;

public class BasicApp extends Application {

    private AppExecutors mAppExecutors;
    private DiagResRepository mDiaResRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }


    public DiagResRepository getRepository() {
        if(mDiaResRepository == null)
            mDiaResRepository =  new DiagResRepository(this);
        return mDiaResRepository;
    }
}
