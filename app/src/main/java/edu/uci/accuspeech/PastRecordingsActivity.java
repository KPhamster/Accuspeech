package edu.uci.accuspeech;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.uci.accuspeech.fragment.PastRecordingsListFragment;
import edu.uci.accuspeech.service.PlayService;


public class PastRecordingsActivity extends NavigationActivity {

    private Button playButton;
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
            mService.setupAudioFile(fileName + ".wav");
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

            // Since we could be recording and this is the first time we connect to the service
            // we will update the button text.
            updatePlayerControls();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

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
        if (mService == null) {
            return;
        }
        mService.stop();
        updatePlayerControls();
    }

    public void onPausePlaying(View view) {
        if (mService == null) {
            return;
        }
        mService.pause();
        updatePlayerControls();
    }

    public void onPastRecordingSelected(View view) {
        View v = findViewById(R.id.recordings_list).findViewWithTag("selected");
        if (v != null) {
            v.setTag("");
            v.setBackgroundColor(getResources().getColor(R.color.white));
        }
        TextView name = (TextView) view.findViewById(R.id.name);
        loadFile((String) name.getText());
        view.setBackgroundColor(getResources().getColor(R.color.grey));
        view.setTag("selected");
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(playFinishedReceiver, playFinishedFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
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
