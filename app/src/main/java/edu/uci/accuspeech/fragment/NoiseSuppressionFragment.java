package edu.uci.accuspeech.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import edu.uci.accuspeech.R;
import edu.uci.accuspeech.util.AudioEffectUtil;


public class NoiseSuppressionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use shared prefs to save audio effect values
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.noise_suppression, container, false);
        //Retrieves the radioGroup view to add listeners
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.radioButtons);
        View notSupportedText = rootView.findViewById(R.id.not_supported);
        //Creates the subtitle that says the current state of the effect
        final TextView subtitle = (TextView) rootView.findViewById(R.id.subtitle);
        if (!AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_NS)) {
            notSupportedText.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        } else {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.noiseSuppressionOn:
                            sharedPreferences.edit().putBoolean(AudioEffectUtil.NOISE_SUPPRESSION_KEY, true).commit();
                            subtitle.setText("is currently On");
                            break;
                        case R.id.noiseSuppressionOff:
                            sharedPreferences.edit().putBoolean(AudioEffectUtil.NOISE_SUPPRESSION_KEY, false).commit();
                            subtitle.setText("is currently Off");
                            break;
                    }
                }
            });
            radioGroup.clearCheck();
            if (sharedPreferences.getBoolean(AudioEffectUtil.NOISE_SUPPRESSION_KEY, false)) {
                radioGroup.check(R.id.noiseSuppressionOff);
                subtitle.setText("is currently Off");
            } else {
                radioGroup.check(R.id.noiseSuppressionOn);
                subtitle.setText("is currently On");
            }
        }
        return rootView;
    }
}
