package edu.miami.c10656908.buttonspeech;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

//=============================================================================
public class MainActivity extends Activity implements TextToSpeech.OnInitListener,
        TextToSpeech.OnUtteranceCompletedListener {
    //---From API 21 onwards, use UtteranceProgressListener instead.
//-----------------------------------------------------------------------------
    private TextToSpeech mySpeaker;
    public int NOUOQ; //number of utterances on queue
    //-----------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySpeaker = new TextToSpeech(this,this);
        mySpeaker.setOnUtteranceCompletedListener(this);
    }
    //-----------------------------------------------------------------------------
    @Override
    public void onDestroy() {

        super.onDestroy();
        myHandler.removeCallbacks(myUtteranceCompleted);
        mySpeaker.shutdown();
    }
    //-----------------------------------------------------------------------------
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            NOUOQ = 0;
            myHandler.postDelayed(myUtteranceCompleted, 5000);

        } else {

        }
    }
    //-----------------------------------------------------------------------------
    public void myClickListener(View view) {

        String whatToSay;
        HashMap<String,String> speechParameters;

        if(NOUOQ <= 0) {
            myHandler.removeCallbacks(myUtteranceCompleted);
        }
        NOUOQ++;


                whatToSay = ((Button)view).getText().toString();
                if (whatToSay != null && whatToSay.length() > 0) {
                    speechParameters = new HashMap<String,String>();
                    speechParameters.put(
                            TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"WHAT_I_SAID");
                    mySpeaker.speak(whatToSay,TextToSpeech.QUEUE_ADD,
                            speechParameters);
                } else {
                    Toast.makeText(this,"Nothing to say",Toast.LENGTH_SHORT).show();
                }
    }
    //-----------------------------------------------------------------------------
    public void onUtteranceCompleted(String utteranceId) {

//----Can't do anything here because it's another thread
        NOUOQ--;
        if(NOUOQ <= 0) {
            myHandler.postDelayed(myUtteranceCompleted,5000);
        }
    }
    //-----------------------------------------------------------------------------
    private Handler myHandler = new Handler();

    private final Runnable myUtteranceCompleted = new Runnable() {

        public void run() {
            String whatToSay;
            HashMap<String,String> speechParameters;

            whatToSay = "I have nothing to say";
            speechParameters = new HashMap<String,String>();
            speechParameters.put(
                    TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"WHAT_I_SAID");
            mySpeaker.speak(whatToSay,TextToSpeech.QUEUE_ADD,
                    speechParameters);
        }
    };
//-----------------------------------------------------------------------------
}
//=============================================================================

