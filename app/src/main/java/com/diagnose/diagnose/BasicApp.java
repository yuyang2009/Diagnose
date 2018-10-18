package com.diagnose.diagnose;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.support.annotation.NonNull;

import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.AppDatabase;
import com.diagnose.diagnose.db.DiagResRepository;

public class BasicApp extends Application {

    private AppExecutors mAppExecutors;
    private DiagResRepository mDiaResRepository;
    private AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDataBase() {
        if(mAppDatabase == null)
            mAppDatabase =  AppDatabase.getDatabase(this.getBaseContext());
        return mAppDatabase;
    }

    public DiagResRepository getRepository() {
        if(mDiaResRepository == null)
            mDiaResRepository =  new DiagResRepository(this);
        return mDiaResRepository;
    }
}
