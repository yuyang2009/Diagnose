package com.diagnose.diagnose.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.lifecycle.LiveData;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.util.List;

@Dao
public interface DiagResDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DiagResEntity... diagResEntities);

    @Query("DELETE FROM DiagResTable")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM diagrestable")
    Integer countDiagRes();

    @Query("SELECT * FROM DiagResTable WHERE id = (:id)")
    LiveData<DiagResEntity> loadAllById(int id);

    @Query("SELECT * FROM DiagResTable WHERe name LIKE :name LIMIT 1")
    DiagResEntity findByName(String name);

    @Query("SELECT * FROM DiagResTable ORDER BY CreateAt DESC")
    LiveData<List<DiagResEntity>> getDiagResAll();

    @Delete
    void deleteDiagRes(DiagResEntity diagResEntity);
}
