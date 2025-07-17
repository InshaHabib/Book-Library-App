package com.example.book_library_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class BuyBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_book);

        // Find Views
        ImageView ivBookImage = findViewById(R.id.ivBookImage);
        TextView tvBookPrice = findViewById(R.id.tvBookPrice);
        TextView tvBookRating = findViewById(R.id.tvBookRating);
        Button btnBuyNow = findViewById(R.id.btnBuyNow);

        // Get Data from Intent
        String bookPrice = getIntent().getStringExtra("price");
        String bookRating = getIntent().getStringExtra("rating");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Set Data to Views
        tvBookPrice.setText("Price: " + bookPrice);
        tvBookRating.setText("Rating: " + bookRating);

        // Load the image using Picasso
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(ivBookImage);
        } else {
            ivBookImage.setImageResource(R.drawable.img); // Default image
        }

        // Buy Button Click Listener
        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(BuyBook.this, "Back to Search Page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BuyBook.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
