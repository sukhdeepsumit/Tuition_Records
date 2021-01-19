package com.example.tuitionrecords.TeacherActivity.Students;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.StudentModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyStudentsAdapter extends FirebaseRecyclerAdapter<StudentModel, MyStudentsAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Students_Profile");

    public MyStudentsAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull StudentModel model) {

        String student_key = getRef(position).getKey();
        Log.i("STUDENT_KEY", student_key);

        reference.child(student_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String email = snapshot.child("myEmail").getValue().toString();
                String gender = snapshot.child("myGender").getValue().toString();
                String location = snapshot.child("myCity").getValue().toString() + ", " + snapshot.child("myState").getValue().toString();
                String standard = snapshot.child("myStandard").getValue().toString();
                String photoURL = snapshot.child("myUri").getValue().toString();

                holder.name.setText(name);
                holder.email.setText(email);
                holder.gender.setText(gender);
                holder.location.setText(location);
                holder.standard.setText(standard);

                Glide.with(context).load(photoURL).into(holder.dp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_students, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, email, gender, location, standard, about, batch;
        CircleImageView dp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            gender = itemView.findViewById(R.id.gender);
            location = itemView.findViewById(R.id.location);
            standard = itemView.findViewById(R.id.standard);
            about = itemView.findViewById(R.id.about);
            batch = itemView.findViewById(R.id.batch);

            dp = itemView.findViewById(R.id.profile_photo);
        }
    }
}
