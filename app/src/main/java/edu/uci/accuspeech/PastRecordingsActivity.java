package edu.uci.accuspeech;

import android.media.MediaPlayer;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import edu.uci.accuspeech.fragment.PastRecordingsListFragment;
import edu.uci.accuspeech.service.PlayService;


public class PastRecordingsActivity extends NavigationActivity {

    private Button stopButton;
    private Button pauseButton;
    private TextView currentPlayingFileName;
    private ProgressBar progressBar;

    private BroadcastReceiver playFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updatePlayerControls();
        }
    };
    private IntentFilter playFinishedFilter = new IntentFilter(PlayService.ACTION_PLAY_STOPPED);

    private void loadFile(String fileName) {
        if (mService != null) {
            mService.reset();
            mService.setupAudioFile(fileName);
            updatePlayerControls();
        }
    }

    // timer logic to show length of played audio
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mService != null && !playButton.isEnabled()) {
                progressBar.setProgress(mService.getCurrentPosition());
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 16);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_recordings);
        playButton = (Button) findViewById(R.id.playButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        currentPlayingFileName = (TextView) findViewById(R.id.file_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PastRecordingsListFragment())
                    .commit();
        }
    }

    private static final String LOG_TAG = "PlayControlFragment";

    private MediaPlayer mPlayer = null;
    private Button playButton;
    private boolean started = false;
    private PlayService mService;
    private boolean mBound;
    /**
     * Copied from Android documentation on how to bind to a service
     * We need a way to connect to/use PlayService from this class because
     * this class are the controls for PlayService to work.
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayService.LocalBinder binder = (PlayService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            // Called when play button is pressed
            // TODO put media player logic in a service like RecordService but it would be PlayService
        public void onPlay(View view) {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
                // Since we could be recording and this is the first time we connect to the service
                // we will update the button text.
                updatePlayerControls();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
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

            private void updatePlayerControls() {
                if (mService != null && !TextUtils.isEmpty(mService.getFileName())) {
                    currentPlayingFileName.setText(mService.getFileName());
                    if (mService.isPlaying()) {
                        playButton.setEnabled(false);
                        pauseButton.setEnabled(true);
                        stopButton.setEnabled(true);
                    } else if (mService.isPaused()) {
                        playButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(true);
                    } else {
                        progressBar.setProgress(0);
                        playButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                        stopButton.setEnabled(false);
                    }
                } else {
                    playButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    stopButton.setEnabled(false);
                }
            }

            public void onPlay(View view) {
                if (mService == null) {
                    return;
                }
                mService.play();
                updatePlayerControls();
                progressBar.setMax(mService.getDuration());
                mHandler.sendMessage(mHandler.obtainMessage());
            }

            public void onStopPlaying(View view) {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                    if (mService == null) {
                        return;
                    }
                    mPlayer = null;
                    mService.stop();
                    updatePlayerControls();
                }

            public void onPausePlaying(View view) {
                //stopPlaying();
                if (mService == null) {
                    return;
                }
                mService.pause();
                updatePlayerControls();
            }

            public void onPastRecordingSelected(View view) {
                TextView name = (TextView) view.findViewById(R.id.name);
                loadFile((String) name.getText());
            }
            @Override
            public void onResume() {
                super.onResume();
                LocalBroadcastManager.getInstance(this).registerReceiver(playFinishedReceiver, playFinishedFilter);
            }

            @Override
            public void onPause() {
                super.onPause();
                onStopPlaying(null);
                LocalBroadcastManager.getInstance(this).unregisterReceiver(playFinishedReceiver);
            }

            @Override
            public void onStart() {
                super.onStart();
                // Intent that is used PlayService
                Intent intent = new Intent(this, PlayService.class);

                // Make sure PlayService is up and running before we try to connect to it
                startService(intent);

                // Bind to LocalService
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }

            @Override
            public void onStop() {
                super.onStop();
                // Unbind from the service
                if (mBound) {
                    unbindService(mConnection);
                    mBound = false;
                }
            }
        }
