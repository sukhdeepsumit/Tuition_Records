package com.app_devs.tuitionrecords.TeacherActivity.TeacherFee;

import android.content.Context;
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

import com.app_devs.tuitionrecords.FeeHistoryModel;
import com.app_devs.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Objects;

public class FeeHistoryAdapter extends FirebaseRecyclerAdapter<FeeHistoryModel,FeeHistoryAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private final Context mContext;
    private String student_uid;

    public FeeHistoryAdapter(@NonNull FirebaseRecyclerOptions<FeeHistoryModel> options, Context context, String mStudent_uid) {
        super(options);
        mContext=context;
        student_uid=mStudent_uid;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FeeHistoryModel model) {

        String ref_key=getRef(position).getKey();

            holder.feeAmount.setText(model.getAmount());
            holder.payDate.setText(model.getDate());

            holder.feeUpdate.setOnClickListener(v -> {
                final DialogPlus dialogPlus = DialogPlus.newDialog(mContext)
                        .setContentHolder(new ViewHolder(R.layout.update_teacher_profile_content))
                        .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                        .create();

                View myView = dialogPlus.getHolderView();
                final TextInputEditText editText = myView.findViewById(R.id.text);
                final AppCompatButton update = myView.findViewById(R.id.update);

                editText.setText(holder.feeAmount.getText().toString());
                dialogPlus.show();

                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                update.setOnClickListener(v1 -> {
                    String amount_edit=editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference("Fee_Status").child(uid).child(student_uid).child(ref_key)
                            .child("amount").setValue(amount_edit).addOnCompleteListener(task -> {
                            Toast.makeText(mContext,"Record Updated",Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                    });
                });
            });

            holder.dateUpdate.setOnClickListener(view -> {
                final DialogPlus dialogPlus = DialogPlus.newDialog(mContext)
                        .setContentHolder(new ViewHolder(R.layout.update_teacher_profile_content))
                        .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                        .create();

                View myView = dialogPlus.getHolderView();
                final TextInputEditText editText = myView.findViewById(R.id.text);
                final AppCompatButton update = myView.findViewById(R.id.update);

                editText.setText(holder.payDate.getText().toString());
                dialogPlus.show();

                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                update.setOnClickListener(v -> {
                    String date_edit=editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference("Fee_Status").child(uid).child(student_uid).child(ref_key).child("date").setValue(date_edit).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext,"Record Updated",Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        }
                    });
                });
            });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_history_card_for_teacher,parent,false);
        return new MyViewHolder(view);
    }

    static  class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView feeAmount, payDate;
        ImageView feeUpdate, dateUpdate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            feeAmount=itemView.findViewById(R.id.amount);
            payDate=itemView.findViewById(R.id.datePay);
            feeUpdate=itemView.findViewById(R.id.amountEdit);
            dateUpdate=itemView.findViewById(R.id.dateEdit);
        }
    }
}
