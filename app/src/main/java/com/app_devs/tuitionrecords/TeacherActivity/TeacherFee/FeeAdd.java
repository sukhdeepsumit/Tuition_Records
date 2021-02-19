package com.app_devs.tuitionrecords.TeacherActivity.TeacherFee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app_devs.tuitionrecords.FeeHistoryModel;
import com.app_devs.tuitionrecords.R;
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
    RelativeLayout checkInternet;
    LinearLayout layout;
    ImageView close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_add);
        checkInternet = findViewById(R.id.check_internet);
        layout = findViewById(R.id.main_layout);
        close = findViewById(R.id.close);
        checkInternet();

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date="dd/mm/yyyy:  "+dayOfMonth+"/"+String.valueOf(month+1)+"/"+year;
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