package com.smartherd.manga2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MangaAdapter mangaAdapter;
    private List<Manga> mangaList = new ArrayList<>();
    private EditText searchBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup categoryRadioGroup;
    private RadioButton radioButtonPopular, radioButtonTrending;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_favorites) {
                    startActivity(new Intent(MainActivity.this, BookMarkActivity.class));
                }

                return false;
            }
        });

        categoryRadioGroup = findViewById(R.id.category_radio_group);

        radioButtonPopular = findViewById(R.id.radio_popular);
        radioButtonTrending = findViewById(R.id.radio_trending);

        // Set up listener for category changes
        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String category = "popular"; // Default category

            if (checkedId == R.id.radio_popular) {
                category = "popular";
            } else if (checkedId == R.id.radio_trending) {
                category = "trending";
            }

            fetchMangaData(category);
        });

        searchBar = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mangaAdapter = new MangaAdapter(mangaList, this, manga -> {
            Intent intent = new Intent(MainActivity.this, MangaDetailActivity.class);
            intent.putExtra("manga_id", manga.getId());
            intent.putExtra("imageUrl", manga.getImageUrl());
            startActivity(intent);
        });
        recyclerView.setAdapter(mangaAdapter);
        fetchMangaData("action");

//        searchBar.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                fetchMangaData(searchBar.getText().toString());
//                return true;
//            }
//            return false;
//        });

        // Set the SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Disable RadioButtons during refresh
            // Clear the selection of the RadioGroup to reset to unselected state

            fetchMangaData(""); // Refresh the data
            categoryRadioGroup.clearCheck();

        });

        searchBar.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(runnable);
                runnable = () -> fetchMangaData(charSequence.toString());
                handler.postDelayed(runnable, 300); // Delay in milliseconds
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

//    private void fetchMangaData(String query) {
//        String url = "https://api.mangadex.org/manga?title=" + query;
//
//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        mangaList.clear();
//                        JSONArray dataArray = response.getJSONArray("data");
//
//                        for (int i = 0; i < dataArray.length(); i++) {
//                            JSONObject mangaObject = dataArray.getJSONObject(i);
//                            String id = mangaObject.getString("id");
//                            String title = mangaObject.getJSONObject("attributes").getJSONObject("title").getString("en");
//                            String imageUrl = "https://uploads.mangadex.org/covers/" + id + "/cover.jpg";
//
//                            mangaList.add(new Manga(id, title, imageUrl));
//                        }
//                        mangaAdapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show());
//
//        queue.add(jsonObjectRequest);
//    }
private void fetchMangaData(String query) {
    String url = "https://api.mangadex.org/manga?title=" + query + "&limit=20&includes[]=cover_art";


    if (query.equals("popular")) {
        url += "&order[followedCount]=desc"; // Use followedCount for popularity sorting
    } else if (query.equals("trending")) {
        url += "&order[relevance]=desc"; // Relevance might simulate trending
    }

// Debug print to check URL
    System.out.println("Generated URL: " + url);



    RequestQueue queue = Volley.newRequestQueue(this);
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            response -> {
                try {
                    mangaList.clear();
                    JSONArray dataArray = response.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject mangaObject = dataArray.getJSONObject(i);
                        String id = mangaObject.getString("id");
                        String title = mangaObject.getJSONObject("attributes").getJSONObject("title").getString("en");

                        // Get the cover_art relationship
                        JSONArray relationships = mangaObject.getJSONArray("relationships");
                        String imageUrl = null;
                        for (int j = 0; j < relationships.length(); j++) {
                            JSONObject relation = relationships.getJSONObject(j);
                            if (relation.getString("type").equals("cover_art")) {
                                String fileName = relation.getJSONObject("attributes").getString("fileName");
                                imageUrl = "https://uploads.mangadex.org/covers/" + id + "/" + fileName;
                                break;
                            }
                        }

                        // Only add the manga if we found a cover image
                        if (imageUrl != null) {
                            mangaList.add(new Manga(id, title, imageUrl));
                        }
                    }
                    mangaAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    // Stop the refreshing animation
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, error -> {
        error.printStackTrace();
        Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation on error

    });

    queue.add(jsonObjectRequest);
}

}
