package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.TeacherActivity.TeacherModel;
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

public class ShowStudentActivity extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    FirebaseAuth myAuth;
    FirebaseUser firebaseUser;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Students_Profile").child(firebaseUser.getUid());

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
                    myAuth.signOut();
                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = LogInStudentActivity.sharedPreferences.edit();
                    editor.putInt("key", 0);
                    editor.apply();

                    startActivity(new Intent(ShowStudentActivity.this,LogInStudentActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                }
            }
            return true;
        });

        navigationHeaderDetails();
    }

    private void navigationHeaderDetails() {
        View headerView = nav.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.nav_name);
        TextView email = headerView.findViewById(R.id.nav_email);
        CircleImageView imageView = headerView.findViewById(R.id.nav_photo);

       ref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               StudentModel studentModel = snapshot.getValue(StudentModel.class);
               assert studentModel != null;
               name.setText(studentModel.getName());
               email.setText(studentModel.getMyEmail());
               Glide.with(getApplicationContext()).load(studentModel.getMyUri()).into(imageView);
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