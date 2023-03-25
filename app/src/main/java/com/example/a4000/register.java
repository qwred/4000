package com.example.a4000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;


public class register extends AppCompatActivity
{


    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signButton;
    private Button backButton;
//    private Button dateButton;
//    private DatePickerDialog datePickerDialog;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), user.class);
//            startActivity(intent);
//            finish();
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        initDatePicker(); //
//        dateButton = findViewById(R.id.datePickerButton); //
//        dateButton.setText(getTodaysDate()); //

        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.register_usernameEditText);
        passwordEditText = findViewById(R.id.register_passwordEditText);

        progressBar = findViewById(R.id.progressBar);

        backButton = findViewById(R.id.register_loginButton);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
            }
        });

        signButton = findViewById(R.id.register_signButton);

        signButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                String username = String.valueOf(usernameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(register.this, "You can't leave blank on username", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(register.this, "You can't leave blank on the password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(register.this, "Account created.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

//    private String getTodaysDate()
//    {
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        month = month + 1;
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//        return makeDateString(day, month, year);
//    }
//
//    private void initDatePicker()
//    {
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
//        {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day)
//            {
//                month = month + 1;
//                String date = makeDateString(day, month, year);
//                dateButton.setText(date);
//            }
//        };
//
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int day = cal.get(Calendar.DAY_OF_MONTH);
//
//        int style = AlertDialog.THEME_HOLO_LIGHT;
//
//        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
//        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//
//    }
//
//    private String makeDateString(int day, int month, int year)
//    {
//        return getMonthFormat(month) + " " + day + " " + year;
//    }
//
//    //Try to find a smarter way to do it
//    private String getMonthFormat(int month)
//    {
//        if(month == 1)
//            return "JAN";
//        if(month == 2)
//            return "FEB";
//        if(month == 3)
//            return "MAR";
//        if(month == 4)
//            return "APR";
//        if(month == 5)
//            return "MAY";
//        if(month == 6)
//            return "JUN";
//        if(month == 7)
//            return "JUL";
//        if(month == 8)
//            return "AUG";
//        if(month == 9)
//            return "SEP";
//        if(month == 10)
//            return "OCT";
//        if(month == 11)
//            return "NOV";
//        if(month == 12)
//            return "DEC";
//
//        return "JAN";
//    }
//
//    public void openDatePicker(View view)
//    {
//        datePickerDialog.show();
//    }


}