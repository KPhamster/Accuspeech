package edu.uci.accuspeech.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uci.accuspeech.R;
import edu.uci.accuspeech.util.AudioEffectUtil;

public class EqualizerControlFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use shared prefs to save audio effect values
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.equalizer_control, container, false);
        ViewGroup control = (ViewGroup) rootView.findViewById(R.id.control);
        View notSupportedText = rootView.findViewById(R.id.not_supported);
        if (!AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_EQUALIZER)) {
            notSupportedText.setVisibility(View.VISIBLE);
            control.setVisibility(View.GONE);
        } else {
            // TODO Kevin's code would go here
            // do this for as many sliders are needed
            for (int i = 0; i < 2; i++) {
                View sliderControl = inflater.inflate(R.layout.seek_bar_control, control, false);
                control.addView(sliderControl);
                TextView bandName = (TextView) sliderControl.findViewById(R.id.seek_bar_name);
                TextView lowerBound = (TextView) sliderControl.findViewById(R.id.seek_bar_lower_bound);
                TextView upperBound = (TextView) sliderControl.findViewById(R.id.seek_bar_upper_bound);
                bandName.setText("Example Band Slider " + (i+1));
                lowerBound.setText("-12");
                upperBound.setText("12");
            }
        }
        return rootView;
    }
}
