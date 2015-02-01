package edu.uci.accuspeech.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import edu.uci.accuspeech.R;

/**
 * UI for controls for turning on and off automatic gain control.
 */
public class GainControlFragment extends Fragment {

    public static final String AUTO_GAIN_CONTROL_KEY = "AUTO_GAIN_CONTROL_KEY";
    public static final String AUTO_GAIN_CONTROL_ACTION = "AUTO_GAIN_CONTROL_ACTION";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //Retrieves the radioGroup view to add listeners
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioButtons);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //TODO Change radio buttons to actually control gain control
                final Intent toggleIntent = new Intent(AUTO_GAIN_CONTROL_ACTION);
                switch (checkedId){
                    case R.id.gainControlOn:
                        Log.d("GainControlFragment", "Gain Control On");
                        toggleIntent.putExtra(AUTO_GAIN_CONTROL_KEY, true);
                        break;
                    case R.id.gainControlOff:
                        Log.d("GainControlFragment", "Gain Control Off");
                        toggleIntent.putExtra(AUTO_GAIN_CONTROL_KEY, false);
                        break;
                }

                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(toggleIntent);
            }
        });
        return rootView;
    }
}
