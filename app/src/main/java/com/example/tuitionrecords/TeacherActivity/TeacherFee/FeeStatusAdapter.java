package com.example.tuitionrecords.TeacherActivity.TeacherFee;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.FeeStatusModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.CheckTeacherProfile;
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

public class FeeStatusAdapter extends FirebaseRecyclerAdapter<FeeStatusModel,FeeStatusAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private final Context context;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Students_Profile");
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

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


                Glide.with(context).load(photoURL).into(holder.profilePic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

//        FirebaseDatabase.getInstance().getReference("Fee_Status").child(uid).child(request_key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String status=Objects.requireNonNull(snapshot.child("feeStatus").getValue()).toString();
//                holder.feeStatus.setText(status);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        holder.update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.update.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.update_teacher_profile_content))
//                        .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
//                        .create();
//
//                View myView= dialogPlus.getHolderView();
//
//                final TextInputEditText editText=myView.findViewById(R.id.text);
//                final AppCompatButton updateFeeStatus=myView.findViewById(R.id.update);
//
//                editText.setText(holder.feeStatus.getText().toString());
//                dialogPlus.show();
//
//                updateFeeStatus.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        String result=editText.getText().toString();
//
//                        FirebaseDatabase.getInstance().getReference("Fee_Status").child(uid).child(request_key).child("feeStatus").setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful())
//                                {
//                                    FirebaseDatabase.getInstance().getReference("Fee_Status").child(request_key).child(uid).child("feeStatus").setValue(result).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task1) {
//                                            Toast.makeText(context,"Record Updated",Toast.LENGTH_SHORT).show();
//                                            dialogPlus.dismiss();
//
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    }
//                });
//
//
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeeHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
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
