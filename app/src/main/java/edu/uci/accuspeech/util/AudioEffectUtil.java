package edu.uci.accuspeech.util;

import android.media.audiofx.AudioEffect;
import java.util.UUID;

public class AudioEffectUtil {

    public static final String SETTINGS_PREFS = "RecordServiceSettings";
    public static final String AUTO_GAIN_CONTROL_KEY = "AUTO_GAIN_CONTROL_KEY";
    public static final String BASS_STRENGTH = "PARAM_STRENGTH";
    public static final String NOISE_SUPPRESSION_KEY = "NOISE_SUPPRESSION_KEY";
    public static final String BASS_BOOST_DEFAULT_ENABLED = "BASS_BOOST_DEFAULT_ENABLED";
    public static final String EQUALIZER_DEFAULT_ENABLED = "EQUALIZER_DEFAULT_ENABLED";
    public static final String EQUALIZER_LEVEL_1 = "EQUALIZER_LEVEL_1";
    public static final String EQUALIZER_LEVEL_2 = "EQUALIZER_LEVEL_2";
    public static final String EQUALIZER_LEVEL_3 = "EQUALIZER_LEVEL_3";
    public static final String EQUALIZER_LEVEL_4 = "EQUALIZER_LEVEL_4";
    //public static final String EQUALIZER_LEVEL_5 = "EQUALIZER_LEVEL_5";

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
