package edu.uci.accuspeech.fragment;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import edu.uci.accuspeech.R;

public class DecibelMeterFragment extends Fragment {

    public class SoundMeter {
        static final private double EMA_FILTER = 0.6;
        private MediaRecorder mRecorder = null;
        private double mEMA = 0.0;
        public void start(){
            System.out.println("Hello1");
            if (mRecorder == null){
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    System.out.println("Prepare Error");
                }
                mRecorder.start();
                mEMA = 0.0;
            }
        }
        public void stop(){
            if (mRecorder != null){
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        }
        public double getAmplitude(){
            if (mRecorder != null)
            {return (mRecorder.getMaxAmplitude());}
            else
            {return 0;}
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.decibel_meter, container, false);
        SoundMeter meter = new SoundMeter();
        meter.start();
        int refresh_count = 0;

//          while (refresh_count < 1200 ){
//            double db = meter.getAmplitude();
//            refresh_count += 1;
//            try {
//                Thread.sleep(33);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            String decibel = new Double(db).toString();
//            System.out.println(decibel);

//        }
        meter.stop();
        return rootView;
    }
}
