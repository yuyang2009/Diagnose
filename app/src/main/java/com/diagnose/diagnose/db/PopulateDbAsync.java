package com.diagnose.diagnose.db;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.io.File;
import java.util.Date;

public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final DiagResDAO mDao;
//    private final String basepath = Environment.getDataDirectory().getAbsolutePath();
//    private final String targPath = pathCombine(basepath, "sampledata");
    private final String basepath = Uri.parse("android.resource://com.diagnose.diagnose/res/raw").toString();
    private final String photoFileName = "photo.jpg";
    private final String tmpFileName = "tmp_test_data.dat";
    private final String resFileName = "res_test_data.dat";

    PopulateDbAsync(AppDatabase db) {
        mDao = db.diagResDAO();
    }

    @Override
    protected Void doInBackground(final Void... params) {
        mDao.deleteAll();
        DiagResEntity diagResEntity = new DiagResEntity("Concentration test");
        diagResEntity.Description = "This is a sample of the diagnose result.";

        diagResEntity.PhotoPath = pathCombine(basepath, photoFileName);
        diagResEntity.TmpFilePath = pathCombine(basepath, tmpFileName);
        diagResEntity.ResultsPath = pathCombine(basepath, resFileName);
        diagResEntity.CreateAt = new Date();
        mDao.insert(diagResEntity);
        return null;
    }

    private static String pathCombine(String path1, String path2) {
        return new File(path1, path2).getPath();
    }

}
