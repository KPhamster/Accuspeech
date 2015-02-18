package edu.uci.accuspeech;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;



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
