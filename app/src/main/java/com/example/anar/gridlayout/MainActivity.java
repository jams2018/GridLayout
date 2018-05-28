package com.example.anar.gridlayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.widget.GridLayout;
import android.widget.Toast;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MESSAGE = "com.example.gridlayout.MESSAGE";
    private static final String TAG = "MainActivity";
    private SharedPreferencesHelper mSharedPreferencesHelper;

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate() is called");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Instantiate a SharedPreferencesHelper class
        SharedPreferences mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        mSharedPreferencesHelper = new SharedPreferencesHelper(mSharedPreferences);

        // populate text field w/ saved entry
        EditText editText = findViewById(R.id.editText);
        editText.setText(mSharedPreferencesHelper.getEntry());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainGrid = findViewById(R.id.mainGrid);

        //Event set
        setSingleEvent(mainGrid);
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item from Main Grid
        for(int i = 0; i<mainGrid.getChildCount();i++)
        {
            CardView cardView = (CardView)mainGrid.getChildAt(i);
            final int order = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (order == 0) // Open activity one
                    {
                        Intent intent = new Intent(MainActivity.this, ActivityTopLeft.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Toast" + order, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast toast = Toast.makeText(MainActivity.this, "Setting option menu!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
            toast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_menu) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }

        if (id == R.id.movie_menu) {
            Intent intent = new Intent(this, ActivityTopLeft.class);
            startActivity(intent);
        }

        if (id == R.id.camera_list) {
            Intent intent = new Intent(this, CameraList.class);
            if (isNetworkAvailable(MainActivity.this)) //returns true if internet available
            {
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "No Connection!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 16);
                toast.show();
            }
        }

        if (id == R.id.maps) {
            Intent intent = new Intent(this, MapActivity.class);
            if (isNetworkAvailable(MainActivity.this)) //returns true if internet available
            {
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "No Connection!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 16);
                toast.show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();

        if (inputIsValid(message)) {
            mSharedPreferencesHelper.saveEntry(message);
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    public boolean inputIsValid(String str) {
        return str.length() != 0;
    }

    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called...");
    }
}
