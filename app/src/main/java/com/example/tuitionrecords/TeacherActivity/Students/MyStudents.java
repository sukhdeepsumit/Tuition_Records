package com.example.tuitionrecords.TeacherActivity.Students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.StudentModel;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyStudents extends AppCompatActivity {

    RecyclerView recyclerView;

    MyStudentsAdapter adapter;
    DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_students);

        recyclerView = findViewById(R.id.recyclerView);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        studentRef = FirebaseDatabase.getInstance().getReference("Accepted_Students").child(currentUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<StudentModel> options =
                new FirebaseRecyclerOptions.Builder<StudentModel>()
                .setQuery(studentRef, StudentModel.class)
                .build();

        adapter = new MyStudentsAdapter(options, getApplicationContext());
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
        startActivity(new Intent(MyStudents.this, ShowTeacherActivity.class));
        finish();
    }
}