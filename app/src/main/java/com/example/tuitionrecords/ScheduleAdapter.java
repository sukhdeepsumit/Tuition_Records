package com.example.tuitionrecords;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ScheduleAdapter extends FirebaseRecyclerAdapter<ScheduleModel, ScheduleAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Time_Table").child(currentUser);
    public ScheduleAdapter(@NonNull FirebaseRecyclerOptions<ScheduleModel> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ScheduleModel model) {
        Log.i("GET_REF_CHECK", getRef(position).getKey());
        String refKey = getRef(position).getKey();

        reference.child(refKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    holder.time.setText(dataSnapshot.child("timing").getValue().toString());
                    holder.subject.setText(dataSnapshot.child("subject").getValue().toString());
                    holder.batch.setText(dataSnapshot.child("batch").getValue().toString());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });


        /*reference.child(refKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("SNAPSHOT_KEY", snapshot.getKey());

                for (DataSnapshot snap : snapshot.getChildren()) {
                    String key = snap.getKey();
                    Log.i("SNAP_KEY", snap.getKey());
                    assert key != null;
                    *//*time = snapshot.child(key).child("timing").getValue().toString();
                    subject = snapshot.child(key).child("subject").getValue().toString();
                    batch = "Batch No. " + snapshot.child(key).child("batch").getValue().toString();*//*

                    holder.time.setText(snapshot.child(key).child("timing").getValue().toString());
                    holder.subject.setText(snapshot.child(key).child("subject").getValue().toString());
                    holder.batch.setText("Batch No. " + snapshot.child(key).child("batch").getValue().toString());
                }
                //notifyDataSetChanged();

                String count = String .valueOf(snapshot.getChildrenCount());
                Log.i("SNAP_COUNT", count);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });*/
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView time, subject, batch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timing);
            subject = itemView.findViewById(R.id.subject);
            batch = itemView.findViewById(R.id.batch);
        }
    }
}
