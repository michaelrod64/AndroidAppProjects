package edu.miami.c10656908.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View.OnClickListener;

public class TwoButtonFragment extends Fragment {

    ToastMaker activity;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View theFragmentView;
        theFragmentView = inflater.inflate(R.layout.fragment_two_button, container, false);
        Button firstButton = (Button)theFragmentView.findViewById(R.id.the_first_button);
        Button secondButton = (Button)theFragmentView.findViewById(R.id.the_second_button);
        firstButton.setOnClickListener(clickHandler);
        secondButton.setOnClickListener(clickHandler);
        activity = (ToastMaker)getActivity();

        return theFragmentView;

    }

    public OnClickListener clickHandler = new OnClickListener() {

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.the_first_button:
                    activity.makeToast(getResources().getString(R.string.click_first_button));
                    break;
                case R.id.the_second_button:
                    activity.makeToast(getResources().getString(R.string.click_second_button));
            }
        }
    };

    public interface ToastMaker {
        public void makeToast(String s);
    }
}
