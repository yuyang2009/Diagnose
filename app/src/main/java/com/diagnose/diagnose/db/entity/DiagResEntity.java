package com.diagnose.diagnose.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.diagnose.diagnose.db.DateConverter;

import java.util.Date;

@Entity(tableName = "DiagResTable")
@TypeConverters(DateConverter.class)
public class DiagResEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    public String Description, PhotoPath, TmpFilePath, ResultsPath;
    public Date CreateAt;

    public int getId() {return this.id;}
    public void setId(int id) {this.id = id;}
    public DiagResEntity(@NonNull String name) {
        this.name = name;
        this.CreateAt = new Date();
    }

    public String getName() {return this.name;}

}
