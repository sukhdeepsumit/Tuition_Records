package com.example.tuitionrecords.TeacherActivity.Students;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Objects;

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
                String standard = snapshot.child("myStandard").getValue().toString();
                String photoURL = snapshot.child("myUri").getValue().toString();

                holder.name.setText(name);
                holder.standard.setText(standard);

                Glide.with(mContext).load(photoURL).into(holder.dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, MyStudents.class);
            intent.putExtra("student_key", student_key);
            mContext.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_students_card, parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, standard;
        CircleImageView dp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            standard = itemView.findViewById(R.id.standard);

            dp = itemView.findViewById(R.id.profile_photo);
        }
    }
}
