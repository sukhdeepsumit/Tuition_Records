package com.example.tuitionrecords.TeacherActivity.TeacherBatches;


import android.annotation.SuppressLint;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BatchAdapter extends FirebaseRecyclerAdapter<ScheduleModel,BatchAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time_Table").child(currentUser);


    public BatchAdapter(@NonNull FirebaseRecyclerOptions<ScheduleModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ScheduleModel model) {
        String refKey = getRef(position).getKey();
        Log.i("OUTER_KEY", refKey);
        reference.child(refKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String key = snap.getKey();
                    Log.i("BATCH_KEY", key);
                    holder.subject.setText(snapshot.child(key).child("subject").getValue().toString());
                    holder.batch.setText("Batch No. " + snapshot.child(key).child("batch").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
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
