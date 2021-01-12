package com.example.tuitionrecords.StudentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentAdapter extends FirebaseRecyclerAdapter<TeacherShowModel, StudentAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StudentAdapter(@NonNull FirebaseRecyclerOptions<TeacherShowModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull TeacherShowModel model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.subject.setText(model.getContent());
        holder.standard.setText(model.getStandard());
        String location = model.getCity() + ", " + model.getState();
        holder.location.setText(location);

        Glide.with(holder.profilePic.getContext()).load(model.getMyUri()).into(holder.profilePic);

        holder.sendRequest.setOnClickListener(view -> {

        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_request_card, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView name, email, standard, subject, location;
        Button sendRequest;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_photo);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            standard = itemView.findViewById(R.id.standard);
            subject = itemView.findViewById(R.id.subject);
            location = itemView.findViewById(R.id.location);

            sendRequest = itemView.findViewById(R.id.send_request);
        }
    }
}
