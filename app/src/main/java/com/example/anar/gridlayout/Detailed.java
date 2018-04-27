package com.example.anar.gridlayout;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anar.gridlayout.R;

import org.w3c.dom.Text;

public class Detailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        getSupportActionBar().hide();

        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        String director = getIntent().getExtras().getString("director");
        int year = getIntent().getExtras().getInt("year");
        String thumbnail = getIntent().getExtras().getString("image");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView movie_title = findViewById(R.id.main_title);
        TextView movie_description = findViewById(R.id.main_description);
        TextView movie_director = findViewById(R.id.main_director);
        TextView movie_year = findViewById(R.id.main_year);
        ImageView movie_image = findViewById(R.id.main_thumbnail);

        // set values
        movie_title.setText(title);
        movie_description.setText(description);
        movie_director.setText(director);
        movie_year.setText(year);

        collapsingToolbarLayout.setTitle(title);

        // set image
        Glide.with(this).load(thumbnail);
    }
}