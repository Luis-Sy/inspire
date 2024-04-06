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
    }

    // set fragment references
    HomeScreen homeScreen = new HomeScreen();
    TaskView taskView = new TaskView();

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
        }

        return false;
    }


    //Declaration of class for fetching data from the quotable API.
    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        //Required method for doing an Async Task.
        protected String doInBackground(String... urls) {
            //Use HttpURLConnection object for handling the connection.
            HttpURLConnection urlConnection = null;
            //Use BufferedReader to handle the information got online.
            BufferedReader reader = null;
            //Use String to hold the information.
            String data = null;

            try {
                //New url used for holding the URL address.
                URL url = new URL(urls[0]);
                //Join the URL with the urlConnection to open the connection.
                urlConnection = (HttpURLConnection) url.openConnection();
                //Set method for request, as per API calls.
                urlConnection.setRequestMethod("GET");
                //Connect.
                urlConnection.connect();

                //Once connected, our InputStream is used to read whatever is on the other side.
                InputStream inputStream = urlConnection.getInputStream();
                //From that, we use a StringBuilder to effectively build our data.
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) { //We declare some nulls along the way to inform the onPostExecute in case there is nothing to be read.
                    // Nothing to do.

                    return null;
                }
                //Instantiate our BufferedReader.
                reader = new BufferedReader(new InputStreamReader(inputStream));

                //Declare a line to hold information temporarily.
                String line;
                //Read each line and do something until we get a null line.
                while ((line = reader.readLine()) != null) {
                    //Append the line read into our Builder, alongside a line break.
                    //System.out.println("getting data");
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    //System.out.println("No data");
                    return null;
                }
                //At the end, we throw in the data to our String.
                data = buffer.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                //Finally, we break our connections if they weren't null at first.
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        //And close our reader.
                        reader.close();
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            return data;
        }


        //The following method will execute immediately after the previous task.
        @Override
        public void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    //Create a json object to store the data.
                    JSONArray jsonArray = new JSONArray(jsonData);
                    //Pass that data to a string.
                    String title = jsonArray.getJSONObject(0).getString("content")
                            + "\n-" + jsonArray.getJSONObject(0).getString("author");
                    System.out.println(jsonArray);

                    //Display the fetched data in TextView.

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}