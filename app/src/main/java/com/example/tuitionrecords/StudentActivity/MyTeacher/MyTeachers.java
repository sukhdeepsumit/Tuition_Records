package com.example.tuitionrecords.StudentActivity.MyTeacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.StudentActivity.Request.TeacherShowModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MyTeachers extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference;
    MyTeachersAdapter myTeachersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teachers);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String currentUser= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference= FirebaseDatabase.getInstance().getReference("Accepted_Students").child(currentUser);

        FirebaseRecyclerOptions<TeacherShowModel> options =
                new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                        .setQuery(reference, TeacherShowModel.class)
                        .build();
        myTeachersAdapter=new MyTeachersAdapter(options,MyTeachers.this);
        recyclerView.setAdapter(myTeachersAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        myTeachersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myTeachersAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyTeachers.this, ShowStudentActivity.class));
        finish();
    }
}