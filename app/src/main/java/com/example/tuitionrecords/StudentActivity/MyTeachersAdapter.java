package com.example.tuitionrecords.StudentActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTeachersAdapter extends FirebaseRecyclerAdapter<TeacherShowModel,MyTeachersAdapter.MyViewHolder> {

    private final Context mContext;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Teacher_profile");


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyTeachersAdapter(@NonNull FirebaseRecyclerOptions<TeacherShowModel> options, Context context) {
        super(options);
        mContext=context;
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder,final int position, @NonNull TeacherShowModel model) {

        String teacherKey=getRef(position).getKey();
        Log.i("TEACHER_KEY",teacherKey);
        assert teacherKey != null;
        reference.child(teacherKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String about= Objects.requireNonNull(snapshot.child("about").getValue()).toString();
                String photoURL = Objects.requireNonNull(snapshot.child("myUri").getValue()).toString();

                holder.name.setText(name);
                holder.about.setText(about);
                Glide.with(mContext).load(photoURL).into(holder.dp);

                holder.itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(mContext, MyTeacherProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("userId", teacherKey);
                    //intent.putExtra("check","Student");
                    mContext.startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_show_card,parent,false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, about;
        CircleImageView dp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            about=itemView.findViewById(R.id.about);
            dp=itemView.findViewById(R.id.profile_photo);

        }
    }
}
