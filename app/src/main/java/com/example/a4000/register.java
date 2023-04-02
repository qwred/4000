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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class register extends AppCompatActivity
{


    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private Button signButton;
    private Button backButton;

    private DatePicker birthday;

    private static final String USERS = "users";

    String dob;

//    private DatePickerDialog datePickerDialog;
    FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    userInfo userInfo;

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
        nameEditText = findViewById(R.id.register_nameEditText);

        progressBar = findViewById(R.id.progressBar);

        birthday = findViewById(R.id.register_dob); //
        birthday.setMaxDate(Calendar.getInstance().getTimeInMillis());

        int day = birthday.getDayOfMonth();
        int month = birthday.getMonth()+1;
        int year = birthday.getYear();

//        Calendar calendar = Calendar.getInstance();


//        birthday.init(year,month,day, new DatePicker.OnDateChangedListener()
//        {
//
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
//            {
//                int selectedYear = view.getYear();
//                int selectedMonth = view.getMonth() +1;
//                int selectedDay = view.getDayOfMonth();
//
//                calendar.set(selectedYear,selectedMonth,selectedDay);
//
//            }
//        });

        birthday.init(year, month, day, new DatePicker.OnDateChangedListener()
                {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        // Get the selected date of birth from the DatePicker widget
                        dob = (monthOfYear + 1) + "/" + dayOfMonth  + "/" + year;
                        // Use the dob variable as needed

                    }
                }
        );

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        dob = dateFormat.format(calendar.getTime());

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
                String fullName = String.valueOf(nameEditText.getText());

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

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference(USERS);
                            userInfo = new userInfo(fullName, dob, username);
                            FirebaseUser user = mAuth.getCurrentUser();
                            addDataFirebase(user);

                            databaseReference.orderByChild("userName"); //Sort the database by the name

                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();

                            user.updateProfile(profileUpdate);

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(register.this, "Account created.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), user.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            if(password.length()<6)
                                Toast.makeText(register.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(register.this, "There is already existing account with this email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

//    private void addDataFirebase(String birthday, String name)
//    {
//        userInfo.setDob(birthday);
//        databaseReference.addValueEventListener(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                // inside the method of on Data change we are setting
//                // our object class to our database reference.
//                // data base reference will sends data to firebase.
//                databaseReference.setValue(userInfo);
//
//                // after adding this data we are showing toast message.
//                Toast.makeText(register.this, "data added", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error)
//            {
//                Toast.makeText(register.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
//            }
//
//
//
//        });
//
//        //https://www.geeksforgeeks.org/how-to-save-data-to-the-firebase-realtime-database-in-android/
//

//    }
    private void addDataFirebase(FirebaseUser currentuser)
    {
//        The code below make the node and add the data to the realtime database.
//        However, it doesn't change the name of the node. In fact, it generates nodes like"-NRjSu88fjdQBjoNoZ1V"
//        String keyId = databaseReference.push().getKey();
//        databaseReference.child(keyId).setValue(userInfo);
//
//      To fix the problem I changed, the name of the node as the user's Full name.
        databaseReference.push();
        databaseReference.child(userInfo.getUserName()).setValue(userInfo);
        Intent loginIntent = new Intent(this,login.class);
        startActivity(loginIntent);
    }

//    https://www.youtube.com/watch?v=0gNPX52o_7I

}