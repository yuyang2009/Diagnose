package com.diagnose.diagnose.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.entity.DiagResEntity;
import com.diagnose.diagnose.db.PopulateDbAsync;

import java.util.Date;

@Database(entities = {DiagResEntity.class}, version=1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DiagResDAO diagResDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final DiagResDAO mDao;
//
//        PopulateDbAsync(AppDatabase db) {
//            mDao = db.diagResDAO();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            mDao.deleteAll();
//            DiagResEntity diagResEntity = new DiagResEntity("Hello test");
//            diagResEntity.Description = "test";
//            diagResEntity.CreateAt = new Date();
//            mDao.insert(diagResEntity);
//            diagResEntity = new DiagResEntity("World test");
//            diagResEntity.Description = "test";
//            diagResEntity.CreateAt = new Date();
//            mDao.insert(diagResEntity);
//            return null;
//        }
//    }


}
