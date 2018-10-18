package com.diagnose.diagnose.ui;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diagnose.diagnose.BasicApp;
import com.diagnose.diagnose.R;
import com.diagnose.diagnose.ViewModel.DiagResListViewModel;
import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.AppDatabase;
import com.diagnose.diagnose.db.PopulateDbAsync;
import com.diagnose.diagnose.db.entity.DiagResEntity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiagResAddFragment extends Fragment {

    private TextView mTitle;

    private TextView mDescription;

    private String mTmpFilePath, mPhotoPath, mResFilePath;

    private static final String KEY_TMP_FILE_PATH = "tmpFilePath";
    private static final String KEY_PHOTO_PATH = "photoPath";

    private DiagResListViewModel viewModel;

    private List<String> resList = new ArrayList<>();

    public static DiagResAddFragment newInstance(String tmpFilePath, String photoPath) {
        DiagResAddFragment fragment = new DiagResAddFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TMP_FILE_PATH, tmpFilePath);
        args.putString(KEY_PHOTO_PATH, photoPath);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_adddiagres, container, false);
        viewModel = ViewModelProviders.of(this).get(DiagResListViewModel.class);

        mTitle = (TextView) root.findViewById(R.id.name);
        mDescription = (TextView) root.findViewById(R.id.desp);

        mTmpFilePath = getArguments().getString(KEY_TMP_FILE_PATH);
        mPhotoPath = getArguments().getString(KEY_PHOTO_PATH);
        mResFilePath = createResultFile();

        Context context = getContext();
        ImageView imageView = root.findViewById(R.id.photo_src);
        Bitmap bitmap = DiagResFragment.getBitmapFromPath(context, mPhotoPath);
        imageView.setImageBitmap(bitmap);

        LineDataSet dataSet_res = DiagResFragment.getResChart(context, mResFilePath);
        dataSet_res.setLabel("Diagnose Result");
        LineData lineData_res = new LineData(dataSet_res);
        LineChart chart_res = root.findViewById(R.id.chart_res);
        chart_res.setData(lineData_res);

        LineChart chart_tmp = root.findViewById(R.id.chart_tmp);
        LineDataSet dataSet_tmp = DiagResFragment.getTmpChart(context, mTmpFilePath);
        dataSet_tmp.setLabel("Temperature trend");
        LineData lineData_tmp = new LineData(dataSet_tmp);
        chart_tmp.setData(lineData_tmp);

        Button confirmBtn = root.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDiagRes();
            }
        });
        return root;
    }

    public void showEmptyTaskError() {
        Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    private void createDiagRes() {
        if(mTitle.getText().toString().isEmpty()) {
            showEmptyTaskError();
        } else {
            DiagResEntity diagResEntity = new DiagResEntity(mTitle.getText().toString());
            diagResEntity.Description = mDescription.getText().toString();
            diagResEntity.ResultsPath = mResFilePath;
            diagResEntity.PhotoPath = mPhotoPath;
            diagResEntity.TmpFilePath = mTmpFilePath;

            viewModel.insert(diagResEntity);
            showTasksList();
        }
    }

    public void showTasksList() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private String createResultFile(){
        // TODO: add image analysis code;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String tmpFileName = "res_" + timeStamp + ".dat";
        try {
            if(resList.isEmpty() || resList == null) {
                tmpFileName = PopulateDbAsync.uriPrefix + PopulateDbAsync.resFileName;
            } else {
                FileOutputStream fos = getActivity().openFileOutput(tmpFileName, Context.MODE_PRIVATE);
                String tmpStr = TextUtils.join(", ", resList);
                fos.write(tmpStr.getBytes());
                fos.close();
                tmpFileName = getActivity().getFileStreamPath(tmpFileName).getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFileName;
    }

}
