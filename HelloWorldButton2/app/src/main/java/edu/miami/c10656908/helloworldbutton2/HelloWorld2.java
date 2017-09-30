package edu.miami.c10656908.helloworldbutton2;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HelloWorld2 extends AppCompatActivity {

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world2);
        Log.i("IN onCreate of HelloWorld2", "Created OK");
    }
}
