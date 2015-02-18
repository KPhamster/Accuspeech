package edu.uci.accuspeech.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.audiofx.BassBoost;
import android.widget.Toast;

import edu.uci.accuspeech.R;
import edu.uci.accuspeech.util.AudioEffectUtil;

public class BassBoostControlFragment extends Fragment {

    BassBoost bb = new BassBoost(0,0);
    SeekBar bassBar;
    int bassLevel;
    public SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use shared prefs to save audio effect values
        sharedPreferences = getActivity().getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.bass_boost_control, container, false);
        ViewGroup control = (ViewGroup) rootView.findViewById(R.id.control);
        View notSupportedText = rootView.findViewById(R.id.not_supported);

        if (!AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_BASS_BOOST)) {
            notSupportedText.setVisibility(View.VISIBLE);
            control.setVisibility(View.GONE);
        } else {
            View sliderControl = inflater.inflate(R.layout.seek_bar_control, control, false);
            control.addView(sliderControl);
            bassBar = (SeekBar)sliderControl.findViewById(R.id.seek_bar);
            TextView seekBarName = (TextView) sliderControl.findViewById(R.id.seek_bar_name);
            TextView lowerBound = (TextView) sliderControl.findViewById(R.id.seek_bar_lower_bound);
            TextView upperBound = (TextView) sliderControl.findViewById(R.id.seek_bar_upper_bound);
            // setting this to gone because there probably is only one bass boost control
            seekBarName.setVisibility(View.GONE);
            lowerBound.setText("0");
            upperBound.setText("1000");
            bassBar.setMax(1000);
            bassBar.setProgress(sharedPreferences.getInt(AudioEffectUtil.BASS_STRENGTH, 0));
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        bassLevel = bassBar.getProgress();
        // We are saving this on pause so we only save once instead of saving it everytime
        // it is changed which slows down the main thread and makes control look bad
        sharedPreferences.edit().putInt(AudioEffectUtil.BASS_STRENGTH, bassLevel).commit();
    }
}
