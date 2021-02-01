package com.example.tuitionrecords.TeacherActivity.TeacherBatches;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.Schedule.ScheduleModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;


public class BatchAdapter extends FirebaseRecyclerAdapter<ScheduleModel,BatchAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public BatchAdapter(@NonNull FirebaseRecyclerOptions<ScheduleModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ScheduleModel model) {
        String refKey = getRef(position).getKey();
        Log.i("OUTER_KEY", refKey);

        holder.time.setText(model.getTiming());
        holder.subject.setText(model.getSubject());
        holder.batch.setText(model.getBatch());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.batch,parent,false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView batch,subject, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            batch=itemView.findViewById(R.id.batchNum);
            subject=itemView.findViewById(R.id.subjectGet);
            time = itemView.findViewById(R.id.timeGet);
        }
    }
}
