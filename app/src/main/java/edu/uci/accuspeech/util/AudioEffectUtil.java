package edu.uci.accuspeech.util;

import android.media.audiofx.AudioEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AudioEffectUtil {

    public static final String SETTINGS_PREFS = "RecordServiceSettings";
    public static final String AUTO_GAIN_CONTROL_KEY = "AUTO_GAIN_CONTROL_KEY";
    public static final String BASS_STRENGTH = "PARAM_STRENGTH";
    public static final String NOISE_SUPPRESSION_KEY = "NOISE_SUPPRESSION_KEY";
    public static final String BASS_BOOST_DEFAULT_ENABLED = "BASS_BOOST_DEFAULT_ENABLED";
    public static final String EQUALIZER_DEFAULT_ENABLED = "EQUALIZER_DEFAULT_ENABLED";

    /**
     *  Supporter method for checking if audio effect works
     * @param effect
     *  The audio effect to check against
     * @return
     *  True if it is supported
     */
    static public boolean isSupported(UUID effect) {
        AudioEffect.Descriptor[] effects = AudioEffect.queryEffects();
        for(AudioEffect.Descriptor audioDesc : effects) {
            if (audioDesc.type.compareTo(effect) == 0) {
                return true;
            }
        }
        return false;
    }
}
