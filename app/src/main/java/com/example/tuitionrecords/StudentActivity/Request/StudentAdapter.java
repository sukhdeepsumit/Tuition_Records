package com.example.tuitionrecords.StudentActivity.Request;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    /*String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref, requests;
    String CURRENT_STATE = "Not Teacher";
    String receiver, sender;*/

    private final Context context;

    public StudentAdapter(@NonNull FirebaseRecyclerOptions<TeacherShowModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull TeacherShowModel model) {
        holder.name.setText(model.getName());
        holder.about.setText(model.getAbout());

        if (model.getMyUri().equals("default")) {
            holder.profilePic.setImageResource(R.drawable.anonymous_user);
        }
        else {
            Glide.with(holder.profilePic.getContext()).load(model.getMyUri()).into(holder.profilePic);
        }

        /*requests = FirebaseDatabase.getInstance().getReference("Requests");
        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();*/

        holder.itemView.setOnClickListener(view -> {
            String userid = getRef(position).getKey();
            Intent intent = new Intent(context, CheckTeacherProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", userid);
            intent.putExtra("check","StudentFromSendRequest");
            context.startActivity(intent);
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
        TextView name, about;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_photo);

            name = itemView.findViewById(R.id.name);
            about = itemView.findViewById(R.id.about);
        }
    }
}
