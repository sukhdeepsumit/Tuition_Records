package com.app_devs.tuitionrecords.TeacherActivity.ClassesAndBatches;

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
import android.widget.RelativeLayout;

import com.app_devs.tuitionrecords.DayTimeTable;
import com.app_devs.tuitionrecords.R;
import com.app_devs.tuitionrecords.Schedule.ScheduleModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyClasses extends AppCompatActivity {

    RecyclerView recyclerView;
    BatchAdapter batchAdapter;
    DatabaseReference reference;

    String user;
    RelativeLayout checkInternet;

    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);
        checkInternet = findViewById(R.id.check_internet);

        close = findViewById(R.id.close);
        checkInternet();
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

        batchAdapter=new BatchAdapter(options, day);
        recyclerView.setAdapter(batchAdapter);

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