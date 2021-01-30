package com.example.tuitionrecords.Schedule;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.StudentActivity.ShowStudentActivity;
import com.example.tuitionrecords.TeacherActivity.ShowTeacherActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//commit check again
public class Schedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextInputEditText batch, subject;
    AppCompatButton add;
    Spinner spinner;

    CheckBox one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen;
    List<String> time = new ArrayList<>();

    String user, check_user;
    DatabaseReference reference;
    String day;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        check_user = getIntent().getStringExtra("user");

        batch = findViewById(R.id.batch_text);
        subject = findViewById(R.id.subject_text);

        add = findViewById(R.id.add);
        spinner = findViewById(R.id.day_spinner);

        /*batchNo = batch.getText().toString().trim();
        batchNo = batchNo.replace(" ", "_");*/

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference("Time_Table").child(user);

        one = findViewById(R.id.checkbox1);
        two = findViewById(R.id.checkbox2);
        three = findViewById(R.id.checkbox3);
        four = findViewById(R.id.checkbox4);
        five = findViewById(R.id.checkbox5);
        six = findViewById(R.id.checkbox6);
        seven = findViewById(R.id.checkbox7);
        eight = findViewById(R.id.checkbox8);
        nine = findViewById(R.id.checkbox9);
        ten = findViewById(R.id.checkbox10);
        eleven = findViewById(R.id.checkbox11);
        twelve = findViewById(R.id.checkbox12);
        thirteen = findViewById(R.id.checkbox13);

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        //ArrayAdapter daysAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, days);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(daysAdapter);
        add.setOnClickListener(view -> {

            if (one.isChecked()) {
                time.add(one.getText().toString());
            }
            if(two.isChecked()) {
                time.add(two.getText().toString());
            }
            if(three.isChecked()) {
                time.add(three.getText().toString());
            }
            if(four.isChecked()) {
                time.add(four.getText().toString());
            }
            if(five.isChecked()) {
                time.add(five.getText().toString());
            }
            if(six.isChecked()) {
                time.add(six.getText().toString());
            }
            if(seven.isChecked()) {
                time.add(seven.getText().toString());
            }
            if(eight.isChecked()) {
                time.add(eight.getText().toString());
            }
            if(nine.isChecked()) {
                time.add(nine.getText().toString());
            }
            if(ten.isChecked()) {
                time.add(ten.getText().toString());
            }
            if(eleven.isChecked()) {
                time.add(eleven.getText().toString());
            }
            if(twelve.isChecked()) {
                time.add(twelve.getText().toString());
            }
            if(thirteen.isChecked()) {
                time.add(thirteen.getText().toString());
            }

            int len = time.size();
            String result_time = time.get(0).substring(0,2) + " - " + time.get(len-1).substring(3,5) + " " + time.get(len-1).substring(6,8);

           /* HashMap<String, Object> map = new HashMap<>();
            map.put("batch_number", batch.getText().toString());
            map.put("subject_batch", subject.getText().toString());
            map.put("time", result_time);*/

            LocalTime time = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String timing = time.format(formatter);

            String[] array_time = timing.split(":");
            int current_hour = Integer.parseInt(array_time[0]);

            int first = Integer.parseInt(result_time.substring(0,2));
            int last = Integer.parseInt(result_time.substring(5,7));
            if (result_time.contains("PM")) {
                if (last < 12) {
                    last += 12;
                }
            }

            Log.i("TIME_CURRENT" ,String.valueOf(current_hour));
            Log.i("TIME_PERIOD", first + " "  + last);

            String batchNo = batch.getText().toString().toLowerCase();
            ScheduleModel model = new ScheduleModel(result_time, subject.getText().toString(), batchNo, String.valueOf(last));

            Toast.makeText(this, "Time Table Added", Toast.LENGTH_SHORT).show();

            reference.child(day).push().setValue(model);

            if (check_user.equals("teacher")) {
                startActivity(new Intent(Schedule.this, ShowTeacherActivity.class));
            }
            else {
                startActivity(new Intent(Schedule.this, ShowStudentActivity.class));
            }
            finish();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i("Day", adapterView.getItemAtPosition(position).toString());
        day = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {  }

    @Override
    public void onBackPressed() {
        if (check_user.equals("teacher")) {
            startActivity(new Intent(Schedule.this,ShowTeacherActivity.class));
        }
        else {
            startActivity(new Intent(Schedule.this,ShowStudentActivity.class));
        }
        finish();
    }
}