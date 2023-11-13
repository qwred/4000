package com.example.a4000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class user extends AppCompatActivity
{

    FirebaseAuth mAuth;
    Button button;
    TextView textView;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ValueEventListener eventListener;

    private RecyclerView parentRecyclerView;
    private RecyclerView grandParentRecyclerView;

    private ArrayList<GrandParentItem> grandParentItemList;

    private ArrayList<ParentItem> parentList;

    private String userName;

    String dob;
    private static final String USERS = "users";

    private int day,month,year;

    private Period age;

    private LocalDate birthday, today;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
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

//        textView = findViewById(R.id.user_details);

        if (user == null)
        {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        }
        else
        {
//            textView.setText(user.getEmail());

            userName = user.getDisplayName();

            eventListener = databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    dob = snapshot.child(userName).child("dob").getValue(String.class);
                    Log.d("dob", "dob" + dob);
                    String parts_of_dob[] = dob.split("/");

                    month = Integer.parseInt(parts_of_dob[0]);
                    day = Integer.parseInt(parts_of_dob[1]);
                    year = Integer.parseInt(parts_of_dob[2]);

                    Log.d("age1", "year " + year);
                    Log.d("age1", "month " + month);
                    Log.d("age1", "day " + day);



                    birthday = LocalDate.of(year,month,day);
                    today = LocalDate.now();

                    Log.d("age1", "birthday" + birthday);
                    Log.d("age1", "today" + today);

                    age = Period.between(birthday, today);

                    Log.d("age1", "age" + age);


                    parentRecyclerView = findViewById(R.id.parentRecyclerView);
                    parentRecyclerView.setHasFixedSize(true);
                    parentRecyclerView.setLayoutManager(new LinearLayoutManager(user.this));
                    parentList = new ArrayList<>();
//                    prepareData();
//                    ParentRecyclerViewAdapter adapter = new ParentRecyclerViewAdapter(parentList);
//                    parentRecyclerView.setAdapter(adapter);

                    grandParentRecyclerView = findViewById(R.id.grandParentRecyclerView);
                    grandParentRecyclerView.setHasFixedSize(true);
                    grandParentRecyclerView.setLayoutManager(new LinearLayoutManager(user.this));
                    grandParentItemList = new ArrayList<>();
                    prepareData();
                    GrandParentRecyclerViewAdapter adapter = new GrandParentRecyclerViewAdapter(grandParentItemList);
                    grandParentRecyclerView.setAdapter(adapter);



                }






                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(user.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }
    private void prepareData()
    {
        
        int user_months = (int) age.toTotalMonths();
        int user_year = year;
        int user_countedYear = 0;
        int month_count = month;
        Log.d("gettingMonth", "Months" + age.getMonths());
        ArrayList<ChildItem>[] months = new ArrayList[user_months];
        ArrayList<ParentItem>[] years = new ArrayList[user_months/12];

        Log.d("real", "months" + user_months);
        Log.d("real", "year" + user_year);

        for(int i=0; i<user_months; i++)
        {
            months[i] = new ArrayList<>();

            months[i].add(new ChildItem("week1",R.drawable.ic_action_name));
            months[i].add(new ChildItem("week2",R.drawable.ic_action_name));
            months[i].add(new ChildItem("week3",R.drawable.ic_action_name));
            months[i].add(new ChildItem("week4",R.drawable.ic_action_name));

            parentList.add(new ParentItem((Integer.toString(user_year) + "." + Integer.toString(month_count)), R.drawable.calendar ,months[i]));
            if(month_count == 12)
            {
                user_year++;
                month_count = 1;
                grandParentItemList.add(new GrandParentItem(Integer.toString(user_year), parentList, R.drawable.ic_action_name));
            }

            else
                month_count++;

        }



    }


}