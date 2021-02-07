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
    RelativeLayout checkInternet;

    ImageView close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_status_student);
        checkInternet = findViewById(R.id.check_internet);

        close = findViewById(R.id.close);
        checkInternet();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String current_user= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        reference= FirebaseDatabase.getInstance().getReference("Accepted_Students").child(current_user);
        FirebaseRecyclerOptions<FeeStatusModel> options=new FirebaseRecyclerOptions.Builder<FeeStatusModel>()
                .setQuery(reference,FeeStatusModel.class)
                .build();

        adapter=new FeeStatusStudentAdapter(options,getApplicationContext());
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
        startActivity(new Intent(FeeStatusStudent.this, ShowStudentActivity.class));
        finish();
    }
}