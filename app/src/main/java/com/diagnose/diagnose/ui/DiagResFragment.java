package com.diagnose.diagnose.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.ViewModel.DiagResViewModel;
import com.diagnose.diagnose.databinding.FragmentDiagresBinding;
import com.diagnose.diagnose.db.PopulateDbAsync;
import com.diagnose.diagnose.db.entity.DiagResEntity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DiagResFragment extends Fragment {

    private static final String KEY_DIAGRESR_ID = "diagres_id";

    private FragmentDiagresBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_diagres, container, false);
        View root = mBinding.getRoot();

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DiagResViewModel.Factory factory = new DiagResViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_DIAGRESR_ID));

        final DiagResViewModel model = ViewModelProviders.of(this, factory)
                .get(DiagResViewModel.class);

        mBinding.setDiagResViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final DiagResViewModel model) {

        // Observe product data
        model.getmObserverableDiagRes().observe(this, new Observer<DiagResEntity>() {
            @Override
            public void onChanged(@Nullable DiagResEntity productEntity) {
                model.setDiagRes(productEntity);
                DiagResViewModel model = mBinding.getDiagResViewModel();
                DiagResEntity diagres = model.getmObserverableDiagRes().getValue();

                View root = mBinding.getRoot();

                Context context = getContext();

                ImageView imageView = root.findViewById(R.id.photo_src);
                Bitmap bitmap = getBitmapFromPath(context, diagres.PhotoPath);
                imageView.setImageBitmap(bitmap);

                LineDataSet dataSet_res = getResChart(context, diagres.ResultsPath);
                dataSet_res.setLabel("Diagnose Result");
                LineData lineData_res = new LineData(dataSet_res);
                LineChart chart_res = root.findViewById(R.id.chart_res);
                chart_res.setData(lineData_res);

                LineChart chart_tmp = root.findViewById(R.id.chart_tmp);
                LineDataSet dataSet_tmp = getTmpChart(context, diagres.TmpFilePath);
                dataSet_tmp.setLabel("Temperature trend");
                LineData lineData_tmp = new LineData(dataSet_tmp);
                chart_tmp.setData(lineData_tmp);

            }
        });
    }

    /** Creates product fragment for specific product ID */
    public static DiagResFragment forDiagRes(int diagresId) {
        DiagResFragment fragment = new DiagResFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_DIAGRESR_ID, diagresId);
        fragment.setArguments(args);
        return fragment;
    }


    public static LineDataSet getResChart(Context context, String path) {
        List<Entry> entries = new ArrayList<Entry>();
        try {
            String s = readFile2String(context, path);
            String[] vars = s.split(",");
            String[] xValues = {"0", "0.4", "0.5", "0.6", "0.7"};
            for(int i=0; i<vars.length; ++i) {
                entries.add(new Entry(Float.parseFloat(xValues[i].trim()),
                                        Float.parseFloat(vars[i].trim())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Label");
        return lineDataSet;
    }

    public static LineDataSet getTmpChart(Context context, String path) {
        List<Entry> entries = new ArrayList<Entry>();
        try {
            String s = readFile2String(context, path);
            String[] vars = s.split(",");
            for(int i=0; i<vars.length; ++i) {
                entries.add(new Entry(i, Float.parseFloat(vars[i].trim())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Label");
        return lineDataSet;
    }


    public static String readFile2String(Context context,String path) throws IOException {
        String uriPrefix = PopulateDbAsync.uriPrefix;
        InputStream is;
        if(path.startsWith(uriPrefix)) {
            AssetManager am = context.getAssets();
            is = am.open(path);
        } else {
            is = new FileInputStream(new File(path));
        }
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuffer sb = new StringBuffer();

        while(line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        return sb.toString();
    }

    public static Bitmap getBitmapFromPath(Context context, String path){
        String uriPrefix = PopulateDbAsync.uriPrefix;
        InputStream is = null;
        try {
            if(path.startsWith(uriPrefix)) {
                AssetManager am = context.getAssets();
                is = am.open(path);
            } else {
                is = new FileInputStream(new File(path));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

}
