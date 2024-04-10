package com.example.agendaassistant;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


// singleton data class to allow for access of user data anywhere in the app
public class AppData extends Application {

    private static AppData dataInstance;
    private JSONArray UserData;

    public AppData() {
        UserData = new JSONArray();
        //dataInstance = new AppData();
    }

    // method to access the appdata object within the application
    public static synchronized AppData getInstance() {
        // if it doesn't already exist, create an instance
        if (dataInstance == null) {
            dataInstance = new AppData();
        }
        return dataInstance;
    }

    public static void initialize(){
        if (dataInstance == null) {
            dataInstance = new AppData();
        }
    }

    public void saveUserData(JSONObject userData, Context context){

        if(userData != null){
            dataInstance.UserData.put(userData);
        }

        //Getting a file path to save.

        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (path != null){
            //Creating the file.
            File file = new File (path, "user_data.json");
            try {
                // create the file output stream, write to file, and then close
                FileOutputStream stream = new FileOutputStream(file, false);
                stream.write(UserData.toString().getBytes());
                stream.write("\n".getBytes());
                stream.close();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
            // reload the data regardless of result
            loadUserData(context);
        }

    }

    public JSONArray loadUserData(Context context){

        File path = context.getExternalFilesDir((Environment.DIRECTORY_DOCUMENTS));
        if (path != null){
            //Instantiating our file.
            File file = new File (path, "user_data.json");
            if (!file.exists()){
                System.out.println("No user data found");
                return null;
            }
            try {
                // open a file input stream and read the data, add every instance of data to the UserData Array
                StringBuilder data = new StringBuilder();

                FileInputStream stream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null){
                    // add each line to the retrieved data string
                    data.append(line);
                }
                // update the UserData object with new data
                dataInstance.UserData = new JSONArray(data.toString());
                stream.close();
            } catch (IOException | JSONException e) {
                System.out.println("Failed to write data to file");
                throw new RuntimeException(e);
            }
        }

        return this.UserData;
    }

    public void setUserData(JSONArray data){
        UserData = data;
    }
    public JSONArray getUserData(){
        return UserData;
    }

}
