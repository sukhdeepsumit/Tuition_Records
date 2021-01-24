package com.example.tuitionrecords.TeacherActivity.TeacherFee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionrecords.FeeStatusModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FeeStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    FeeStatusAdapter feeStatusAdapter;
    DatabaseReference reference;

//comitt check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Accepted_Students").child(currentUser);

        FirebaseRecyclerOptions<FeeStatusModel> options =
                new FirebaseRecyclerOptions.Builder<FeeStatusModel>()
                        .setQuery(reference,FeeStatusModel.class)
                        .build();

        feeStatusAdapter=new FeeStatusAdapter(options,FeeStatus.this);
        recyclerView.setAdapter(feeStatusAdapter);

        
    }

    @Override
    protected void onStart() {
        super.onStart();
        feeStatusAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        feeStatusAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FeeStatus.this, ShowTeacherActivity.class));
        finish();
    }
}