package edu.uci.accuspeech.service;

import android.app.Service;
import android.os.Environment;

abstract public class AudioService extends Service {

    // Notification id for when sound is being recorded
    public static final int ONGOING_NOTIFICATION_ID = 34637;

    public static final String RAW_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/accuspeach/raw/";
    public static final String PAST_RECORDINGS_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/accuspeach/";

    // path to file where the raw sound data is stored
    public static final String RAW_FILE_NAME = "raw_file.data";

    // When PlayService is at the end of a audio file this action gets sent out
    public static final String ACTION_PLAY_STOPPED = "ACTION_PLAY_STOPPED";

    public static final String DEFAULT_WAV_FILENAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/accuspeach/audiorecordtest.wav";
}
