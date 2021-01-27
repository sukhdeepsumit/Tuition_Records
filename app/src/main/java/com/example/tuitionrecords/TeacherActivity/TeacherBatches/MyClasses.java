package com.example.tuitionrecords.TeacherActivity.TeacherBatches;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.DayTimeTable;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.Schedule.ScheduleModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyClasses extends AppCompatActivity {

    RecyclerView recyclerView;
    BatchAdapter batchAdapter;
    DatabaseReference reference;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String day = getIntent().getStringExtra("day");

        user = getIntent().getStringExtra("user");

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Time_Table").child(currentUser).child(day);

        FirebaseRecyclerOptions<ScheduleModel> options =
                new FirebaseRecyclerOptions.Builder<ScheduleModel>()
                        .setQuery(reference.orderByChild("order"),ScheduleModel.class)
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyClasses.this, DayTimeTable.class);
        intent.putExtra("user", user);
        finish();
    }
}