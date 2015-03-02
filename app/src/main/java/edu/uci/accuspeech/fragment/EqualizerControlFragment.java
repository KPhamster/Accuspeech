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
import android.media.audiofx.Equalizer;

import edu.uci.accuspeech.R;
import edu.uci.accuspeech.util.AudioEffectUtil;

public class EqualizerControlFragment extends Fragment {

    Equalizer eq = new Equalizer(0, 0);
    SeekBar eqBar1;
    SeekBar eqBar2;
    SeekBar eqBar3;
    SeekBar eqBar4;
    public SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use shared prefs to save audio effect values
        sharedPreferences = getActivity().getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.equalizer_control, container, false);
        ViewGroup control = (ViewGroup) rootView.findViewById(R.id.control);
        View notSupportedText = rootView.findViewById(R.id.not_supported);

        if (!AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_EQUALIZER)) {
            notSupportedText.setVisibility(View.VISIBLE);
            control.setVisibility(View.GONE);
        } else {
            // do this for as many sliders are needed
            /* Note: Each equalizer bar represents the "frequency band", which filters
               the audio signal falling within their specific frequency range.
               Source: http://www.101apps.co.za/index.php/articles/perfect-sound-using-the-equalizer-effect-a-tutorial.html

               Note2: How many frequency bands are needed for a "bare-bones" equalizer? "for experienced
               audio engineers 4 bands are enough." Thus, we have 4 different frequency bands.
               Source: http://blog.audioforge.org/2012/12/tester.html
             */
            eq.setEnabled(true);
            final short eqIndex1 = 1;
            final short eqIndex2 = 2;
            final short eqIndex3 = 3;
            final short eqIndex4 = 4;
            final short lowerBandRange = eq.getBandLevelRange()[0];
            final short upperBandRange = eq.getBandLevelRange()[1];
            final int maxEqualizerLevel = upperBandRange - lowerBandRange;

            eq.setBandLevel(eqIndex1, (short) sharedPreferences.getInt(AudioEffectUtil.EQUALIZER_LEVEL_1, 0));
            eq.setBandLevel(eqIndex2, (short) sharedPreferences.getInt(AudioEffectUtil.EQUALIZER_LEVEL_2, 0));
            eq.setBandLevel(eqIndex3, (short) sharedPreferences.getInt(AudioEffectUtil.EQUALIZER_LEVEL_3, 0));
            eq.setBandLevel(eqIndex4, (short) sharedPreferences.getInt(AudioEffectUtil.EQUALIZER_LEVEL_4, 0));

            // Equalizer Bar 1
            View sliderControl = inflater.inflate(R.layout.seek_bar_control, control, false);
            control.addView(sliderControl);
            eqBar1 = (SeekBar)sliderControl.findViewById(R.id.seek_bar);
            final TextView bandName = (TextView) sliderControl.findViewById(R.id.seek_bar_name);
            TextView lowerBound = (TextView) sliderControl.findViewById(R.id.seek_bar_lower_bound);
            TextView upperBound = (TextView) sliderControl.findViewById(R.id.seek_bar_upper_bound);
            bandName.setText("Frequency Band 1 = " + ((eq.getCenterFreq(eqIndex1)) / 1000) + "Hz");
            lowerBound.setText((lowerBandRange / 100) + "dB");
            upperBound.setText((upperBandRange / 100) + "dB");
            eqBar1.setMax(maxEqualizerLevel);
            eqBar1.setProgress(eq.getBandLevel(eqIndex1));
            eqBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    eq.setBandLevel(eqIndex1, (short) (progress + lowerBandRange));
                    bandName.setText("Frequency Band 1 = " + (progress + lowerBandRange) + "Hz");

                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    //not used
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //not used
                }

            });

            // Equalizer Bar 2
            View sliderControl2 = inflater.inflate(R.layout.seek_bar_control, control, false);
            control.addView(sliderControl2);
            eqBar2 = (SeekBar)sliderControl2.findViewById(R.id.seek_bar);
            final TextView bandName2 = (TextView) sliderControl2.findViewById(R.id.seek_bar_name);
            TextView lowerBound2 = (TextView) sliderControl2.findViewById(R.id.seek_bar_lower_bound);
            TextView upperBound2 = (TextView) sliderControl2.findViewById(R.id.seek_bar_upper_bound);
            bandName2.setText("Frequency Band 2 = " + ((eq.getCenterFreq(eqIndex2)) / 1000) + "Hz");
            lowerBound2.setText((lowerBandRange / 100) + "dB");
            upperBound2.setText((upperBandRange / 100) + "dB");
            eqBar2.setMax(maxEqualizerLevel);
            eqBar2.setProgress(eq.getBandLevel(eqIndex2));
            eqBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    eq.setBandLevel(eqIndex2, (short) (progress + lowerBandRange));
                    bandName2.setText("Frequency Band 2 = " + (progress + lowerBandRange) + "Hz");

                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    //not used
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //not used
                }

            });
            // Equalizer Bar 3
            View sliderControl3 = inflater.inflate(R.layout.seek_bar_control, control, false);
            control.addView(sliderControl3);
            eqBar3 = (SeekBar)sliderControl3.findViewById(R.id.seek_bar);
            final TextView bandName3 = (TextView) sliderControl3.findViewById(R.id.seek_bar_name);
            TextView lowerBound3 = (TextView) sliderControl3.findViewById(R.id.seek_bar_lower_bound);
            TextView upperBound3 = (TextView) sliderControl3.findViewById(R.id.seek_bar_upper_bound);
            bandName3.setText("Frequency Band 3 = " + ((eq.getCenterFreq(eqIndex3)) / 1000) + "Hz");
            lowerBound3.setText((lowerBandRange / 100) + "dB");
            upperBound3.setText((upperBandRange / 100) + "dB");
            eqBar3.setMax(maxEqualizerLevel);
            eqBar3.setProgress(eq.getBandLevel(eqIndex3));
            eqBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    eq.setBandLevel(eqIndex3, (short) (progress + lowerBandRange));
                    bandName3.setText("Frequency Band 3 = " + (progress + lowerBandRange) + "Hz");

                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    //not used
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //not used
                }

            });

            // Equalizer Bar 4
            View sliderControl4 = inflater.inflate(R.layout.seek_bar_control, control, false);
            control.addView(sliderControl4);
            eqBar4 = (SeekBar)sliderControl4.findViewById(R.id.seek_bar);
            final TextView bandName4 = (TextView) sliderControl4.findViewById(R.id.seek_bar_name);
            TextView lowerBound4 = (TextView) sliderControl4.findViewById(R.id.seek_bar_lower_bound);
            TextView upperBound4 = (TextView) sliderControl4.findViewById(R.id.seek_bar_upper_bound);
            bandName4.setText("Frequency Band 4 = " + ((eq.getCenterFreq(eqIndex4)) / 1000) + "Hz");
            lowerBound4.setText((lowerBandRange / 100) + "dB");
            upperBound4.setText((upperBandRange / 100) + "dB");
            eqBar4.setMax(maxEqualizerLevel);
            eqBar4.setProgress(eq.getBandLevel(eqIndex4));
            eqBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    eq.setBandLevel(eqIndex4, (short) (progress + lowerBandRange));
                    bandName4.setText("Frequency Band 4 = " + (progress + lowerBandRange) + "Hz");

                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    //not used
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    //not used
                }

            });
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(eqBar1 != null) {
            int eqLevel1 = eq.getBandLevel((short)1);
            int eqLevel2 = eq.getBandLevel((short)2);
            int eqLevel3 = eq.getBandLevel((short)3);
            int eqLevel4 = eq.getBandLevel((short)4);
            // We are saving this on pause so we only save once instead of saving it every time
            // it is changed which slows down the main thread and makes control look bad
            sharedPreferences.edit().putInt(AudioEffectUtil.EQUALIZER_LEVEL_1, eqLevel1).apply();
            sharedPreferences.edit().putInt(AudioEffectUtil.EQUALIZER_LEVEL_2, eqLevel2).apply();
            sharedPreferences.edit().putInt(AudioEffectUtil.EQUALIZER_LEVEL_3, eqLevel3).apply();
            sharedPreferences.edit().putInt(AudioEffectUtil.EQUALIZER_LEVEL_4, eqLevel4).apply();
        }
    }
}













