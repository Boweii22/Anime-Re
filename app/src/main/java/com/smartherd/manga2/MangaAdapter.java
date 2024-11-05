package com.smartherd.manga2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {
    private List<Manga> mangaList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Manga manga);
    }

    public MangaAdapter(List<Manga> mangaList, Context context, OnItemClickListener listener) {
        this.mangaList = mangaList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manga_item, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        holder.title.setText(manga.getTitle());
        Picasso.get().load(manga.getImageUrl()).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(manga));
    }

    @Override
    public int getItemCount() { return mangaList.size(); }

    public class MangaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.manga_image);
            title = itemView.findViewById(R.id.manga_title);
        }
    }
}

