package edu.uci.accuspeech.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.AutomaticGainControl;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import edu.uci.accuspeech.MainActivity;
import edu.uci.accuspeech.R;
import edu.uci.accuspeech.util.AudioEffectUtil;
import edu.uci.accuspeech.util.ConvertRawToWav;

/**
 * This class contains the logic for recording sound using the mic on device.
 *
 * The reason for this class to be a service is so it can run outside the
 * lifecycle of an activity or fragment.
 */
public class RecordService extends Service {

    // Notification id for when sound is being recorded
    private static final int ONGOING_NOTIFICATION_ID = 34637;

    // path to file where the raw sound data is stored
    private static final String RAW_FILE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/raw_file";

    private AudioRecord recorder;

    // Size of buffer where data is stored while recording
    private int iAudioBufferSize;

    // Background thread for saving data from audio buffer to file
    private RecordThread recordThread = null;

    // Output stream for raw file
    private OutputStream bos = null;

    private boolean isRecording = false;

    private final IBinder mBinder = new LocalBinder();

    private SharedPreferences sharedPreferences;

    public RecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * LocalBinder is a helper class for RecordControlFragment to bind this service.
     * Binding to this service allows the controls fragment to call methods directly
     * on this service.
     */
    public class LocalBinder extends Binder {
        public RecordService getService() {
            // Return this instance of LocalService so clients can call public methods
            return RecordService.this;
        }
    }

    public int onStartCommand (Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Method to begin recording. This will create new objects needed for recording and
     * start a background thread to write a raw file from the audio buffer.
     * @throws IOException
     *  Throws this exception if there are problems with file IO
     */
    public void startRecording() throws IOException {
        // If we are already recording this should return and do nothing
        if (isRecording) {
            return;
        }
        isRecording = true;
        final int iSampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM);
        iAudioBufferSize = AudioRecord.getMinBufferSize(iSampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        bos = new BufferedOutputStream(new FileOutputStream(RAW_FILE_NAME));
        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, iSampleRate, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, iAudioBufferSize);

        // Check if AGC is supported, if so retrieve from shared prefs
        if (AudioEffectUtil.isSupported(AudioEffect.EFFECT_TYPE_AGC)) {
            AutomaticGainControl gainControl = AutomaticGainControl.create(recorder.getAudioSessionId());
            if (sharedPreferences == null) {
                sharedPreferences = getSharedPreferences(AudioEffectUtil.SETTINGS_PREFS, Context.MODE_PRIVATE);
            }
            gainControl.setEnabled(sharedPreferences.getBoolean(AudioEffectUtil.AUTO_GAIN_CONTROL_KEY, false));
        }

        recordThread = new RecordThread(bos, recorder);
        recordThread.start();

        // Create a notification for this service while we are recording
        // Basically this is copy-paste from Android docs on foreground service
        Notification notification = new Notification(R.drawable.ic_launcher, getText(R.string.ticker_text),
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, getText(R.string.notification_title),
                getText(R.string.notification_message), pendingIntent);
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    /**
     * Stop recording closes, stops and releases objects that are no longer needed.
     * This method also converts the raw file into a wav file since we are done recording
     * and need a playable sound file.
     * @throws IOException
     */
    public void stopRecording() throws IOException {
        // If we are already not recording this should return and do nothing
        if (!isRecording) {
            return;
        }
        isRecording = false;
        stopForeground(true);
        recordThread.interrupt();
        recorder.stop();
        recorder.release();
        bos.close();
        final String waveFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audiorecordtest.wav";

        // TODO This should be done in a background thread, currently it is being run on main thread
        // TODO This could lead to UI lag
        ConvertRawToWav.properWAV(RAW_FILE_NAME, waveFileName, AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM));
        bos = null;
        recorder = null;
        recordThread = null;
    }

    /**
     * This is the class that will read from the audio buffer and output that data
     * into an output stream which should be the raw file.
     */
    class RecordThread extends Thread {
        private final AudioRecord recorder;
        private final OutputStream out;
        public int iBytesRead;

        /**
         * @param out
         *  This should be where the raw data is being written to.
         * @param recorder
         *  This is where we get the raw audio data.
         */
        public RecordThread(OutputStream out, AudioRecord recorder) {
            this.recorder = recorder;
            this.out = out;
        }

        @Override
        public void run()
        {
            byte[] buffer = new byte[iAudioBufferSize];
            int iBufferReadResult;
            iBytesRead = 0;
            recorder.startRecording();
            while(!interrupted())
            {
                while((iBufferReadResult = recorder.read(buffer, 0, iAudioBufferSize)) > 0) {
                    try {
                        bos.write(buffer, 0, iBufferReadResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
