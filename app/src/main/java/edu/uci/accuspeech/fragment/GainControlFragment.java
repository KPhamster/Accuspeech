package edu.uci.accuspeech.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.uci.accuspeech.R;
import edu.uci.accuspeech.util.AudioEffectUtil;

/**
 * UI for controls for turning on and off automatic gain control.
 */
public class GainControlFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use shared prefs to save audio effect values
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.auto_gain_control, container, false);
        //Retrieves the radioGroup view to add listeners
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioButtons);
        View notSupportedText = rootView.findViewById(R.id.not_supported);
        //Creates the subtitle that says the current state of the effect
        final TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);
        if (!AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_AGC)) {
            notSupportedText.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        } else {
            if (sharedPreferences.getBoolean(AudioEffectUtil.AUTO_GAIN_CONTROL_KEY, false)) {
                radioGroup.check(R.id.gainControlOff);
                subtitle.setText("is currently Off");
            } else {
                radioGroup.check(R.id.gainControlOn);
                subtitle.setText("is currently On");
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.gainControlOn:
                            sharedPreferences.edit().putBoolean(AudioEffectUtil.AUTO_GAIN_CONTROL_KEY, true).commit();
                            subtitle.setText("is currently On");
                            break;
                        case R.id.gainControlOff:
                            sharedPreferences.edit().putBoolean(AudioEffectUtil.AUTO_GAIN_CONTROL_KEY, false).commit();
                            subtitle.setText("is currently Off");
                            break;
                    }
                }
            });
        }
        return rootView;
    }
}
