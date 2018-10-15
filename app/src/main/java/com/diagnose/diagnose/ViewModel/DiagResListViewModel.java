package com.diagnose.diagnose.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.diagnose.diagnose.BasicApp;
import com.diagnose.diagnose.db.DiagResRepository;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DiagResListViewModel extends AndroidViewModel {

    private final MediatorLiveData<List<DiagResEntity>> mObservableDiagRes;

    public DiagResListViewModel(Application application) {

        super(application);

        mObservableDiagRes = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableDiagRes.setValue(null);

        LiveData<List<DiagResEntity>> diagRes = ((BasicApp) application).getRepository()
                                                    .getAllDiagRes();

        // observe the changes of the products from the database and forward them
        mObservableDiagRes.addSource(diagRes, mObservableDiagRes::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<DiagResEntity>> getDiagRes() {
        return mObservableDiagRes;
    }
}
