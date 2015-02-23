package edu.uci.accuspeech;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uci.accuspeech.service.AudioService;
import edu.uci.accuspeech.service.RecordService;
import edu.uci.accuspeech.util.ConvertRawToWav;

public class RenameActivity extends Activity{
    private String fileName;
    TextView defaultFileName;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_rename_recording);
        fileName = getIntent().getStringExtra("name");
        defaultFileName = (TextView) findViewById(R.id.defaultRecordingNameText);
        defaultFileName.setText(fileName);
    }

    public void save(View view){
        String newFileName = defaultFileName.getText().toString();
        if (TextUtils.isEmpty(newFileName)){
            Toast.makeText(this, R.string.cannot_rename_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (newFileName.endsWith(".wav")) {
            newFileName = newFileName.substring(0,newFileName.length() - 4);
        }
        if (!fileName.equals(newFileName)){
            File fromFile = new File(AudioService.DEFAULT_WAV_PATH, fileName + ".wav");
            File toFile = new File(AudioService.DEFAULT_WAV_PATH, newFileName + ".wav");
            if (fromFile.exists()) {
                fromFile.renameTo(toFile);
            }
        }
        finish();
    }

    public void delete(View view){
        File audioFile = new File(AudioService.DEFAULT_WAV_PATH, fileName + ".wav");
        audioFile.delete();
        finish();
    }
}