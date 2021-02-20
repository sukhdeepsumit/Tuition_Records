package com.app_devs.tuitionrecords.Schedule;

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

import com.app_devs.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Calendar;
import java.util.Date;

public class ScheduleAdapter extends FirebaseRecyclerAdapter<ScheduleModel, ScheduleAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ScheduleAdapter(@NonNull FirebaseRecyclerOptions<ScheduleModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ScheduleModel model) {
        Log.i("GET_REF_CHECK", getRef(position).getKey());
        //String refKey = getRef(position).getKey();

        String hour = model.getTiming();
        holder.time.setText(hour);
        holder.subject.setText(model.getSubject());
        holder.batch.setText("Batch No. " + model.getBatch());

        Date date = Calendar.getInstance().getTime();
        String time = String.valueOf(date);

        Log.i("TIME_CURRENT_CHECK", time);

        int current = Integer.parseInt(time.substring(11,13));

        int first = Integer.parseInt(hour.substring(0,2));
        int last = Integer.parseInt(hour.substring(5,7));

        if (hour.contains("PM")) {
            if (first != 10 && first != 11 && first != 12) {
                first += 12;
            }
            if (last != 12) {
                last += 12;
            }
        }

        statusColor(holder, first, last, current);
    }

    private void statusColor(@NonNull MyViewHolder holder, int first, int last, int current) {
        if (first > current) {
            holder.status.setText("Upcoming");
            holder.status.setTextColor(Color.parseColor("#6EC72D"));
        }
        else if (last <= current) {
            holder.status.setText("Completed");
            holder.status.setTextColor(Color.parseColor("#E6425E"));
        }
        else {
            holder.status.setText("Ongoing");
            holder.status.setTextColor(Color.parseColor("#2196F3"));
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
