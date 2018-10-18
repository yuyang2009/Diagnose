package com.diagnose.diagnose.db;

import android.os.AsyncTask;

import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.util.Date;

public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final DiagResDAO mDao;
//    private final String basepath = Environment.getDataDirectory().getAbsolutePath();
//    private final String targPath = pathCombine(basepath, "sampledata");
//    private final String basepath = "file:///android_asset/";
    public static final String uriPrefix = "sampledata/";
    public static final String photoFileName = "photo.jpg";
    public static final String tmpFileName = "tmp_test_data.dat";
    public static final String resFileName = "res_test_data.dat";

    PopulateDbAsync(AppDatabase db) {
        mDao = db.diagResDAO();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        mDao.deleteAll();
        DiagResEntity diagResEntity = new DiagResEntity("Concentration test");
        diagResEntity.Description = "This is a sample of the diagnose result.";

        diagResEntity.PhotoPath = pathCombine(uriPrefix, photoFileName);
        diagResEntity.TmpFilePath = pathCombine(uriPrefix, tmpFileName);
        diagResEntity.ResultsPath = pathCombine(uriPrefix, resFileName);
        diagResEntity.CreateAt = new Date();
        mDao.insert(diagResEntity);
        return null;
    }

    public static String pathCombine(String path1, String path2) {
        return path1 + path2;
    }


}
