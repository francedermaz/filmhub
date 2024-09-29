package com.example.filmhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Importa MenuItem
import android.view.View; // Importa View
import android.widget.Button; // Importa Button
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide; // Importa Glide
import com.example.filmhub.R;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOverview;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageViewPoster = findViewById(R.id.image_view_poster);
        textViewTitle = findViewById(R.id.text_view_title);
        textViewOverview = findViewById(R.id.text_view_overview);
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String posterPath = intent.getStringExtra("posterPath");

        if (title != null && overview != null && posterPath != null) {
            textViewTitle.setText(title);
            textViewOverview.setText(overview);
            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Glide.with(this).load(imageUrl).into(imageViewPoster);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


