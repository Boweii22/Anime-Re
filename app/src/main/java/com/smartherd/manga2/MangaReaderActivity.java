package com.smartherd.manga2;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MangaReaderActivity extends AppCompatActivity {
    private List<String> pageUrls = new ArrayList<>();
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_reader);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        webView = findViewById(R.id.manga_web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // For better content compatibility
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // Allow mixed content if needed

        webView.setWebViewClient(new WebViewClient());

        //Get the read url and load it
        String readUrl = "https://mangadex.org/title/" + getIntent().getStringExtra("manga_id");
        Log.d("MangaReaderActivity","ReadUrl: " + readUrl);
        webView.loadUrl(readUrl);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}

