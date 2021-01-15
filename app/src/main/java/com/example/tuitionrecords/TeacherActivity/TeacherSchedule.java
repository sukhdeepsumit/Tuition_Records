package com.example.tuitionrecords.TeacherActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.tuitionrecords.R;
import com.example.tuitionrecords.ScheduleModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//commit check
public class TeacherSchedule extends AppCompatActivity {

    TextInputEditText batch, subject;
    AppCompatButton add;

    CheckBox one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen;
    List<String> time = new ArrayList<>();

    String user;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        batch = findViewById(R.id.batch_text);
        subject = findViewById(R.id.subject_text);

        add = findViewById(R.id.add);

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

            /*if (current_hour < first) {
                status = "upcoming";
            }
            else if(current_hour > last) {
                status = "completed";
            }
            else {
                status = "ongoing";
            }*/

            Log.i("TIME_CURRENT" ,String.valueOf(current_hour));
            Log.i("TIME_PERIOD", first + " "  + last);

            ScheduleModel model = new ScheduleModel(result_time, subject.getText().toString(), batch.getText().toString(), String.valueOf(last));
            reference.push().setValue(model);

            Toast.makeText(this, "Time Table Added", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(TeacherSchedule.this, ShowTeacherActivity.class));
            finish();
        });

    }

   /* public void checkBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkbox1:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox1)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox2:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox2)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox3:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox3)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox4:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox4)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox5:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox5)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox6:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox6)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox7:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox7)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox8:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox8)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox9:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox9)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox10:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox10)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox11:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox11)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox12:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox12)).getText().toString() + " ";
                }
            break;
            case R.id.checkbox13:
                if (checked) {
                    time += ((CheckBox)findViewById(R.id.checkbox13)).getText().toString() + " ";
                }
            break;
        }
    }*/
}