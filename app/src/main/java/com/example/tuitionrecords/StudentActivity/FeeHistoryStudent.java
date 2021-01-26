package com.example.tuitionrecords.StudentActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.FeeHistoryModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.TeacherFee.FeeHistory;
import com.example.tuitionrecords.TeacherActivity.TeacherFee.FeeStatus;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FeeHistoryStudent extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference;
    FeeHistoryStudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history_student);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String teacher_ref=getIntent().getStringExtra("teacher_uid");
        
        String user= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Fee_Status").child(teacher_ref).child(user);

        FirebaseRecyclerOptions<FeeHistoryModel> options =
                new FirebaseRecyclerOptions.Builder<FeeHistoryModel>()
                        .setQuery(reference,FeeHistoryModel.class)
                        .build();
        adapter=new FeeHistoryStudentAdapter(options);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(FeeHistoryStudent.this, FeeStatusStudent.class));
        finish();
    }
}