package com.smartherd.manga2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MangaAdapter mangaAdapter;
    private List<Manga> mangaList = new ArrayList<>();
    private EditText searchBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup categoryRadioGroup;
    private RadioButton radioButtonPopular, radioButtonTrending;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        categoryRadioGroup = rootView.findViewById(R.id.category_radio_group);
        radioButtonPopular = rootView.findViewById(R.id.radio_popular);
        radioButtonTrending = rootView.findViewById(R.id.radio_trending);
        searchBar = rootView.findViewById(R.id.search_bar);
        recyclerView = rootView.findViewById(R.id.recycler_view);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mangaAdapter = new MangaAdapter(mangaList, getContext(), manga -> {
            Intent intent = new Intent(getContext(), MangaDetailActivity.class);
            intent.putExtra("manga_id", manga.getId());
            intent.putExtra("imageUrl", manga.getImageUrl());
            startActivity(intent);
        });
        recyclerView.setAdapter(mangaAdapter);

        // Set up SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener(() -> fetchMangaData(""));

        // Set up category RadioButton change listener
        categoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String category = "popular"; // Default category
            if (checkedId == R.id.radio_popular) {
                category = "popular";
            } else if (checkedId == R.id.radio_trending) {
                category = "trending";
            }
            fetchMangaData(category);
        });

        // Set up search bar listener
        searchBar.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(runnable);
                runnable = () -> fetchMangaData(charSequence.toString());
                handler.postDelayed(runnable, 300);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Initially fetch manga data
        fetchMangaData("action");

        return rootView;
    }

    private void fetchMangaData(String query) {
        String url = "https://api.mangadex.org/manga?title=" + query + "&limit=20&includes[]=cover_art";
        if (query.equals("popular")) {
            url += "&order[followedCount]=desc";
        } else if (query.equals("trending")) {
            url += "&order[relevance]=desc";
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                    } finally {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        });

        queue.add(jsonObjectRequest);
    }
}
