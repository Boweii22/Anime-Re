package com.smartherd.manga2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarkViewHolder> {

    private List<Bookmark> bookmarksList;
    private Context context;
    private OnItemClickListener listener;

    // Interface for item click
    public interface OnItemClickListener {
        void onItemClick(Bookmark bookmark);
    }

    public BookmarksAdapter(List<Bookmark> bookmarksList, Context context, OnItemClickListener listener) {
        this.bookmarksList = bookmarksList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manga_item, parent, false);
        return new BookmarkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        Bookmark bookmark = bookmarksList.get(position);
        holder.mangaTitle.setText(bookmark.getTitle());
        Glide.with(context).load(bookmark.getImageUrl()).into(holder.mangaImage);

        // Handle item click
        holder.itemView.setOnClickListener(v -> listener.onItemClick(bookmark));
    }

    @Override
    public int getItemCount() {
        return bookmarksList.size();
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        public TextView mangaTitle;
        public ImageView mangaImage;

        public BookmarkViewHolder(View view) {
            super(view);
            mangaTitle = view.findViewById(R.id.manga_title);
            mangaImage = view.findViewById(R.id.manga_image);
        }
    }
}
