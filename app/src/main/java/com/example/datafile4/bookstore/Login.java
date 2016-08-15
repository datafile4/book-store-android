package com.example.datafile4.bookstore;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    EditText emailEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //hiding action bar in login
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

         /*login*/
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdit = (EditText)findViewById(R.id.email_login);
                passwordEdit = (EditText)findViewById(R.id.password_login);

                String email = emailEdit.getText().toString();
                String pass = passwordEdit.getText().toString();
                Context context = getApplicationContext();
                //check, if empty
                if(email.isEmpty() || pass.isEmpty()){
                    CommonMethods.showToast("Email or Password is empty", context);
                }
            }
        });

        /*signup*/
        final TextView signUp = (TextView)findViewById(R.id.signUpText);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}
