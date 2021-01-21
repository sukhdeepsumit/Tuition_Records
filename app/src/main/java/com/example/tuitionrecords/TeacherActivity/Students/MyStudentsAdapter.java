package com.example.tuitionrecords.TeacherActivity.Students;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.StudentModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    private final Context mContext;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Students_Profile");
    DatabaseReference accepted = FirebaseDatabase.getInstance().getReference("Accepted_Students");

    public MyStudentsAdapter(@NonNull FirebaseRecyclerOptions<StudentModel> options, Context context) {
        super(options);
        mContext = context;
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

                Glide.with(mContext).load(photoURL).into(holder.dp);

                holder.remove.setOnClickListener(view -> removeStudent(student_key));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    private void removeStudent(String student_key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Remove");
        builder.setMessage("Do you want to remove this student from your list? ");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", (dialogInterface, i) -> accepted.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(student_key).removeValue()
                .addOnCompleteListener(task -> accepted.child(student_key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()
                        .addOnCompleteListener(task1 -> {
                            Toast.makeText(mContext, "Removed from your student list", Toast.LENGTH_SHORT).show();
                        })
                )
        );

        builder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());

        builder.create().show();
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
        AppCompatButton remove;

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
            remove = itemView.findViewById(R.id.remove);
        }
    }
}
