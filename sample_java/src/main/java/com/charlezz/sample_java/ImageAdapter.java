package com.charlezz.sample_java;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.charlezz.sample_java.databinding.ViewImageBinding;

import life.sabujak.pickle.data.entity.Media;

public class ImageAdapter extends ListAdapter<Media, ImageAdapter.ViewHolder> {

    private static DiffUtil.ItemCallback<Media> diffCallback = new DiffUtil.ItemCallback<Media>() {
        @Override
        public boolean areItemsTheSame(@NonNull Media oldItem, @NonNull Media newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Media oldItem, @NonNull Media newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };
    protected ImageAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewImageBinding binding = ViewImageBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.binding.imageView).load(getItem(position).getUri()).into(holder.binding.imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ViewImageBinding binding;
        public ViewHolder(@NonNull ViewImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
