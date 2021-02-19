package com.app_devs.tuitionrecords.TeacherActivity.TeacherFee;

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

public class FeeStatusAdapter extends FirebaseRecyclerAdapter<FeeStatusModel,FeeStatusAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Students_Profile");
    //String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

    public FeeStatusAdapter(@NonNull FirebaseRecyclerOptions<FeeStatusModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull FeeStatusModel model) {

        String request_key=getRef(position).getKey();
        Log.i("REQUEST_REF", request_key);

        assert request_key != null;
        reference.child(request_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name  = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String email  = Objects.requireNonNull(snapshot.child("myEmail").getValue()).toString();
                String photoURL = Objects.requireNonNull(snapshot.child("myUri").getValue()).toString();

                holder.name.setText(name);
                holder.email.setText(email);

                if (photoURL.equals("default")) {
                    holder.profilePic.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(context).load(photoURL).into(holder.profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FeeHistory.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("student_uid",request_key);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_status_card,parent,false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
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
