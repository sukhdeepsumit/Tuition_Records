package com.example.tuitionrecords.TeacherActivity.TeacherFee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tuitionrecords.FeeHistoryModel;
import com.example.tuitionrecords.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class FeeAdd extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    TextInputEditText amount,dateInput;
    AppCompatButton button;
    ImageView calendar;
    DatabaseReference reference;
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_add);

        button=findViewById(R.id.addButton);
        amount=findViewById(R.id.amount_text);
        dateInput=findViewById(R.id.date);
        calendar=findViewById(R.id.calendar);
        user= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        reference = FirebaseDatabase.getInstance().getReference("Fee_Status").child(user);

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        FeeAdd.this,
                        FeeAdd.this::onDateSet,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH) );
                datePickerDialog.show();

            }
        });
        String student_uid=getIntent().getStringExtra("student_uid");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FeeHistoryModel feeHistoryModel=new FeeHistoryModel("Rs. "+amount.getText().toString(),dateInput.getText().toString());
                reference.child(student_uid).push().setValue(feeHistoryModel);
                Toast.makeText(FeeAdd.this, "Record added", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FeeAdd.this,FeeHistory.class);
                intent.putExtra("student_uid",student_uid);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date="dd/mm/yyyy:  "+dayOfMonth+"/"+month+1+"/"+year;
        dateInput.setText(date);

    }

    @Override
    public void onBackPressed() {
        String student_uid=getIntent().getStringExtra("student_uid");
        Intent intent=new Intent(FeeAdd.this,FeeHistory.class);
        intent.putExtra("student_uid",student_uid);
        startActivity(intent);
        finish();
    }
}