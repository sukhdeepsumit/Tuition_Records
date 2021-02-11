package com.example.tuitionrecords.TeacherActivity.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.Authentication.StudentModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class  MyStudents extends AppCompatActivity {

    CircleImageView profile_picture;
    TextView name, email, gender, city_state, standard, about, batch;
    AppCompatButton remove;

    DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("Students_Profile");
    DatabaseReference batchRef = FirebaseDatabase.getInstance().getReference("Allot_Batches");
    DatabaseReference accepted = FirebaseDatabase.getInstance().getReference("Accepted_Students");

    String teacher = FirebaseAuth.getInstance().getCurrentUser().getUid();
    RelativeLayout checkInternet;
    LinearLayout layout;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_students);
        checkInternet = findViewById(R.id.check_internet);
        layout = findViewById(R.id.main_layout);
        close = findViewById(R.id.close);
        checkInternet();

        name = findViewById(R.id.name_get);
        email = findViewById(R.id.email_get);
        gender = findViewById(R.id.gender_get);
        city_state = findViewById(R.id.city_state_get);
        standard = findViewById(R.id.standard_get);
        about = findViewById(R.id.about_get);
        batch = findViewById(R.id.batch);

        remove = findViewById(R.id.remove);
        profile_picture = findViewById(R.id.dp_upload);

        String student_key = getIntent().getStringExtra("student_key");

        remove.setOnClickListener(view -> removeStudent(student_key));

        studentRef.child(student_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentModel model = snapshot.getValue(StudentModel.class);

                assert model != null;
                name.setText(model.getName());
                email.setText(model.getMyEmail());
                gender.setText(model.getMyGender());
                String location= model.getMyCity()+", "+model.getMyState();
                city_state.setText(location);
                standard.setText(model.getMyStandard());
                about.setText(model.getMyDescription());

                if(model.getMyUri().equals("default")) {
                    profile_picture.setImageResource(R.drawable.anonymous_user);
                }
                else {
                    Glide.with(getApplicationContext()).load(model.getMyUri()).into(profile_picture);
                }

                batchRef.child(teacher).child(student_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        batch.setText(Objects.requireNonNull(snapshot.child("batch").getValue()).toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        batch.setOnClickListener(view -> allotBatch(student_key));
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
    private void removeStudent(String student_key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove");
        builder.setMessage("Do you want to remove this student from your list? ");
        builder.setCancelable(false);

        builder.setPositiveButton("YES", (dialogInterface, i) -> accepted.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(student_key).removeValue()
                .addOnCompleteListener(task -> accepted.child(student_key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()
                        .addOnCompleteListener(task1 -> {
                            Toast.makeText(getApplicationContext(), "Removed from your student list", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MyStudents.this, My_Registered_Students.class));
                            finish();
                        })
                )
        );

        builder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());
        builder.create().show();
    }

    private void allotBatch(String student) {
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.update_batch_allottment))
                .setExpanded(true, WindowManager.LayoutParams.WRAP_CONTENT)
                .create();

        View myView = dialogPlus.getHolderView();
        final TextInputEditText editText = myView.findViewById(R.id.text);
        final AppCompatButton update = myView.findViewById(R.id.update);

        editText.setText(batch.getText());
        dialogPlus.show();

        update.setOnClickListener(view -> {
            String updatedBatch = Objects.requireNonNull(editText.getText()).toString();

            batchRef.child(teacher).child(student).child("batch").setValue(updatedBatch).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    batchRef.child(student).child(teacher).child("batch").setValue(updatedBatch).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Batch Number Allotted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            dialogPlus.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyStudents.this, My_Registered_Students.class));
        finish();
    }
}