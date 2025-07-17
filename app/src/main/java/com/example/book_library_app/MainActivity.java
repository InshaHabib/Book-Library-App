package com.example.book_library_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private RequestQueue requestQueue;
    private ArrayList<BookInfo> bookInfoList;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar and Drawer Setup
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_prebuybook) {
                Toast.makeText(MainActivity.this, "History of Purchased Book", Toast.LENGTH_SHORT).show();

                // Redirect to MainActivity Page
                Intent intent = new Intent(MainActivity.this, BuyBook.class);
                startActivity(intent);

            } else if (id == R.id.nav_profile) {
                Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_tools) {
                Toast.makeText(MainActivity.this, "Tools", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                // Logout Logic
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                // Redirect to Login Page (Optional)
                Intent intent = new Intent(MainActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Close the current activity
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        // Book Search and Fetching Setup
        progressBar = findViewById(R.id.idLoadingPB);
        searchEditText = findViewById(R.id.idEdtSearchBooks);
        searchButton = findViewById(R.id.idBtnSearch);

        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (query.isEmpty()) {
                searchEditText.setError("Please enter a search query");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            fetchBooks(query);
        });
    }

    private void fetchBooks(String query) {
        bookInfoList = new ArrayList<>();
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }

        requestQueue.getCache().clear();

//        google book url apis:-
        String url = "" + query;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                this::parseResponse,
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(request);
    }

    private void parseResponse(JSONObject response) {
        progressBar.setVisibility(View.GONE);
        try {
            JSONArray items = response.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                String title = volumeInfo.optString("title");
                String subtitle = volumeInfo.optString("subtitle");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                String publisher = volumeInfo.optString("publisher");
                String publishedDate = volumeInfo.optString("publishedDate");
                String description = volumeInfo.optString("description");
                int pageCount = volumeInfo.optInt("pageCount");
                String thumbnail = volumeInfo.optJSONObject("imageLinks") != null ?
                        volumeInfo.getJSONObject("imageLinks").optString("thumbnail") : "";
                String previewLink = volumeInfo.optString("previewLink");
                String infoLink = volumeInfo.optString("infoLink");
                String buyLink = item.optJSONObject("saleInfo") != null ?
                        item.getJSONObject("saleInfo").optString("buyLink") : "";

                ArrayList<String> authors = new ArrayList<>();
                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        authors.add(authorsArray.optString(j));
                    }
                }

                bookInfoList.add(new BookInfo(title, subtitle, authors, publisher, publishedDate, description, pageCount, thumbnail, previewLink, infoLink, buyLink));
            }

            setupRecyclerView();
        } catch (JSONException e) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.idRVBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BookAdapter(bookInfoList, this));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
