package com.example.tuitionrecords.TeacherActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tuitionrecords.Contact_us;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.ScheduleAdapter;
import com.example.tuitionrecords.ScheduleModel;
import com.example.tuitionrecords.TeacherActivity.Authentication.LogInTeacherActivity;
import com.example.tuitionrecords.TeacherActivity.Authentication.TeacherModel;
import com.example.tuitionrecords.TeacherActivity.Requests.RequestActivity;
import com.example.tuitionrecords.TeacherActivity.Students.MyStudents;
import com.example.tuitionrecords.TeacherActivity.Students.My_Registered_Students;
import com.example.tuitionrecords.TeacherActivity.TeacherBatches.MyBatches;
import com.example.tuitionrecords.TeacherActivity.TeacherFee.FeeStatus;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

//commit check

public class ShowTeacherActivity extends AppCompatActivity  {
    ImageView requests;
    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    ConstraintLayout layout;
    RecyclerView recyclerView;
    RelativeLayout myStudents;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ref, notification;

    ProgressDialog progressDialog;

    RelativeLayout checkInternet,feeStatus, myBatches;
    ImageView close;

    SharedPreferences sharedPreferences;
    ScheduleAdapter adapter;

    CircleImageView notificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teacher);

        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);

        layout = findViewById(R.id.full_layout);
        recyclerView = findViewById(R.id.recyclerView);

        myStudents = findViewById(R.id.my_students);

        sharedPreferences = getApplicationContext().getSharedPreferences("auto_login_teacher", Context.MODE_PRIVATE);

        checkInternet();
        progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Welcome aboard !");
        progressDialog.setMessage("Just a moment...");

        feeStatus=findViewById(R.id.relativeLayout);
        feeStatus.setOnClickListener(view -> startActivity(new Intent(ShowTeacherActivity.this, FeeStatus.class)));

        myBatches=findViewById(R.id.batches);
        myBatches.setOnClickListener(v -> startActivity(new Intent(ShowTeacherActivity.this, MyBatches.class)));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(userId);
        Log.i("CURRENT_USER", firebaseUser.getUid());

        requests = findViewById(R.id.message_requests);
        notificationStatus = findViewById(R.id.alert);

        notification = FirebaseDatabase.getInstance().getReference("Requests");
        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    notificationStatus.setVisibility(View.VISIBLE);
                }
                else {
                    notificationStatus.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //orderByChild("order"):
        FirebaseRecyclerOptions<ScheduleModel> options =
                new FirebaseRecyclerOptions.Builder<ScheduleModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Time_Table").child(userId).orderByChild("order"), ScheduleModel.class)
                .build();

        adapter = new ScheduleAdapter(options);
        recyclerView.setAdapter(adapter);

        requests.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RequestActivity.class));
            finish();
        });

        myStudents.setOnClickListener(view -> {
            startActivity(new Intent(this, My_Registered_Students.class));
            finish();
        });

        nav=findViewById(R.id.navMenu);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId())
            {
                case R.id.myAccount :
                {
                    startActivity(new Intent(ShowTeacherActivity.this,TeacherAccountInfo.class));
                    Toast.makeText(getApplicationContext(), "My Account opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.schedule :
                {
                    startActivity(new Intent(ShowTeacherActivity.this,TeacherSchedule.class));
                    Toast.makeText(getApplicationContext(), "Time table opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.contactus :
                {
                    Intent intent=new Intent(ShowTeacherActivity.this,Contact_us.class);
                    intent.putExtra("userId","Teacher");
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Contact us opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case  R.id.howto :
                {
                    Toast.makeText(getApplicationContext(), "How to use opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.logout :
                {
                    FirebaseAuth.getInstance().signOut();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("key_teacher", 0);
                    editor.apply();

                    Toast.makeText(this, "User Logged Out", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(ShowTeacherActivity.this, LogInTeacherActivity.class));
                    finish();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
            }

            return true;
        });

        navigationHeaderDetails();
    }

    private void navigationHeaderDetails() {
        progressDialog.show();
        View headerView = nav.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.nav_name);
        TextView email = headerView.findViewById(R.id.nav_email);
        CircleImageView imageView = headerView.findViewById(R.id.nav_photo);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TeacherModel teacherModel = snapshot.getValue(TeacherModel.class);
                assert teacherModel != null;
                name.setText(teacherModel.getName());
                email.setText(teacherModel.getEmail());
                Glide.with(getApplicationContext()).load(teacherModel.getMyUri()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressDialog.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressDialog.dismiss();
                        return false;
                    }
                }).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
                    layout.setVisibility(View.GONE);
                    showInternetWarning();
                }
                else {
                    layout.setVisibility(View.VISIBLE);
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
        if (firebaseAuth == null) {
            //firebaseAuth.signOut();
            startActivity(new Intent(ShowTeacherActivity.this, LogInTeacherActivity.class));
        }
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        finish();
    }
}