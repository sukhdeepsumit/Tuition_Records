package com.example.tuitionrecords.TeacherActivity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.ScheduleModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


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
     holder.subject.setText(model.getSubject());
     holder.batch.setText(model.getBatch());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bacth,parent,false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView batch,subject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            batch=itemView.findViewById(R.id.batchNum);
            subject=itemView.findViewById(R.id.subjectGet);
        }
    }
}
