package com.example.book_library_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Get the contact number passed via intent
        String contactNumber = getIntent().getStringExtra("contactNumber");

        // Find the TextView and set the contact number
        TextView contactNumberTV = findViewById(R.id.contactNumberTV);
        contactNumberTV.setText("Contact Number: " + contactNumber);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // Closes the current activity and returns to the previous one
        });

    }
}
