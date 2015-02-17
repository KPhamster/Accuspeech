package edu.uci.accuspeech;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import edu.uci.accuspeech.util.AudioEffectUtil;


public class PastRecordingsActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_recordings);
    }

    private static final String LOG_TAG = "PlayControlFragment";

    private MediaPlayer mPlayer = null;
    private Button playButton;
    private boolean started = false;
    private BassBoost bass;
    SharedPreferences sharedPreferences;

    // Called when play button is pressed
    // TODO put media player logic in a service like RecordService but it would be PlayService
    public void onPlay(View view) {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
        if (!mPlayer.isPlaying()) {
            try {
                final String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.wav";
                mPlayer.setDataSource(fileName);
                if (AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_BASS_BOOST)) {
                    bass = new BassBoost(0, mPlayer.getAudioSessionId());
                    if(bass.getStrengthSupported()) {
                        if (sharedPreferences == null) {
                            sharedPreferences = getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
                        }
                        bass.setStrength((short) sharedPreferences.getInt(AudioEffectUtil.BASS_STRENGTH, 0));
                        Toast.makeText(this, "Bass: " + bass.getRoundedStrength(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "Setting Strength Not Supported", Toast.LENGTH_SHORT).show();
                    }
                }


                mPlayer.prepare();
                mPlayer.start();
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onStopPlaying(null);
                    }
                });
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
        }
    }

    public void onStopPlaying(View view) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }

    public void onPausePlaying(View view) {
        //stopPlaying();
    }

    @Override
    public void onPause() {
        super.onPause();
        onStopPlaying(null);
    }
}
