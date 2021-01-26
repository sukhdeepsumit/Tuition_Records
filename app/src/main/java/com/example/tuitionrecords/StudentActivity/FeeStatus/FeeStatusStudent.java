package com.example.tuitionrecords.StudentActivity.FeeStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.FeeStatusModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FeeStatusStudent extends AppCompatActivity {
    DatabaseReference reference;

    RecyclerView recyclerView;
    FeeStatusStudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status_student);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String current_user= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        reference= FirebaseDatabase.getInstance().getReference("Accepted_Students").child(current_user);
        FirebaseRecyclerOptions<FeeStatusModel> options=new FirebaseRecyclerOptions.Builder<FeeStatusModel>()
                .setQuery(reference,FeeStatusModel.class)
                .build();

        adapter=new FeeStatusStudentAdapter(options,FeeStatusStudent.this);
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
        startActivity(new Intent(FeeStatusStudent.this, ShowStudentActivity.class));
        finish();
    }
}