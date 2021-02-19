package com.app_devs.tuitionrecords.StudentActivity.MyTeacher;

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

import com.app_devs.tuitionrecords.R;
import com.app_devs.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.app_devs.tuitionrecords.StudentActivity.Request.TeacherShowModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MyTeachers extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference reference;
    MyTeachersAdapter myTeachersAdapter;
    RelativeLayout checkInternet;

    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teachers);
        checkInternet = findViewById(R.id.check_internet);

        close = findViewById(R.id.close);
        checkInternet();

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String currentUser= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference= FirebaseDatabase.getInstance().getReference("Accepted_Students").child(currentUser);

        FirebaseRecyclerOptions<TeacherShowModel> options =
                new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                        .setQuery(reference, TeacherShowModel.class)
                        .build();
        myTeachersAdapter=new MyTeachersAdapter(options,getApplicationContext());
        recyclerView.setAdapter(myTeachersAdapter);


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