package com.smartherd.manga2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity implements BookmarksAdapter.OnItemClickListener {

    private RecyclerView bookmarksRecyclerView;
    private BookmarksAdapter bookmarksAdapter;
    private List<Bookmark> bookmarksList;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        bookmarksRecyclerView = findViewById(R.id.recycler_view_bookmarks);
        bookmarksList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set up the RecyclerView with a GridLayoutManager for 2 columns
        bookmarksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set up the adapter for the RecyclerView
        bookmarksAdapter = new BookmarksAdapter(bookmarksList, this, this);
        bookmarksRecyclerView.setAdapter(bookmarksAdapter);

        loadBookmarkedManga();
    }

    private void loadBookmarkedManga() {
        String userId = auth.getCurrentUser().getUid();
        Log.d("BookmarksActivity", "User ID: " + userId);
        Log.d("BookmarksActivity", "Number of bookmarks: " + bookmarksList.size());

        registration = db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading bookmarks: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
    protected void onStop() {
        super.onStop();
        if (registration != null) {
            registration.remove();
        }
    }

    @Override
    public void onItemClick(Bookmark bookmark) {
        // Handle the item click, passing the bookmark details to MangaDetailActivity
        Intent intent = new Intent(this, MangaDetailActivity.class);
        intent.putExtra("manga_id", bookmark.getMangaId());
        intent.putExtra("imageUrl", bookmark.getImageUrl());
        intent.putExtra("title", bookmark.getTitle());
        startActivity(intent);
    }
}