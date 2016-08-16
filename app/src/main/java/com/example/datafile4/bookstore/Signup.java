package com.example.datafile4.bookstore;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.datafile4.bookstore.CommonMethods;
public class Signup extends AppCompatActivity {

    Button registerButton;
    EditText emailEdit;
    EditText loginEdit;
    EditText passwordEdit;
    EditText passwordRepeatEdit;
    private String url =  "http://biatoms.azurewebsites.net/api/BookStore/Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //enable back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Register");

        emailEdit = (EditText) findViewById(R.id.email_register);
        loginEdit = (EditText) findViewById(R.id.login_register);
        passwordEdit = (EditText) findViewById(R.id.password_register);
        passwordRepeatEdit = (EditText) findViewById(R.id.password_repeat_register);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEdit.getText().toString();
                String login = loginEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String repeatPassword = passwordRepeatEdit.getText().toString();
                Context context = getApplicationContext();
                //in this section we check if any of fields is empty
                //if empty, we show toast!
                if (email.isEmpty()) {
                    CommonMethods.showToast("Email is empty",context);
                } else if (login.isEmpty()) {
                    CommonMethods.showToast("Login is empty",context);
                } else if (password.isEmpty()) {
                    CommonMethods.showToast("Password is empty",context);
                } else if (password != repeatPassword) {
                    //require mathing passwords
                    //if doesn't match, we will send toast!
                    CommonMethods.showToast("Passwords doesn't match",context);
                }
            }
        });
    }

    //if back button pressed, return home
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
