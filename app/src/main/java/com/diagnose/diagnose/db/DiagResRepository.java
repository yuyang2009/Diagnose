package com.diagnose.diagnose.db;

import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;

import java.util.List;

public class DiagResRepository {
    private DiagResDAO mDiagResDao;
    private MediatorLiveData<List<DiagResEntity>> mAllDiagRes;

    public DiagResRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDiagResDao = db.diagResDAO();
        mAllDiagRes = new MediatorLiveData<>();

        mAllDiagRes.addSource(mDiagResDao.getDiagResAll(),
            diagResEntities -> {
                mAllDiagRes.postValue(diagResEntities);
            });
    }

    public LiveData<List<DiagResEntity>> getAllDiagRes() {
        return mAllDiagRes;
    }
    public LiveData<DiagResEntity> getDiaresById(int id) {
        return  mDiagResDao.loadAllById(id);
    }

    public Void insert(DiagResEntity... diagResEntities) {
        insertAsyncTask task = new insertAsyncTask(mDiagResDao);
        task.execute(diagResEntities);
        return null;
    }

    private static class insertAsyncTask extends AsyncTask<DiagResEntity, Void, Void> {
        private DiagResDAO mAsyncTaskDao;

        insertAsyncTask(DiagResDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DiagResEntity... params) {
            mAsyncTaskDao.insert(params);
            return null;
        }
    }
}
