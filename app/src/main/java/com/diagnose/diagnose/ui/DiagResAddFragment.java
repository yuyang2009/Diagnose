package com.diagnose.diagnose.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.dao.DiagResDAO;
import com.diagnose.diagnose.db.AppDatabase;
import com.diagnose.diagnose.db.entity.DiagResEntity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class DiagResAddFragment extends Fragment {

    private TextView mTitle;

    private TextView mDescription;

    private String mTmpFilePath, mPhotoPath, mResFilePath;

    private static final String KEY_TMP_FILE_PATH = "tmpFilePath";
    private static final String KEY_PHOTO_PATH = "tmpFilePath";

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
        mTitle = (TextView) root.findViewById(R.id.name);
        mDescription = (TextView) root.findViewById(R.id.desp);

        mTmpFilePath = getArguments().getString(KEY_TMP_FILE_PATH);
        mPhotoPath = getArguments().getString(KEY_PHOTO_PATH);

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
        DiagResEntity diagResEntity = new DiagResEntity(mTitle.getText().toString());
        if(diagResEntity.isEmpty()) {
            showEmptyTaskError();
        } else {
            diagResEntity.ResultsPath = mResFilePath;
            diagResEntity.PhotoPath = mPhotoPath;
            diagResEntity.TmpFilePath = mTmpFilePath;

            AppDatabase database = AppDatabase.getDatabase(getContext());
            DiagResDAO dao = database.diagResDAO();
            dao.insert(diagResEntity);
            showTasksList();
        }
    }

    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
