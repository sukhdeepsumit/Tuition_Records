package com.example.tuitionrecords.TeacherActivity;

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
     * @param reference
     */
    public FeeStatusAdapter(@NonNull FirebaseRecyclerOptions<FeeStatusModel> options, DatabaseReference reference) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull FeeStatusModel model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getMyEmail());
        holder.feeStatus.setText("Not Paid");

        Glide.with(holder.profilePic.getContext()).load(model.getMyUri()).into(holder.profilePic);




        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.update.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_teacher_profile_content))
                        .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                        .create();

                View myView= dialogPlus.getHolderView();

                final TextInputEditText editText=myView.findViewById(R.id.text);

                final AppCompatButton updateFeeStatus=myView.findViewById(R.id.update);

                editText.setText(holder.feeStatus.getText().toString());
                dialogPlus.show();

                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


                updateFeeStatus.setOnClickListener(new View.OnClickListener() {

                    final String result = Objects.requireNonNull(editText.getText()).toString();

                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("Fee_Status").child(uid)
                                .child(Objects.requireNonNull(getRef(position).getKey())).setValue(result)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(holder.update.getContext(),"Record Updated",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialogPlus.dismiss();
                    }
                });


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
        TextView name, email, feeStatus;
        ImageView update;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic=itemView.findViewById(R.id.profile_photo);
            name=itemView.findViewById(R.id.name);
            email=itemView.findViewById(R.id.email);
            feeStatus=itemView.findViewById(R.id.feeStatus);
            update=itemView.findViewById(R.id.feeStatusEdit);
        }
    }
}
