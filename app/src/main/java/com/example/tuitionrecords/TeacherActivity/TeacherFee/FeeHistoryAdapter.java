package com.example.tuitionrecords.TeacherActivity.TeacherFee;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuitionrecords.FeeHistoryModel;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FeeHistoryAdapter extends FirebaseRecyclerAdapter<FeeHistoryModel,FeeHistoryAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Fee_Status").child(currentUser);

    public FeeHistoryAdapter(@NonNull FirebaseRecyclerOptions<FeeHistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FeeHistoryModel model) {
        String refKey = getRef(position).getKey();
        Log.i("CHECK_KEY", refKey);
        reference.child(refKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    String key = snap.getKey();
                    Log.i("INNER_KEY",key);
                    holder.feeAmount.setText(snap.child(key).child("amount").getValue().toString());
                    holder.payDate.setText(snap.child(key).child("date").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_history_card,parent,false);
        return new MyViewHolder(view);
    }

    static  class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView feeAmount, payDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            feeAmount=itemView.findViewById(R.id.amount);
            payDate=itemView.findViewById(R.id.datePay);
        }
    }
}
