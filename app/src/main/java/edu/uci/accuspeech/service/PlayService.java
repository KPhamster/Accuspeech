package edu.uci.accuspeech.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.BassBoost;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import edu.uci.accuspeech.util.AudioEffectUtil;

public class PlayService extends AudioService {

    private static final String LOG_TAG = "PlayService";
    private final IBinder mBinder = new LocalBinder();

    private SharedPreferences sharedPreferences;

    private MediaPlayer mPlayer;
    private boolean isPaused = false;
    private String currentFileName;

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public PlayService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayService.this;
        }
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public String getFileName() {
        return currentFileName;
    }

    public int getCurrentPosition() {
        if (mPlayer != null) {
            return mPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (mPlayer != null) {
            return mPlayer.getDuration();
        }
        return 0;
    }

    public void setupAudioFile(String fileName) {
        currentFileName = fileName;
        if (mPlayer != null) {
            reset();
        }
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(PAST_RECORDINGS_FILE_PATH + fileName);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LocalBroadcastManager.getInstance(PlayService.this).sendBroadcast(new Intent(ACTION_PLAY_STOPPED));
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void play() {
        if (mPlayer == null) {
            return;
        }
        if (!mPlayer.isPlaying()) {
            setupAudioSettings(mPlayer.getAudioSessionId());
            isPaused = false;
            mPlayer.start();
        }
    }

    private void setupAudioSettings(int sessionId) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
        }
        if (AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_BASS_BOOST)) {
            if (!sharedPreferences.getBoolean(AudioEffectUtil.BASS_BOOST_DEFAULT_ENABLED, false)){
                BassBoost bass = new BassBoost(0, sessionId);
                if(bass.getStrengthSupported()) {
                    bass.setStrength((short) sharedPreferences.getInt(AudioEffectUtil.BASS_STRENGTH, 0));
                }
                else{
                    Toast.makeText(this, "Bass Boost Setting Strength Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_EQUALIZER)) {
            if (!sharedPreferences.getBoolean(AudioEffectUtil.EQUALIZER_DEFAULT_ENABLED, false)) {
                // TODO Michael's implementation code here.
            }
        }

    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            try {
                mPlayer.prepare();
                mPlayer.seekTo(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            isPaused = false;
        }
    }

    public void reset() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            isPaused = false;
            currentFileName = null;
        }
        mPlayer = null;
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            isPaused = true;
        }
    }
}
