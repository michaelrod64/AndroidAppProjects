package edu.miami.c10656908.clockdate;

import android.app.Activity;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.GregorianCalendar;
import java.util.logging.LogRecord;


//=============================================================================
public class MainActivity extends Activity
        implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    //-----------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {

        DatePicker datePicker;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePicker = (DatePicker) findViewById(R.id.date_picker);
        datePicker.init(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), this);

        myProgresser.run();
        //setUNIXSeconds();
        //((TimePicker) findViewById(R.id.time_picker)).
          //      setOnTimeChangedListener(this);
    }

    //-----------------------------------------------------------------------------
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        //setUNIXSeconds();
    }

    //-----------------------------------------------------------------------------
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        //setUNIXSeconds();
    }

    //-----------------------------------------------------------------------------
    private final Runnable myProgresser = new Runnable() {

        private android.os.Handler myHandler = new Handler();

        public void run() {
            DatePicker datePicker;
            TimePicker timePicker;

            datePicker = (DatePicker) findViewById(R.id.date_picker);
            timePicker = (TimePicker) findViewById(R.id.time_picker);

            int currentMinute = timePicker.getCurrentMinute();
            int currentHour = timePicker.getCurrentHour();
            int currentDay = datePicker.getDayOfMonth();
            int currentMonth = datePicker.getMonth();
            int currentYear = datePicker.getYear();
            Log.i("IN PROGRESSOR", "minute: " + currentMinute + " hour: " + currentHour + " day: " + currentDay + " month "
                    + currentMonth + " year: " + currentYear);


            timePicker.setCurrentMinute(currentMinute + 1);
            if (currentMinute == 59) {

                if(currentHour != 23) {
                    timePicker.setCurrentHour(currentHour + 1);
                }
                else {
                    timePicker.setCurrentHour(0);
                    if(currentMonth == getResources().getInteger(R.integer.December) && currentDay == 31) {
                        datePicker.updateDate(currentYear + 1, 0, 1);
                    }
                    else if((currentMonth == getResources().getInteger(R.integer.January) ||
                            currentMonth == getResources().getInteger(R.integer.March) ||
                            currentMonth == getResources().getInteger(R.integer.May) ||
                            currentMonth == getResources().getInteger(R.integer.July) ||
                            currentMonth == getResources().getInteger(R.integer.August) ||
                            currentMonth == getResources().getInteger(R.integer.October))
                            && currentDay == 31) {
                        datePicker.updateDate(currentYear, currentMonth + 1, 1);
                    }
                    else if((currentMonth == getResources().getInteger(R.integer.April) ||
                            currentMonth == getResources().getInteger(R.integer.June) ||
                            currentMonth == getResources().getInteger(R.integer.September) ||
                            currentMonth == getResources().getInteger(R.integer.November))
                            && (currentDay == 30)) {
                        datePicker.updateDate(currentYear, currentMonth + 1, 1);
                    }
                    else if((currentMonth == getResources().getInteger(R.integer.February))){
                        if(isLeapYear(currentYear) && (currentDay == 29)) {
                            datePicker.updateDate(currentYear, currentMonth + 1, 1);
                        }
                        else if(!isLeapYear(currentYear) && (currentDay == 28)) {
                            datePicker.updateDate(currentYear, currentMonth + 1, 1);
                        }
                    }
                    else {
                        datePicker.updateDate(currentYear, currentMonth, currentDay + 1);
                    }
                }
                //if (currentHour == 23) {
                  //  datePicker.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth() + 1);
                //}

            }

            if (!myHandler.postDelayed(myProgresser, getResources().getInteger(R.integer.Minute))) {
                Log.e("ERROR", "Cannot postDelayed");
            }
        }
    };


    private boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
    }
}

//-----------------------------------------------------------------------------

//=============================================================================