package com.diagnose.diagnose.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.diagnose.diagnose.BasicApp;
import com.diagnose.diagnose.db.DiagResRepository;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.util.List;


public class DiagResViewModel extends AndroidViewModel {

    private final int mDiagResId;

    private final LiveData<DiagResEntity> mObserverableDiagRes;

    public ObservableField<DiagResEntity> diagres = new ObservableField<>();


    public DiagResViewModel(@NonNull Application application, DiagResRepository diagResRepository,
            final int diagResId) {
        super(application);
        mDiagResId = diagResId;
        mObserverableDiagRes = diagResRepository.getDiaresById(mDiagResId);
    }

    public LiveData<DiagResEntity> getmObserverableDiagRes() { return mObserverableDiagRes;}

    public void setDiagRes(DiagResEntity diagres) {
        this.diagres.set(diagres);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mDiagResId;

        private final DiagResRepository mRepository;

        public Factory(@NonNull Application application, int diagResId) {
            mApplication = application;
            mDiagResId = diagResId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new DiagResViewModel(mApplication, mRepository, mDiagResId);
        }
    }

}
