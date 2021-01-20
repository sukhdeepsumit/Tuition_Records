package com.example.tuitionrecords.TeacherActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tuitionrecords.FeeStatusModel;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.ScheduleModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyBatches extends AppCompatActivity {

    RecyclerView recyclerView;
    BatchAdapter batchAdapter;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_batches);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Time_Table").child(currentUser);

        FirebaseRecyclerOptions<ScheduleModel> options =
                new FirebaseRecyclerOptions.Builder<ScheduleModel>()
                        .setQuery(reference,ScheduleModel.class)
                        .build();

        batchAdapter=new BatchAdapter(options);
        recyclerView.setAdapter(batchAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        batchAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        batchAdapter.stopListening();
    }
}