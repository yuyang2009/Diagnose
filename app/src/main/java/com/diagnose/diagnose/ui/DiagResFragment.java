package com.diagnose.diagnose.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.diagnose.diagnose.R;
import com.diagnose.diagnose.ViewModel.DiagResViewModel;
import com.diagnose.diagnose.databinding.FragmentDiagresBinding;
import com.diagnose.diagnose.db.entity.DiagResEntity;
import com.diagnose.diagnose.db.PopulateDbAsync;

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
                AnyChartView chart_res = root.findViewById(R.id.chart_res);
                chart_res.setProgressBar(root.findViewById(R.id.pb_res));
                chart_res.setChart(getResChart(diagres.ResultsPath));

                AnyChartView chart_tmp = root.findViewById(R.id.chart_tmp);
                chart_tmp.setProgressBar(root.findViewById(R.id.pb_tmp));
                chart_tmp.setChart(getResChart(diagres.TmpFilePath));
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


    public Cartesian getResChart(String path) {

        Cartesian cartesian = AnyChart.line();

        try {
            String s = readFile2String(path);
            cartesian.data(genTmpChart(s));
            cartesian.legend().enabled(true);
            cartesian.legend().fontSize(13d);
            cartesian.legend().padding(0d, 0d, 10d, 0d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartesian;
    }

    public String readFile2String(String path) throws IOException {
        String uriPrefix = PopulateDbAsync.uriPrefix;
        InputStream is;
        if(path.startsWith(uriPrefix)) {
            AssetManager am = this.getActivity().getAssets();
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

    public List<DataEntry> genTmpChart(String str) {

        List<DataEntry> seriesData = new ArrayList<>();
        String[] vars = str.split(",");
        for(int i=0; i<vars.length; ++i) {
            seriesData.add(new ValueDataEntry(i, Integer.parseInt(vars[i].trim())));
        }
        return seriesData;
    }
}
