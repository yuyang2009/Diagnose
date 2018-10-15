package com.diagnose.diagnose.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.ViewModel.DiagResViewModel;
import com.diagnose.diagnose.databinding.FragmentDiagresBinding;
import com.diagnose.diagnose.db.entity.DiagResEntity;

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

        return mBinding.getRoot();
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
}
