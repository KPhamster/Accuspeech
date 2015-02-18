package edu.uci.accuspeech;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import edu.uci.accuspeech.service.RecordService;

public class RecordActivity extends NavigationActivity {

    private Button recordButton;
    private TextView timerText;
    private long startTime;
    RecordService mService;
    boolean mBound = false;

    // timer logic to show length of recording
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (isTimerRunning) {
                long elapsedTimeInSeconds = (SystemClock.elapsedRealtime() - startTime) / 1000;
                long minutes = elapsedTimeInSeconds / 60;
                long seconds = elapsedTimeInSeconds % 60;
                timerText.setText(String.format("%d:%02d", minutes, seconds));
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 1000);
            }
        }
    };

    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recording);
        recordButton = (Button) findViewById(R.id.recordButton);
        timerText = (TextView) findViewById(R.id.recordTimerText);
        updateRecordButtonText();
    }


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
            updateRecordButtonText();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void updateRecordButtonText() {
        if (mService != null) {
            if (mService.isRecording()) {
                recordButton.setText(R.string.stop_recording);
                recordButton.setBackground(getResources().getDrawable(R.drawable.red_button_pressed));
            } else {
                recordButton.setText(R.string.start_recording);
                recordButton.setBackground(getResources().getDrawable(R.drawable.red_button));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Intent that is used RecordService
        Intent intent = new Intent(this, RecordService.class);

        // Make sure record service is up and running before we try to connect to it
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

    /*
     * Most of the code below this point was copied and slightly modified from android documentation.
     * http://developer.android.com/guide/topics/media/audio-capture.html
     */

    // Called when record button is pressed
    public void onRecord(View view) {
        if (mService == null) {
            return;
        }
        if (!mService.isRecording()) {
            try {
                mService.startRecording();
                startTime = SystemClock.elapsedRealtime();
                isTimerRunning = true;
                mHandler.obtainMessage().sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mService.stopRecording();
                isTimerRunning = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateRecordButtonText();
    }

}
