package com.example.tuitionrecords.StudentActivity.Request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class  SendRequest extends AppCompatActivity {

    LinearLayout sort, filter;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;

    Toolbar toolbar;

    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students_Profile").child(user);

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

        sort.setOnClickListener(view -> sortTeachers());
        filter.setOnClickListener(view -> filterTeachers());
    }

    private void filterTeachers() {
        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.subject_filter))
                .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                .create();

        View subjectView = dialogPlus.getHolderView();

        final TextInputEditText subjectFilter = subjectView.findViewById(R.id.text);
        final AppCompatButton filter = subjectView.findViewById(R.id.filter_subject);

        dialogPlus.show();

        filter.setOnClickListener(view -> {
            String subjectText = subjectFilter.getText().toString();
            subjectText = subjectText.substring(0,1).toUpperCase() + subjectText.substring(1).toLowerCase();

            FirebaseRecyclerOptions<TeacherShowModel> options =
                    new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Teacher_profile").orderByChild("content").equalTo(subjectText), TeacherShowModel.class)
                            .build();

            dialogPlus.dismiss();

            studentAdapter = new StudentAdapter(options, getApplicationContext());
            recyclerView.setAdapter(studentAdapter);
            studentAdapter.startListening();
        });
    }

    private void sortTeachers() {
        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.sort_content))
                .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                .create();

        View myView = dialogPlus.getHolderView();

        final RadioButton ascending = myView.findViewById(R.id.asc);
        final RadioButton city = myView.findViewById(R.id.city);
        final RadioButton state = myView.findViewById(R.id.state);

        dialogPlus.show();

        ascending.setOnClickListener(view -> {
            FirebaseRecyclerOptions<TeacherShowModel> options =
                    new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Teacher_profile").orderByChild("name"), TeacherShowModel.class)
                    .build();

            dialogPlus.dismiss();

            studentAdapter = new StudentAdapter(options, getApplicationContext());
            recyclerView.setAdapter(studentAdapter);
            studentAdapter.startListening();
        });

        city.setOnClickListener(view -> {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StudentModel model = snapshot.getValue(StudentModel.class);

                    String city = model.getMyCity();
                    FirebaseRecyclerOptions<TeacherShowModel> options =
                            new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Teacher_profile").orderByChild("city").equalTo(city), TeacherShowModel.class)
                                    .build();

                    dialogPlus.dismiss();

                    studentAdapter = new StudentAdapter(options, getApplicationContext());
                    recyclerView.setAdapter(studentAdapter);
                    studentAdapter.startListening();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {  }
            });
        });

        state.setOnClickListener(view -> {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    StudentModel model = snapshot.getValue(StudentModel.class);

                    String state = model.getMyState();
                    FirebaseRecyclerOptions<TeacherShowModel> options =
                            new FirebaseRecyclerOptions.Builder<TeacherShowModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Teacher_profile").orderByChild("state").equalTo(state), TeacherShowModel.class)
                                    .build();

                    dialogPlus.dismiss();

                    studentAdapter = new StudentAdapter(options, getApplicationContext());
                    recyclerView.setAdapter(studentAdapter);
                    studentAdapter.startListening();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {  }
            });
        });
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