package com.smartherd.manga2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaPageAdapter extends RecyclerView.Adapter<MangaPageAdapter.PageViewHolder> {
    private List<String> pageUrls;

    public MangaPageAdapter(List<String> pageUrls) {
        this.pageUrls = pageUrls;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return new PageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        Picasso.get().load(pageUrls.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pageUrls.size();
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
