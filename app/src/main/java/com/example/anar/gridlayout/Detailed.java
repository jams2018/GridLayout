package com.example.anar.gridlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Detailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Detailed");

        String title = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
        String director = getIntent().getExtras().getString("director");
        int year = getIntent().getExtras().getInt("year");
        String image = getIntent().getExtras().getString("image");

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView main_title = findViewById(R.id.main_title);
        TextView main_description = findViewById(R.id.main_description);
        TextView main_director = findViewById(R.id.main_director);
        TextView main_year = findViewById(R.id.main_year);
        ImageView main_thumbnail = findViewById(R.id.main_thumbnail);

        // set values
        main_title.setText(title);
        main_description.setText(description);
        main_director.setText(director);
        main_year.setText(String.valueOf(year));

        collapsingToolbarLayout.setTitle(title);

        // set image
        Glide.with(this).load(image).into(main_thumbnail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // goto back activity from here

                Intent intent = new Intent(Detailed.this, ActivityTopLeft.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}