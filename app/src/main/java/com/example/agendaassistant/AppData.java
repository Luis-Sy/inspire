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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/** Singleton data class to allow for access of user data anywhere in the app
* <p>Loads and holds user data within the UserData JSONArray object</p>
* <p>Static dataInstance ensures it is initialized only once when the app runs and data
 *  remains consistent throughout the entire app</p>
* @author Luis Marlou Sy
* */
public class AppData extends Application {

    private static AppData dataInstance;
    private JSONArray UserData;

    public AppData() {
        UserData = new JSONArray();
        //dataInstance = new AppData();
    }

    /** Method to access the appdata object within the application
     *
     * @author Luis Marlou Sy
     * @return dataInstance the current loaded instance
     * */
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

    /** Method add a task object to the stored data object
     *
     * @author Luis Marlou Sy
     * */
    public void saveUserData(JSONObject userData, Context context) throws JSONException, ParseException {

        if(userData != null){
            UserData.put(userData);

        }

        // write to file and update userData
        updateUserData(context);

    }

    /** method that loads the user data file
     * <p>returns the UserData object as a JSONArray</p>
     *
     * @author Luis Marlou Sy
     * @param context the application's context
     * @return UserData (A static JSONArray object within the AppData class)
     * */
    public JSONArray loadUserData(Context context) throws JSONException {

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
                if(!data.toString().equals("")){
                    UserData = new JSONArray(data.toString());
                }
                stream.close();
                // remove any expired tasks before committing update to UserData
                removeExpiredTasks();

            } catch (IOException | JSONException | ParseException e) {
                System.out.println("Failed to write data to file");
                throw new RuntimeException(e);
            }
        }

        return this.UserData;
    }


    /** Method that writes the AppData Class' user data to file
     *
     * @author Luis Marlou Sy
     * */
    public void updateUserData(Context context) throws JSONException, ParseException {

        //Getting a file path to save.

        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (path != null){
            //Creating the file.
            File file = new File (path, "user_data.json");
            try {
                // create the file output stream, write to file, and then close
                FileOutputStream stream = new FileOutputStream(file, false);
                //removeExpiredTasks();
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

    /** Method that marks a task from the UserData JSONArray as complete
     *<p>Done by marking the "task_completed" property of a task inside UserData to "true"</p>
     *
     * @author Luis Marlou Sy
     * @param id The id of the task inside UserData to mark as complete
     * */
    public void completeTask(int id, Context context) {

        // update the
        try{
            UserData.getJSONObject(id).put("task_completed", true);
            updateUserData(context);

        }catch (JSONException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /** Method that removes all tasks that are past their deadline
     * <p>Done by iterating through the UserData JSONArray and removing them</p>
     *
     * @author Luis Marlou Sy
     * */
    public void removeExpiredTasks() throws JSONException, ParseException {

        System.out.println("trying to remove expired tasks");
        if(UserData != null && UserData.length() > 0){
            System.out.println("removing expired tasks");
            // get the current date
            Date currentDate = new Date();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            JSONArray updatedData = new JSONArray();

            // iterate through array, remove all expired tasks
            for(int i = 0; i < UserData.length(); i++){

                JSONObject task = UserData.getJSONObject(i);
                Date deadline = sdf.parse(task.getString("task_deadline"));
                System.out.println(sdf.format(deadline));
                System.out.println(sdf.format(currentDate));

                // if the task's deadline is after the current date, do not remove it

                if(deadline != null && deadline.after(currentDate)){
                    System.out.println("task " + i + " has not expired");
                    updatedData.put(task);
                }else{
                    System.out.println("task " + i + " has expired, removing from UserData");
                }


            }

            // update UserData with the new array
            System.out.println("tasks removed, updating data");
            UserData = updatedData;
        }


    }


    /** Method that gets the number of incomplete tasks and returns the count
     *
     * @author Luis Marlou Sy
     * @return int (Returns number of items contained in UserData. Returns 0 if empty or null)
     * */
    public int getNumberOfIncompleteTasks(){

        //
        if(UserData != null && UserData.length() > 0){
            int count = 0;

            for (int i = 0; i < UserData.length(); i++) {
                try {
                    if(!UserData.getJSONObject(i).getBoolean("task_completed")){
                        count++;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            return count;
        }

        return 0;
    }


}
