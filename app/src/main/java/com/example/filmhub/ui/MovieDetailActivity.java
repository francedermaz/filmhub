package com.example.filmhub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.filmhub.R;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewOverview;
    private TextView textViewReleaseDate;
    private TextView textViewVoteAverage;
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
        textViewReleaseDate = findViewById(R.id.text_view_release_date);
        textViewVoteAverage = findViewById(R.id.text_view_vote_average);
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
        String releaseDate = intent.getStringExtra("releaseDate");
        String voteAverage = intent.getStringExtra("voteAverage");

        if (title != null && overview != null && posterPath != null) {
            textViewTitle.setText(title);
            textViewOverview.setText(overview);
            String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
            Glide.with(this).load(imageUrl).into(imageViewPoster);
        }

        if (releaseDate != null) {
            textViewReleaseDate.setText("Fecha de lanzamiento: " + releaseDate);
        }

        if (voteAverage != null) {
            textViewVoteAverage.setText("Calificación: " + voteAverage);
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
