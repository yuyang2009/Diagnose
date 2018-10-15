package com.diagnose.diagnose.ui;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.diagnose.diagnose.R;
import com.diagnose.diagnose.db.entity.DiagResEntity;
import com.diagnose.diagnose.databinding.DiagresItemBinding;

import java.util.List;
import java.util.Objects;

public class DiagResAdapter extends RecyclerView.Adapter<DiagResAdapter.DiagResViewHolder> {
    List<? extends DiagResEntity> mDiagResList;

    @NonNull
    private final DiagResClickCallback mDiagResClickCallback;

    public DiagResAdapter(@NonNull DiagResClickCallback clickCallback) {
        mDiagResClickCallback = clickCallback;
    }

    public void setDiagResList(final List<? extends DiagResEntity> DiagResList) {
        if(mDiagResList == null) {
            mDiagResList = DiagResList;
            notifyItemRangeInserted(0, DiagResList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mDiagResList.size();
                }

                @Override
                public int getNewListSize() {
                    return DiagResList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mDiagResList.get(oldItemPosition).getId() ==
                            DiagResList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DiagResEntity newDiagRes = DiagResList.get(newItemPosition);
                    DiagResEntity oldDiagRes = mDiagResList.get(oldItemPosition);
                    return newDiagRes.getId() == oldDiagRes.getId()
                            && Objects.equals(newDiagRes.Description, oldDiagRes.Description)
                            && Objects.equals(newDiagRes.CreateAt, oldDiagRes.CreateAt)
                            && Objects.equals(newDiagRes.name, oldDiagRes.name);
                }
            });
            mDiagResList = DiagResList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public DiagResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DiagresItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.diagres_item,
                        parent, false);
        binding.setCallback(mDiagResClickCallback);
        return new DiagResViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DiagResViewHolder holder, int position) {
        holder.binding.setDiagres(mDiagResList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDiagResList == null ? 0 : mDiagResList.size();
    }


    static class DiagResViewHolder extends RecyclerView.ViewHolder {

        final DiagresItemBinding binding;

        public DiagResViewHolder(DiagresItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
