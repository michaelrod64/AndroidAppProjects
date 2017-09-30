package edu.miami.c10656908.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TwoButtonFragment.ToastMaker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void myClickHandler(View view) {
        switch (view.getId()) {
            case R.id.one_button_fragment:
                makeToast(getResources().getString(R.string.click_one_button));
        }
    }

    @Override
    public void makeToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
