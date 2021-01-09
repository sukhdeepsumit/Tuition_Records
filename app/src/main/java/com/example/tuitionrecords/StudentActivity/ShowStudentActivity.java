package com.example.tuitionrecords.StudentActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.google.android.material.navigation.NavigationView;

public class ShowStudentActivity extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav=findViewById(R.id.navMenu);
        drawerLayout=findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ShowStudentActivity.this,LogInStudentActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                }

                return true;
            }
        });


    }
}