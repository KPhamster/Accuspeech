package edu.uci.accuspeech.util;

import android.media.audiofx.AudioEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AudioEffectUtil {

    public static final String SETTINGS_PREFS = "RecordServiceSettings";
    public static final String AUTO_GAIN_CONTROL_KEY = "AUTO_GAIN_CONTROL_KEY";

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
