package com.example.datafile4.bookstore;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.NetworkResponse;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends AppCompatActivity {
    EditText usernameEdit;
    EditText passwordEdit;
    String KEY_USERNAME = "username";
    String KEY_PASSWORD = "password";
    private String PREF = "user_data";
    private String url = "https://amiraslan.azurewebsites.net/api/BookStore/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hiding action bar in login
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        SharedPreferences settings = getSharedPreferences(PREF,Context.MODE_PRIVATE);
        //if setting does not exists, getBoolean will return false
        if(settings.contains("cookie")){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
       }

         /*login*/
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEdit = (EditText) findViewById(R.id.email_login);
                passwordEdit = (EditText) findViewById(R.id.password_login);

                final String username = usernameEdit.getText().toString().trim();
                final String pass = passwordEdit.getText().toString().trim();
                final Context context = getApplicationContext();
                //check, if empty
                if (username.isEmpty() || pass.isEmpty()) {
                    CommonMethods.showToast("Email or Password is empty", context);
                } else {
                    //login process
                   HashMap<String,String>params = new HashMap<String, String>();
                    params.put(KEY_USERNAME,username);
                    params.put(KEY_PASSWORD,pass);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                            boolean success = response.optBoolean("success");
//                            if(success){
//                                Intent intent = new Intent(Login.this,MainActivity.class);
//                                startActivity(intent);
//                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ",error.getMessage());
                        }
                    }){
                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            Log.v("Cookie: ",response.headers.get("Set-Cookie"));
                            /*we must save user-g token in SharedPreferences*/
                            String cookie = response.headers.get("Set-Cookie");
                            if(!cookie.isEmpty()){
                                SharedPreferences sharedPrefs = getSharedPreferences(PREF,Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPrefs.edit();

                            /*extact user-g with regex*/
                                Pattern p = Pattern.compile("user-g=(.+?);");
                                Matcher m = p.matcher(cookie);
                                if(m.find()){
                                    editor.putString("cookie",m.group(1));
                                }
                                editor.putString("username",username);
                                editor.apply();
                                Intent intent = new Intent(Login.this,MainActivity.class);
                                startActivity(intent);
                            }
                            return super.parseNetworkResponse(response);
                        }
                    };
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
