package edu.uci.accuspeech.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uci.accuspeech.R;

public class DecibelMeterFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.decibel_meter, container, false);
        // TODO Ashish's code would go here
        return rootView;
    }
}
