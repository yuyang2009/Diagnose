package com.diagnose.diagnose.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.diagnose.diagnose.db.DateConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd-hh:mm:ss");
        return formatter.format(CreateAt);
    }

    public String readFile2String(String path) throws IOException {
        InputStream is = new FileInputStream(path);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuffer sb = new StringBuffer();

        while(line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        return sb.toString();
    }

    public List<DataEntry> genTmpChart(String str) {

        List<DataEntry> seriesData = new ArrayList<>();
        String[] vars = str.split(",");
        for(int i=0; i<vars.length; ++i) {
            seriesData.add(new ValueDataEntry(i, Integer.parseInt(vars[i])));
        }
        return seriesData;
    }

    public Cartesian getResChart() {

        Cartesian cartesian = AnyChart.line();

        try {
            String s = readFile2String(TmpFilePath);
            cartesian.data(genTmpChart(s));
            cartesian.legend().enabled(true);
            cartesian.legend().fontSize(13d);
            cartesian.legend().padding(0d, 0d, 10d, 0d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartesian;
    }

}
