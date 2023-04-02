package com.example.a4000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;

public class user extends AppCompatActivity
{

    FirebaseAuth mAuth;
    Button button;
    TextView textView;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ValueEventListener eventListener;

    private String userName;

    String dob;
    private static final String USERS = "users";

    private int day,month,year;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

//        String dob;

        button = findViewById(R.id.logout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USERS);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        textView = findViewById(R.id.user_details);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        userName = user.getDisplayName();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                dob = snapshot.child(userName).child("dob").getValue(String.class);
                String parts_of_dob[] = dob.split("/");

                month = Integer.parseInt(parts_of_dob[0]);
                day = Integer.parseInt(parts_of_dob[1]);
                year = Integer.parseInt(parts_of_dob[2]);

                LocalDate birthday = LocalDate.of(year,month,day);
                LocalDate today = LocalDate.now();

                Period age = Period.between(birthday, today);

                textView = findViewById(R.id.user_age);
                textView.setText("Your age is " + age.getYears() + " years "  + age.getMonths()  + " months and " + age.getDays() + " days");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(user.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        textView = findViewById(R.id.user_name);
        textView.setText(userName);

//        bottomNavigationView = findViewById(R.id.bottom_navigation);


    }


}