package com.example.tuitionrecords.TeacherActivity.TeacherFee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionrecords.FeeHistoryModel;
import com.example.tuitionrecords.FeeStatusModel;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FeeHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add;
    DatabaseReference reference;
    FeeHistoryAdapter feeHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);
        add=findViewById(R.id.addFee);

        String student_ref =getIntent().getStringExtra("student_uid");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(FeeHistory.this,FeeAdd.class);
               intent.putExtra("student_uid",student_ref);
               startActivity(intent);
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String user= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Fee_Status").child(user).child(student_ref);

        FirebaseRecyclerOptions<FeeHistoryModel> options =
                new FirebaseRecyclerOptions.Builder<FeeHistoryModel>()
                        .setQuery(reference,FeeHistoryModel.class)
                        .build();

        feeHistoryAdapter=new FeeHistoryAdapter(options,FeeHistory.this,student_ref);
        recyclerView.setAdapter(feeHistoryAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        feeHistoryAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        feeHistoryAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FeeHistory.this,FeeStatus.class));
        finish();
    }
}