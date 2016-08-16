package com.example.datafile4.bookstore;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    EditText emailEdit;
    EditText passwordEdit;
    String KEY_USERNAME = "username";
    String KEY_PASSWORD = "password";
    private String url = "http://amiraslan.azurewebsites.net/api/BookStore/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //hiding action bar in login
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

         /*login*/
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdit = (EditText) findViewById(R.id.email_login);
                passwordEdit = (EditText) findViewById(R.id.password_login);

                final String email = emailEdit.getText().toString().trim();
                final String pass = passwordEdit.getText().toString().trim();
                final Context context = getApplicationContext();
                //check, if empty
                if (email.isEmpty() || pass.isEmpty()) {
                    CommonMethods.showToast("Email or Password is empty", context);
                } else {
                    //login process
                   HashMap<String,String>params = new HashMap<String, String>();
                    params.put(KEY_USERNAME,email);
                    params.put(KEY_PASSWORD,pass);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            CommonMethods.showToast(response.toString(), context);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ",error.getMessage());
                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    requestQueue.add(request);
                }
            }

        });
        /*signup*/
        final TextView signUp = (TextView) findViewById(R.id.signUpText);
        signUp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}
