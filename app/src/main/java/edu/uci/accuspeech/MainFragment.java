package edu.uci.accuspeech;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by Sarah on 1/17/2015.
 * UI for controls for turning on and off automatic gain control.
 */
public class MainFragment extends Fragment {

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
                switch (checkedId){
                    case R.id.gainControlOn:
                        Log.d("main fragment", "Gain Control On");
                        break;
                    case R.id.gainControlOff:
                        Log.d("main fragment", "Gain Control Off");
                        break;
                }
            }
        });
        return rootView;
    }
}
