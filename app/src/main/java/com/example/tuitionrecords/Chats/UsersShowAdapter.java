package com.example.tuitionrecords.Chats;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersShowAdapter extends FirebaseRecyclerAdapter<UsersShowModel,UsersShowAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private final Context context;
    String role;
    DatabaseReference reference;


    public UsersShowAdapter(@NonNull FirebaseRecyclerOptions<UsersShowModel> options, Context ctx, String user) {
        super(options);
        context=ctx;
        role=user;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull UsersShowModel model) {
        String request_key=getRef(position).getKey();
        Log.i("REQUEST_REF", request_key);
        if(role.equals("teacher")) {
            reference = FirebaseDatabase.getInstance().getReference().child("Students_Profile");
        }
        else
        {
            reference = FirebaseDatabase.getInstance().getReference().child("Teacher_profile");
        }

        assert request_key != null;
        reference.child(request_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name  = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                String photoURL = Objects.requireNonNull(snapshot.child("myUri").getValue()).toString();

                holder.name.setText(name);

                Glide.with(context).load(photoURL).into(holder.profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_show,parent,false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profilePic;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profile_photo);
            name=itemView.findViewById(R.id.name);
        }
    }

}
