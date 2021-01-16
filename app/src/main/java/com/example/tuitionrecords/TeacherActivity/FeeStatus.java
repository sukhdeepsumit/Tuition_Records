package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tuitionrecords.FeeStatusModel;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FeeStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    FeeStatusAdapter feeStatusAdapter;
    String user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<FeeStatusModel> options =
                new FirebaseRecyclerOptions.Builder<FeeStatusModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Students_Profile"),FeeStatusModel.class)
                        .build();

        feeStatusAdapter=new FeeStatusAdapter(options,reference);
        recyclerView.setAdapter(feeStatusAdapter);

        user= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference=FirebaseDatabase.getInstance().getReference("Fee_Status").child(user);

        FeeStatusModel feeStatusModel=new FeeStatusModel();
        reference.push().setValue(feeStatusModel);



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

}