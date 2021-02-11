package com.example.tuitionrecords.Schedule;

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

import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

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

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ScheduleModel model) {
        Log.i("GET_REF_CHECK", getRef(position).getKey());
        String refKey = getRef(position).getKey();

        String hour = model.getTiming();
        holder.time.setText(hour);
        holder.subject.setText(model.getSubject());
        holder.batch.setText("Batch No. " + model.getBatch());

        Date date = Calendar.getInstance().getTime();
        String time = String.valueOf(date);
        int current = Integer.parseInt(time.substring(11,13));

        int first = Integer.parseInt(hour.substring(0,2));
        int last = Integer.parseInt(hour.substring(5,7));

        if (hour.contains("AM")) {
            if (first > current) {
                holder.status.setText("Upcoming");
                holder.status.setTextColor(Color.parseColor("#6EC72D"));
            }
            else if(last < current) {
                holder.status.setText("Completed");
                holder.status.setTextColor(Color.parseColor("#758283"));
            }
            else {
                holder.status.setText("Ongoing");
                holder.status.setTextColor(Color.parseColor("#2196F3"));
            }
        }
        else {
            if (first != 10 && first != 11 && first != 12) {
                first += 12;
            }
            if (last != 12) {
                last += 12;
            }

            if (first > current) {
                holder.status.setText("Upcoming");
                holder.status.setTextColor(Color.parseColor("#6EC72D"));
            }
            else if (last < current) {
                holder.status.setText("Completed");
                holder.status.setTextColor(Color.parseColor("#E03B8B"));
            }
            else {
                holder.status.setText("Ongoing");
                holder.status.setTextColor(Color.parseColor("#2196F3"));
            }
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView time, subject, batch, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.timing);
            subject = itemView.findViewById(R.id.subject);
            batch = itemView.findViewById(R.id.batch);
            status = itemView.findViewById(R.id.status);
        }
    }
}
