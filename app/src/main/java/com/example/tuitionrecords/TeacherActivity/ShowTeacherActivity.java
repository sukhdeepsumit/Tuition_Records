package com.example.tuitionrecords.TeacherActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tuitionrecords.Contact_us;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.LogInStudentActivity;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowTeacherActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {
    ImageView message;
    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    FirebaseUser firebaseUser;
    DatabaseReference ref;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_teacher);
        progressBar=findViewById(R.id.progressBar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Teacher_profile").child(firebaseUser.getUid());
        Log.i("CURRENT_USER", firebaseUser.getUid());

        message = findViewById(R.id.message_requests);
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
                    startActivity(new Intent(ShowTeacherActivity.this,TeacherAccountInfo.class));
                    Toast.makeText(getApplicationContext(), "My Account opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.schedule :
                {
                    Toast.makeText(getApplicationContext(), "Time table opened", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
                case R.id.contactus :
                {
                    startActivity(new Intent(ShowTeacherActivity.this, Contact_us.class));
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
                    
                    SharedPreferences.Editor editor = LogInTeacherActivity.sharedPreferences.edit();
                    editor.putInt("key", 0);
                    editor.apply();
                    
                    startActivity(new Intent(ShowTeacherActivity.this, LogInTeacherActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
            }

            return true;
        });

        navigationHeaderDetails();
    }

    private void navigationHeaderDetails() {
        progressBar.setVisibility(View.VISIBLE);
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
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
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
}