package com.example.agendaassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        // initialize user data on first app launch
        AppData.initialize();

    }

    // set fragment references
    HomeScreen homeScreen = new HomeScreen();
    TaskView taskView = new TaskView();
    TaskCreation taskCreate = new TaskCreation();

    // handle fragment switching in the app
    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.home){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeScreen)
                    .commit();
            return true;
        }else if(id == R.id.taskView){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, taskView)
                    .commit();
            return true;
        }else if(id == R.id.taskCreate){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, taskCreate)
                    .commit();
            return true;
        }

        return false;
    }

}