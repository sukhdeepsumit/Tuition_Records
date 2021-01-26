package com.example.tuitionrecords.StudentActivity.Request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class  SendRequest extends AppCompatActivity {

    LinearLayout sort, filter;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.search_menu);

        recyclerView  = findViewById(R.id.recyclerView);

        sort = findViewById(R.id.sort);
        filter = findViewById(R.id.filter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TeacherShowModel> options =
                new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Teacher_profile"), TeacherShowModel.class)
                .build();

        studentAdapter = new StudentAdapter(options, getApplicationContext());
        recyclerView.setAdapter(studentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        studentAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processSearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String s) {
        FirebaseRecyclerOptions<TeacherShowModel> options =
                new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Teacher_profile").orderByChild("name")
                                                                               .startAt(s).endAt(s+"\uf8ff"), TeacherShowModel.class).build();

        studentAdapter = new StudentAdapter(options, getApplicationContext());
        studentAdapter.startListening();
        recyclerView.setAdapter(studentAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SendRequest.this, ShowStudentActivity.class));
        finish();
    }
}