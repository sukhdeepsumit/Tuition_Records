package com.app_devs.tuitionrecords.StudentActivity.FeeStatus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app_devs.tuitionrecords.FeeHistoryModel;
import com.app_devs.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class FeeHistoryStudentAdapter extends FirebaseRecyclerAdapter<FeeHistoryModel,FeeHistoryStudentAdapter.MyViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeeHistoryStudentAdapter(@NonNull FirebaseRecyclerOptions<FeeHistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FeeHistoryModel model) {
        holder.feeAmount.setText(model.getAmount());
        holder.payDate.setText(model.getDate());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fee_history_card,parent,false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView feeAmount, payDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            feeAmount=itemView.findViewById(R.id.amount);
            payDate=itemView.findViewById(R.id.datePay);
        }
    }
}
