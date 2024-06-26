package com.example.agendaassistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/** Adapter for the RecyclerView used in the TaskView Class
 *
 *
 * @author Luis Marlou Sy
 * */

public class taskDataAdapter extends RecyclerView.Adapter<taskDataAdapter.ViewHolder> {


    private static JSONArray user_data;
    private final RecyclerView recyclerView;

    public taskDataAdapter(JSONArray userData, RecyclerView recyclerView) {
        user_data = userData;
        this.recyclerView = recyclerView;
        System.out.println(user_data);
        System.out.println(getItemCount());
    }
    @NonNull
    @Override
    public taskDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_data_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull taskDataAdapter.ViewHolder holder, int position) {

        try {
            JSONObject task = user_data.getJSONObject(position);

            holder.taskNameView.setText(task.getString("task_name"));
            holder.taskDescriptionView.setText(task.getString("task_description"));
            holder.taskDeadlineView.setText(task.getString("task_deadline"));

            boolean completed = task.getBoolean("task_completed");

            holder.taskCompletionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // hack fix to ensure the adapter position is never -1
                    int index = holder.getAdapterPosition();
                    if(index < 0){
                    index = 0;
                }
                    AppData.getInstance().completeTask(index, holder.itemView.getContext());
                    notifyDataSetChanged();
                }
            });

            // enable/disable taskCompletionButton if the task is already completed or not

            if(completed){
                holder.taskCompletionStatusView.setText(R.string.task_completion_status_complete);
                holder.taskCompletionButton.setText(R.string.task_view_data_button_complete);
                holder.taskCompletionButton.setEnabled(false);
                holder.taskCompletionButton.setOnClickListener(null);
            }else{
                holder.taskCompletionStatusView.setText(R.string.task_completion_status_incomplete);
                holder.taskCompletionButton.setText(R.string.task_view_data_button_incomplete);
                holder.taskCompletionButton.setEnabled(true);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public int getItemCount() {
        if(user_data == null){
            return 0;
        }
        return user_data.length();
    }

    //Declaration of our viewHolder, which extends the original one.
    class ViewHolder extends RecyclerView.ViewHolder {

        //Declaration of view elements used on the recycles.
        TextView taskNameView, taskDescriptionView, taskDeadlineView, taskCompletionStatusView;
        Button taskCompletionButton;

        //When the recycles is constructed:
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Assign the views to their objects.

            System.out.println(getAdapterPosition());

            taskNameView = itemView.findViewById(R.id.taskNameDataText);
            taskDescriptionView = itemView.findViewById(R.id.taskDescriptionDataText);
            taskDeadlineView = itemView.findViewById(R.id.taskDeadlineDataText);
            taskCompletionStatusView = itemView.findViewById(R.id.taskCompletionStatusDataText);

            taskDescriptionView.setEnabled(false);

            taskCompletionButton = itemView.findViewById(R.id.taskCompletionButton);


        }


    }
}
