package edu.miami.c10656908.CountMe;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static String PREFERENCES_NAME = "DataPreferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView)findViewById(R.id.textview);
        SharedPreferences persistentData;
        persistentData = getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE);

        int timesRun = persistentData.getInt("times_run",
                0);
        textView.setText("" + timesRun);


        SharedPreferences.Editor editor;
        editor = persistentData.edit();
        editor.putInt("times_run", timesRun + 1);
        editor.commit();

    }
}
