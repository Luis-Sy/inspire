package com.example.agendaassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/** Fragment that handles task creation by the user
 *
 * @author Luis Marlou Sy
 * */
public class TaskCreation extends Fragment {


    EditText taskName, taskDeadline, taskDescription;
    Button createTaskBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_task_creation, container, false);

        taskName = returnView.findViewById(R.id.taskNameText);
        taskDeadline = returnView.findViewById(R.id.taskDeadlineText);
        taskDescription = returnView.findViewById(R.id.taskDescriptionText);
        createTaskBtn = returnView.findViewById(R.id.createTaskBtn);

        createTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });

        AppData.getInstance();

        return returnView;
    }

    /** Method that creates a task using input from the task creation fragment
     * <p>Verifies both Task Name and Task Deadline for valid input</p>
     * <p>Task description is optional</p>
     *
     * @author Luis Marlou Sy
     * */
    public void createTask(){

        // validate task data before writing it to file

        if(taskName.getText().toString().equals("")){

            // no valid task name
            taskName.requestFocus();
            Toast.makeText(this.getContext(), "Please enter a task name",Toast.LENGTH_SHORT).show();

        }else if(!isValidDateFormat(taskDeadline.getText().toString(), "dd/MM/yyyy")){

            // no valid task deadline
            taskDeadline.requestFocus();
            Toast.makeText(this.getContext(), "Please enter a valid deadline",Toast.LENGTH_SHORT).show();

        }
        else{

            JSONObject data = new JSONObject();

            try{


                data.put("task_name", taskName.getText());
                data.put("task_deadline", taskDeadline.getText());
                data.put("task_description", taskDescription.getText());
                data.put("task_completed", false);

                AppData.getInstance().saveUserData(data, getContext());
                Toast.makeText(this.getContext(), "Task successfully created",Toast.LENGTH_SHORT).show();

                taskName.setText("");
                taskDeadline.setText("");
                taskDescription.setText("");

            }catch (JSONException | ParseException e){
                System.out.println("Could not create data");
                Toast.makeText(this.getContext(), "Could not create task",Toast.LENGTH_SHORT).show();

            }
        }


    }

    /** Method that verifies the user's entered date before createTask() makes a call to AppData
     *
     * @author Luis Marlou Sy
     * @return boolean
     * */
    private boolean isValidDateFormat(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false); // Set lenient to false to strictly enforce the format

        try {
            // Attempt to parse the date string
            sdf.parse(dateString);
            return true; // If parsing succeeds, the date format is valid
        } catch (ParseException e) {
            return false; // If parsing fails, the date format is invalid
        }
    }
}