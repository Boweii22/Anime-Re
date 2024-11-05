package com.smartherd.manga2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MangaDetailActivity extends AppCompatActivity {
    private ImageView mangaImage, bookMarkBtn;
    private TextView mangaTitle, mangaDescription, authorTextView, statusTextView, ratingTextView;
    private Button readButton;

    private FirebaseFirestore db;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);

        mangaImage = findViewById(R.id.manga_detail_image);
        mangaTitle = findViewById(R.id.manga_detail_title);
        mangaDescription = findViewById(R.id.manga_detail_description);
        authorTextView = findViewById(R.id.manga_detail_author);
        statusTextView = findViewById(R.id.manga_detail_status);
        ratingTextView = findViewById(R.id.manga_detail_rate);
        readButton = findViewById(R.id.read_button);
        bookMarkBtn = findViewById(R.id.bookMarkBtn);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        String mangaId = getIntent().getStringExtra("manga_id");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        //Load the cover image using Glide
        Glide.with(this)
                        .load(imageUrl)
                                .into(mangaImage);
        fetchMangaDetails(mangaId);

        bookMarkBtn.setOnClickListener(v -> bookmarkManga(mangaId, imageUrl, mangaTitle.getText().toString()));
    }

    private void bookmarkManga(String mangaId, String imageUrl, String title) {
        String userId = auth.getCurrentUser().getUid();
        Bookmark bookmark = new Bookmark(mangaId, title, imageUrl);

        db.collection("users").document(userId).collection("bookmarks").document(mangaId)
                .set(bookmark)
                .addOnSuccessListener(aVoid -> Toast.makeText(MangaDetailActivity.this, "Manga bookmarked", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MangaDetailActivity.this, "Error bookmarking manga: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void fetchMangaDetails(String mangaId) {
        String url = "https://api.mangadex.org/manga/" + mangaId;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
//                        String cleanDescription =  "";
                        JSONObject mangaObject = response.getJSONObject("data");
                        JSONObject attributes = mangaObject.getJSONObject("attributes");

                        String title = mangaObject.getJSONObject("attributes").getJSONObject("title").getString("en");
                        String description = mangaObject.getJSONObject("attributes").getJSONObject("description").getString("en");

                        description.replace("\\n", " ").replace("\\","");


//                        String imageUrl = "https://uploads.mangadex.org/covers/" + mangaId + "/cover.jpg";

                        // Retrieve genres from tags
                        JSONArray tagsArray = mangaObject.getJSONObject("attributes").getJSONArray("tags");
                        List<String> genres = new ArrayList<>();
                        for (int i = 0; i < tagsArray.length(); i++) {
                            JSONObject tagObject = tagsArray.getJSONObject(i);
                            String genre = tagObject.getJSONObject("attributes").getJSONObject("name").getString("en");
                            genres.add(genre);
                        }

                        mangaTitle.setText(title);
                        mangaDescription.setText(description);
//                        Picasso.get().load(imageUrl).into(mangaImage);

                        // Display genres as comma-separated list
                        TextView genreTextView = findViewById(R.id.manga_detail_genre);
                        genreTextView.setText("Genres: " + String.join(", ", genres));

                        // Status
                        String status = attributes.optString("status", "Unknown");
                        status = status.substring(0, 1).toUpperCase() + status.substring(1);

                        // Retrieve the author name (if available)
                        JSONArray relationships = mangaObject.optJSONArray("relationships");
                        String authorName = "Unknown";
                        if (relationships != null) {
                            for (int i = 0; i < relationships.length(); i++) {
                                JSONObject relationship = relationships.getJSONObject(i);
                                if ("author".equals(relationship.optString("type"))) {
                                    // Assuming author name is in "attributes" of the relationship
                                    JSONObject authorAttributes = relationship.optJSONObject("attributes");
                                    if (authorAttributes != null) {
                                        authorName = authorAttributes.optString("name", "Unknown Author");
                                    }
                                    break;
                                }
                            }
                        }

                        statusTextView.setText(status);
                        authorTextView.setText(authorName);

                        // Retrieve ratings from "links" (if available)
                        JSONObject links = attributes.optJSONObject("links");
                        String malRating = links != null ? links.optString("mal", "N/A") : "N/A";
                        String ebjRating = links != null ? links.optString("ebj", "N/A") : "N/A";


                        // Display ratings if available
                        ratingTextView.setText(malRating);

                        readButton.setOnClickListener(v -> {
//                            String readUrl = "https://mangadex.org/title/" + mangaId;
//                            Intent intent = new Intent(MangaDetailActivity.this, MangaReaderActivity.class);
////                            intent.putExtra("mangaUrl", Uri.parse(readUrl)); // replace with actual URL
//                            DataHolder.getInstance().setMangaUrl(readUrl);
//                            startActivity(intent);
////                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(readUrl));
////                            startActivity(browserIntent);
                            Intent intent = new Intent(MangaDetailActivity.this, MangaReaderActivity.class);
                            intent.putExtra("manga_id", mangaId);
                            startActivity(intent);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(this, "Error loading details", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
