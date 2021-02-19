package com.app_devs.tuitionrecords.StudentActivity.FeeStatus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app_devs.tuitionrecords.FeeStatusModel;
import com.app_devs.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeeStatusStudentAdapter extends FirebaseRecyclerAdapter<FeeStatusModel,FeeStatusStudentAdapter.MyViewHolder> {

    private final Context mContext;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Teacher_profile");

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeeStatusStudentAdapter(@NonNull FirebaseRecyclerOptions options, Context context) {
        super(options);
        mContext=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FeeStatusModel model) {
        String request_key=getRef(position).getKey();
        Log.i("REQUEST_REF", request_key);

        assert request_key != null;
        reference.child(request_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name  = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String email  = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                String photoURL = Objects.requireNonNull(snapshot.child("myUri").getValue()).toString();

                holder.name.setText(name);
                holder.email.setText(email);

                if (photoURL.equals("default")) {
                    holder.profilePic.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(mContext).load(photoURL).into(holder.profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FeeHistoryStudent.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("teacher_uid",request_key);
                mContext.startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_status_card,parent,false);
        return new MyViewHolder(view);
    }


    static  class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profilePic;
        TextView name, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profile_photo);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
        }
    }
}
