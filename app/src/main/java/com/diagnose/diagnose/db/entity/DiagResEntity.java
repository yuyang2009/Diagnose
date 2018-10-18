package com.diagnose.diagnose.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.diagnose.diagnose.db.DateConverter;


import java.text.SimpleDateFormat;
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

    public String getDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        return formatter.format(CreateAt);
    }

    public boolean isEmpty() {
        return isNullOrEmpty(name) &&
                isNullOrEmpty(Description);
    }

    public boolean isNullOrEmpty(String s) {
        return s.isEmpty() || s == null;
    }

}
