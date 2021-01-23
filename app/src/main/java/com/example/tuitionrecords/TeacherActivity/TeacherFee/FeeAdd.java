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

import com.example.tuitionrecords.R;

import java.util.Calendar;

public class FeeAdd extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText amount,dateInput;
    AppCompatButton button;
    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_add);
        button=findViewById(R.id.addButton);
        amount=findViewById(R.id.amount_text);
        dateInput=findViewById(R.id.date);
        calendar=findViewById(R.id.calendar);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(FeeAdd.this,FeeHistory.class));
    }
}