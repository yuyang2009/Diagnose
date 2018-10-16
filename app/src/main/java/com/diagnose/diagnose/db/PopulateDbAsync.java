package com.diagnose.diagnose.db;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.io.File;
import java.util.Date;

public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final DiagResDAO mDao;
    private final String path = Environment.getDataDirectory().getPath();

    PopulateDbAsync(AppDatabase db) {
        mDao = db.diagResDAO();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        mDao.deleteAll();
        DiagResEntity diagResEntity = new DiagResEntity("Concentration test");
        diagResEntity.Description = "This is a sample of the diagnose result.";
        diagResEntity.PhotoPath = pathCombine(path, "photo.jpg");
        diagResEntity.TmpFilePath = pathCombine(path, "tmp_test_data.dat");
        diagResEntity.ResultsPath = pathCombine(path, "res_test_data.dat");
        Log.i("path test log", String.format("doInBackground: PhotoPath: %s", diagResEntity.PhotoPath));
        diagResEntity.CreateAt = new Date();
        mDao.insert(diagResEntity);
        return null;
    }

    private static String pathCombine(String path1, String path2) {
        return new File(path1, path2).getPath();
    }
}
