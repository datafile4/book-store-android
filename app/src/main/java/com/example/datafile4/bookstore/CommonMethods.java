package com.example.datafile4.bookstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by datafile4 on 8/15/16.
 */
public class CommonMethods {
    public static void showToast(String message, Context context) {
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
