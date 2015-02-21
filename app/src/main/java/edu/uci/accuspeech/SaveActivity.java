package edu.uci.accuspeech;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uci.accuspeech.service.AudioService;
import edu.uci.accuspeech.service.RecordService;
import edu.uci.accuspeech.util.ConvertRawToWav;

public class SaveActivity extends Activity {
    private String fileName;
    final static String DATE_FORMAT = "MM-dd-yy HH:mm:ss";
    TextView defaultFileName;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_save_recording);
        TextView timer = (TextView) findViewById(R.id.recordTimerText);
        timer.setText(getIntent().getStringExtra("timer"));
        defaultFileName = (TextView) findViewById(R.id.defaultRecordingNameText);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        fileName = sdf.format(new Date());
        defaultFileName.setHint(fileName);
    }

    public void save(View view){
        if (!TextUtils.isEmpty(defaultFileName.getText())) {
            fileName = defaultFileName.getText().toString();
        }
        // TODO This should be done in a background thread, currently it is being run on main thread
        // TODO This could lead to UI lag
        ConvertRawToWav.properWAV(RecordService.RAW_FILE_PATH + RecordService.RAW_FILE_NAME,
                AudioService.DEFAULT_WAV_PATH + fileName + ".wav", AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM));
        startActivity(new Intent(this, PastRecordingsActivity.class));
        finish();
    }

    public void cancel(View view){
        finish();
        //TODO add in that this will delete recording
    }
}
