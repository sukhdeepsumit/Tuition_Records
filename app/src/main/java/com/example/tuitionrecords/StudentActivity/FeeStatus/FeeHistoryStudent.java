package com.example.tuitionrecords.StudentActivity.FeeStatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.tuitionrecords.FeeHistoryModel;
import com.example.tuitionrecords.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FeeHistoryStudent extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference;
    FeeHistoryStudentAdapter adapter;
    RelativeLayout checkInternet;

    ImageView close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history_student);
        checkInternet = findViewById(R.id.check_internet);


        close = findViewById(R.id.close);
        checkInternet();

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
    public void checkInternet()
    {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm =
                        (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected)
                {
                    showInternetWarning();

                }
                handler.postDelayed(this,3000);
            }
        });
    }
    public void showInternetWarning() {
        checkInternet.setVisibility(View.VISIBLE);
        close.setOnClickListener(view -> checkInternet.setVisibility(View.GONE));
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