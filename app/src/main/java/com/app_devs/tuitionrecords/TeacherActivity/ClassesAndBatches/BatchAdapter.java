package com.app_devs.tuitionrecords.TeacherActivity.ClassesAndBatches;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_devs.tuitionrecords.R;
import com.app_devs.tuitionrecords.Schedule.ScheduleModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BatchAdapter extends FirebaseRecyclerAdapter<ScheduleModel,BatchAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time_Table").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    String day;

    public BatchAdapter(@NonNull FirebaseRecyclerOptions<ScheduleModel> options, String day) {
        super(options);
        this.day = day;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ScheduleModel model) {
        String refKey = getRef(position).getKey();
        Log.i("OUTER_KEY", refKey);

        holder.time.setText(model.getTiming());
        holder.subject.setText(model.getSubject());
        holder.batch.setText(model.getBatch());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.batch.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to remove it from your timetable ?");
                builder.setCancelable(false);

                builder.setPositiveButton("YES", ((dialogInterface, i) -> {
                    reference.child(day).child(refKey).removeValue().addOnCompleteListener(task -> {
                        Toast.makeText(context, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
                    });
                }));

                builder.setNegativeButton("NO", ((dialogInterface, i) -> dialogInterface.cancel()));
                builder.create().show();
            }
        });

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
        ImageView delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            batch=itemView.findViewById(R.id.batchNum);
            subject=itemView.findViewById(R.id.subjectGet);
            time = itemView.findViewById(R.id.timeGet);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
