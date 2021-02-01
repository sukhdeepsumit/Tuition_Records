package com.example.tuitionrecords.StudentActivity;

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
import com.example.tuitionrecords.Chats.Chat;
import com.example.tuitionrecords.Contact_us;
import com.example.tuitionrecords.DayTimeTable;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.Schedule.Schedule;
import com.example.tuitionrecords.Schedule.ScheduleAdapter;
import com.example.tuitionrecords.Schedule.ScheduleModel;
import com.example.tuitionrecords.StudentActivity.Authentication.LogInStudentActivity;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.example.tuitionrecords.StudentActivity.FeeStatus.FeeStatusStudent;
import com.example.tuitionrecords.StudentActivity.MyTeacher.MyTeachers;
import com.example.tuitionrecords.StudentActivity.Request.SendRequest;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowStudentActivity extends AppCompatActivity {
    //commit check2

    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ref;

    ConstraintLayout layout;
    RelativeLayout checkInternet, myTeachers, myFeeStatus, myClass, myChats;
    ImageView close;

    RecyclerView recyclerView;
    ScheduleAdapter adapter;

    FloatingActionButton add;

    SharedPreferences sharedPreferences;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Accepted_Students");
    DatabaseReference batchRef = FirebaseDatabase.getInstance().getReference("Allot_Batches");
    DatabaseReference timetable = FirebaseDatabase.getInstance().getReference("Time_Table");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Welcome aboard !");
        progressDialog.setMessage("Just a moment...");

        layout = findViewById(R.id.layout_full);
        add=findViewById(R.id.add);

        checkInternet = findViewById(R.id.check_internet);
        close = findViewById(R.id.close);

        myClass = findViewById(R.id.time_table);
        myChats = findViewById(R.id.chat);

        myChats.setOnClickListener(view -> {
            Intent intent=new Intent(ShowStudentActivity.this, Chat.class);
            intent.putExtra("user","student");
            startActivity(intent);
        });

        sharedPreferences = getApplicationContext().getSharedPreferences("auto_login_student", Context.MODE_PRIVATE);

        checkInternet();

        myFeeStatus=findViewById(R.id.feeStatus);
        myFeeStatus.setOnClickListener(view -> {
            startActivity(new Intent(ShowStudentActivity.this, FeeStatusStudent.class));
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);

        myTeachers=findViewById(R.id.myTeachers);
        myTeachers.setOnClickListener(view -> {
            startActivity(new Intent(ShowStudentActivity.this, MyTeachers.class));
            finish();
        });

        myClass.setOnClickListener(view -> {
            Intent intent = new Intent(ShowStudentActivity.this, DayTimeTable.class);
            intent.putExtra("user", "student");
            startActivity(intent);
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Students_Profile").child(userId);

        add.setOnClickListener(view -> {
            Intent intent = new Intent(ShowStudentActivity.this, SendRequest.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        });

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        Log.i("DAY_TODAY", day);

        FirebaseRecyclerOptions<ScheduleModel> options =
                new FirebaseRecyclerOptions.Builder<ScheduleModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Time_Table")
                                .child(userId).child(day).orderByChild("order"), ScheduleModel.class)
                                .build();

        adapter = new ScheduleAdapter(options);
        recyclerView.setAdapter(adapter);

        Log.i("REFERENCE_KEY", reference.child(userId).getKey());

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    startActivity(new Intent(ShowStudentActivity.this, StudentAccountInfo.class));
                    Toast.makeText(getApplicationContext(), "My Account opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.schedule :
                {
                    Intent intent = new Intent(ShowStudentActivity.this, Schedule.class);
                    intent.putExtra("user", "student");
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Time table opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.contactus :
                {
                    Intent intent=new Intent(ShowStudentActivity.this,Contact_us.class);
                    intent.putExtra("userId","Student");
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Contact us opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case  R.id.howto:
                {
                    Toast.makeText(getApplicationContext(), "How to use opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.logout:
                {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("key_student", 0);
                    editor.apply();

                    startActivity(new Intent(ShowStudentActivity.this, LogInStudentActivity.class));
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

               StudentModel studentModel = snapshot.getValue(StudentModel.class);
               assert studentModel != null;
               name.setText(studentModel.getName());
               email.setText(studentModel.getMyEmail());
               Glide.with(getApplicationContext()).load(studentModel.getMyUri()).placeholder(R.drawable.anonymous_user).listener(new RequestListener<Drawable>() {
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

        Log.i("UID_USER", Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
        if (firebaseAuth == null) {
            startActivity(new Intent(ShowStudentActivity.this, LogInStudentActivity.class));
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