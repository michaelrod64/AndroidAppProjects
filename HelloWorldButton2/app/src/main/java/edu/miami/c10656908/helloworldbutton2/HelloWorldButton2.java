package edu.miami.c10656908.helloworldbutton2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HelloWorldButton2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world_button2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_world_button2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //switch statement to confirm that hello_world_menu_item was pressed
        switch (item.getItemId()) {
            case R.id.hello_world_menu_item:
                Intent nextActivity;

                nextActivity = new Intent();
                //sets activity to be started by package name and then activity name
                nextActivity.setClassName("edu.miami.c10656908.helloworldbutton2",
                        "edu.miami.c10656908.helloworldbutton2.HelloWorld2");
                startActivity(nextActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }


//        Vibrator buzzer;
//        final long[] menuBuzz =
//                {0,250,50,250,500,250,50,250,500,250,50,250,500,250,50,250};
//
//        switch(item.getItemId()) {
//            case R.id.buzz_menu_item:
//                buzzer = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                buzzer.vibrate(menuBuzz, -1);
//                return true;
//            case R.id.popup_menu_item:
//                Toast.makeText(this, "Toast is ready!", Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);



    public void myClickHandler(View view) {

        Intent nextActivity;

        switch (view.getId()) {
//            case R.id.the_button:
//                nextActivity = new Intent();
//                nextActivity.setClassName("edu.miami.c10656908.helloworldbutton2",
//                        "edu.miami.c10656908.helloworldbutton2.HelloWorld2");
//                startActivity(nextActivity);
//                break;

            case R.id.buzz_button:
                //array to store pattern of vibration
                final long[] menuBuzz =
                        {0,250,50,250,500,250,50,250,500,250,50,250,500,250,50,250};
                Vibrator buzzer;

                buzzer = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //-1 to tell the phone not to repeat the buzzing pattern once finished
                buzzer.vibrate(menuBuzz, -1);
                Log.i("IN myClickHandler of HelloWorldButton2", "Buzz successful");
                break;
            case R.id.popup_button:
                Toast.makeText(this, "Toast is ready!", Toast.LENGTH_LONG).show();
                Log.i("IN myClickHandler of HelloWorldButton2", "Popup successful");
                break;
            default:
                break;
        }
    }




}
