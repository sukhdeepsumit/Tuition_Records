package com.example.tuitionrecords.TeacherActivity.TeacherFee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tuitionrecords.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeeHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);
        add=findViewById(R.id.addFee);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeeHistory.this, FeeAdd.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FeeHistory.this,FeeStatus.class));

    }
}