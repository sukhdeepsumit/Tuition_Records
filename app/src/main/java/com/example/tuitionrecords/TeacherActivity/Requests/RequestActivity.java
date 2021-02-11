package com.example.tuitionrecords.TeacherActivity.Requests;

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

import com.example.tuitionrecords.Notifications.Token;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

//commit check
public class RequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    DatabaseReference requestReference;
    StudentRequestAdapter adapter;

    String currentUser;
    RelativeLayout checkInternet;

    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        checkInternet = findViewById(R.id.check_internet);

        close = findViewById(R.id.close);
        checkInternet();
        recyclerView = findViewById(R.id.recyclerView);

        currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        requestReference = FirebaseDatabase.getInstance().getReference("Requests").child(currentUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<StudentRequestProfileModel> options =
                new FirebaseRecyclerOptions.Builder<StudentRequestProfileModel>()
                .setQuery(requestReference, StudentRequestProfileModel.class)
                .build();

        adapter = new StudentRequestAdapter(options, getApplicationContext());
        recyclerView.setAdapter(adapter);

        //updateToken(FirebaseInstanceId.getInstance().getToken());
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
    /*public void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(currentUser).setValue(token1);
    }*/

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RequestActivity.this, ShowTeacherActivity.class));
        finish();
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
}