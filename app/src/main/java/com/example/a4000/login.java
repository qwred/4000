package com.example.a4000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity
{

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signButton;

    private String admin_username = "admin";
    private String admin_password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameEditText = findViewById(R.id.login_usernameEditText);
        passwordEditText = findViewById(R.id.login_passwordEditText);
        loginButton = findViewById(R.id.login_loginButton);
        signButton = findViewById(R.id.login_signButton);


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username = String.valueOf(usernameEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                if((username.length()>0) && (password.length()>0))
                    if((username.equals(admin_username)) &&(password.equals(admin_password)))
                        startActivity(new Intent(login.this, home.class));
                    else
                    {
                        String toastMessage = "Invalid username or Invalid password";
                        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    }
                else
                {
                    String toastMessage = "You can't leave blank space at the username or password";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}