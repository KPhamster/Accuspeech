package edu.uci.accuspeech;


import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
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
        findViewById(R.id.container).setVisibility(View.GONE);
        findViewById(R.id.saving).setVisibility(View.VISIBLE);
        new AsyncSave(RecordService.RAW_FILE_PATH + RecordService.RAW_FILE_NAME,
                AudioService.DEFAULT_WAV_PATH + fileName + ".wav", AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM)).execute();
    }

    public void cancel(View view){
        Intent intent = new Intent(this, DeleteVerificationActivity.class);
        startActivity(intent);
        finish();
    }

    private class AsyncSave extends AsyncTask<Void, Void, Void> {

        private final String rawFile;
        private final String newWavFile;
        private final int nativeOutputSampleRate;

        public AsyncSave(String rawFile, String newWavFile, int nativeOutputSampleRate) {
            this.rawFile = rawFile;
            this.newWavFile = newWavFile;
            this.nativeOutputSampleRate = nativeOutputSampleRate;

        }

        @Override
        protected Void doInBackground(Void... params) {
            ConvertRawToWav.properWAV(rawFile, newWavFile, nativeOutputSampleRate);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            startActivity(new Intent(SaveActivity.this, PastRecordingsActivity.class));
            finish();
        }

    }
}
