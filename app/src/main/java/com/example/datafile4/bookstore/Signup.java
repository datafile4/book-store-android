package com.example.datafile4.bookstore;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.datafile4.bookstore.CommonMethods;

import org.json.JSONObject;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    Button registerButton;
    EditText emailEdit;
    EditText passwordEdit;
    EditText passwordRepeatEdit;
    EditText firstNameEdit;
    EditText lastNameEdit;
    EditText usernameEdit;
    //keys
    String KEY_USERNAME="username";
    String KEY_EMAIL="email";
    String KEY_PASSWORD="password";
    String KEY_LAST_NAME="lastname";
    String KEY_FIRST_NAME="firstname";


    private String url =  "http://biatoms.azurewebsites.net/api/BookStore/Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //enable back button in action bar
        ActionBar actionBar = getSupportActionBar();
        try{
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Register");
        } catch (NullPointerException e){
            Log.e("Error: ", e.getMessage());
        }

        emailEdit = (EditText) findViewById(R.id.email_register);
        passwordEdit = (EditText) findViewById(R.id.password_register);
        passwordRepeatEdit = (EditText) findViewById(R.id.password_repeat_register);
        firstNameEdit = (EditText)findViewById(R.id.first_name_register);
        lastNameEdit = (EditText)findViewById(R.id.last_name_register);
        usernameEdit = (EditText)findViewById(R.id.username_register);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trim for remove whitespaces
                String email = emailEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String repeatPassword = passwordRepeatEdit.getText().toString().trim();
                String firstName = firstNameEdit.getText().toString().trim();
                String lastName = lastNameEdit.getText().toString().trim();
                String username = usernameEdit.getText().toString().trim();

                final Context context = getApplicationContext();
                //in this section we check if any of fields is empty
                //if empty, we show toast!
                if (email.isEmpty()) {
                    CommonMethods.showToast("Email is empty",context);
                } else if (password.isEmpty()) {
                    CommonMethods.showToast("Password is empty",context);
                } else if(lastName.isEmpty()){
                    CommonMethods.showToast("Last Name is empty",context);
                } else if(firstName.isEmpty()){
                    CommonMethods.showToast("First Name is empty",context);
                } else if(username.isEmpty()){
                    CommonMethods.showToast("Username is empty",context);
                } else if (!password.equals(repeatPassword)) {
                    //require mathing passwords
                    //if doesn't match, we will send toast!
                    CommonMethods.showToast("Passwords doesn't match",context);
                } else {
                    //registration section
                    HashMap<String, String>params = new HashMap<String, String>();
                    params.put(KEY_USERNAME,username);
                    params.put(KEY_EMAIL,email);
                    params.put(KEY_PASSWORD,password);
                    params.put(KEY_FIRST_NAME,firstName);
                    params.put(KEY_LAST_NAME,lastName);

                    //request
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            boolean success = response.optBoolean("success");
                            if (success) {
                                CommonMethods.showToast("Sucessfully Registered!", context);
                            } else {
                                CommonMethods.showToast("Registration Failed", context);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ",error.getMessage());
                        }
                    }){
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                            return super.parseNetworkResponse(response);
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Signup.this);
                    requestQueue.add(jsonObjectRequest);
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
