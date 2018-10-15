package com.diagnose.diagnose.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.ViewModel.DiagResListViewModel;
import com.diagnose.diagnose.databinding.FragmentDiagreslistBinding;
import com.diagnose.diagnose.db.entity.DiagResEntity;

import java.util.List;

public class DiagResListFragment extends Fragment {

    public static final String TAG = "DiagResListViewModel";

    private DiagResAdapter mDiagResAdapter;

    private FragmentDiagreslistBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_diagreslist, container, false);

        mDiagResAdapter = new DiagResAdapter(mDiagResClickCallback);
        mBinding.productsList.setAdapter(mDiagResAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final DiagResListViewModel viewModel =
                ViewModelProviders.of(this).get(DiagResListViewModel.class);

        subscribeUi(viewModel);
    }

    private void subscribeUi(final DiagResListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getDiagRes().observe(this, new Observer<List<DiagResEntity>>() {
            @Override
            public void onChanged(@Nullable List<DiagResEntity> myDiagRes) {
                if (myDiagRes != null) {
                    mBinding.setIsLoading(false);
                    mDiagResAdapter.setDiagResList(myDiagRes);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    private final DiagResClickCallback mDiagResClickCallback = new DiagResClickCallback() {
        @Override
        public void onClick(DiagResEntity diagres) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(diagres);
            }
        }
    };
}
