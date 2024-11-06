package com.smartherd.manga2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookMarkFragment extends Fragment implements BookmarksAdapter.OnItemClickListener {

    private RecyclerView bookmarksRecyclerView;
    private BookmarksAdapter bookmarksAdapter;
    private List<Bookmark> bookmarksList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ListenerRegistration registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_book_mark, container, false);

        bookmarksRecyclerView = rootView.findViewById(R.id.recycler_view_bookmarks);
        bookmarksList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up the RecyclerView with a GridLayoutManager for 2 columns
        bookmarksRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set up the adapter for the RecyclerView
        bookmarksAdapter = new BookmarksAdapter(bookmarksList, getContext(), this);
        bookmarksRecyclerView.setAdapter(bookmarksAdapter);

        loadBookmarkedManga();

        return rootView;
    }

    private void loadBookmarkedManga() {
        String userId = auth.getCurrentUser().getUid();
        Log.d("BookmarksFragment", "User ID: " + userId);
        Log.d("BookmarksFragment", "Number of bookmarks: " + bookmarksList.size());

        registration = db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Error loading bookmarks: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    bookmarksList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Bookmark bookmark = doc.toObject(Bookmark.class);
                        bookmarksList.add(bookmark);
                    }
                    bookmarksAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onItemClick(Bookmark bookmark) {
        // Handle the item click, passing the bookmark details to MangaDetailActivity
        Intent intent = new Intent(getContext(), MangaDetailActivity.class);
        intent.putExtra("manga_id", bookmark.getMangaId());
        intent.putExtra("imageUrl", bookmark.getImageUrl());
        intent.putExtra("title", bookmark.getTitle());
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration != null) {
            registration.remove();
        }
    }
}
