package edu.uci.accuspeech.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import edu.uci.accuspeech.service.RecordService;

/**
 * This is the UI for starting and stopping a recording. Also this
 * contains a button for playing the sound that was just recorded.
 */
public class RecordControlFragment extends Fragment
{
    private static final String LOG_TAG = "AudioRecordTest";
    private RecordButton mRecordButton = null;
    private PlayButton   mPlayButton = null;
    private boolean autoGainControl = true;

    private MediaPlayer mPlayer = null;

    RecordService mService;
    boolean mBound = false;

    // When GainControlFragment toggles AutomaticGainControl on and off it will be received by this
    // broadcast receiver.
    final public BroadcastReceiver autoGainControlRecv = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            autoGainControl = intent.getBooleanExtra(GainControlFragment.AUTO_GAIN_CONTROL_KEY, false);
        }
    };
    final public IntentFilter autoGainControlFilter = new IntentFilter(GainControlFragment.AUTO_GAIN_CONTROL_ACTION);
    /**
     * Copied from Android documentation on how to bind to a service
     * We need a way to connect to/use RecordService from this class because
     * this class are the controls for RecordService to work.
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            RecordService.LocalBinder binder = (RecordService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            // Since we could be recording and this is the first time we connect to the service
            // we will update the button text.
            mRecordButton.updateText();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO use a xml layout, this was copied from Android documentation
        LinearLayout ll = new LinearLayout(getActivity());
        mRecordButton = new RecordButton(getActivity());
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(getActivity());
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        return ll;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Intent that is used RecordService
        Intent intent = new Intent(getActivity(), RecordService.class);

        // Make sure record service is up and running before we try to connect to it
        getActivity().startService(intent);

        // Bind to LocalService
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(autoGainControlRecv, autoGainControlFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlaying();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(autoGainControlRecv);
    }

    /*
     * Most of the code below this point was copied and slightly modified from android documentation.
     * http://developer.android.com/guide/topics/media/audio-capture.html
     */

    // Called when record button is pressed
    private void onRecord(boolean isRecording) {
        if (!isRecording) {
            try {
                stopPlaying();
                mService.startRecording(autoGainControl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mService.stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Called when play button is pressed
    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    // Plays the wav file that was just recorded
    private void startPlaying() {
        if (mService.isRecording()) {
            try {
                mService.stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecordButton.updateText();
        }
        mPlayer = new MediaPlayer();
        try {
            final String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.wav";
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    RecordControlFragment.this.stopPlaying();
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
        mPlayButton.updateText();
    }


    class RecordButton extends Button {

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                boolean isRecording = mService.isRecording();
                onRecord(isRecording);
                updateText();
            }
        };

        public void updateText() {
            if (mService.isRecording()) {
                setText("Stop recording");
            } else {
                setText("Start recording");
            }
        }

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mPlayer == null);
                updateText();
            }
        };

        public void updateText() {
            if (mPlayer != null && mPlayer.isPlaying()) {
                setText("Stop playing");
            } else {
                setText("Start playing");
            }
        }

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }
}