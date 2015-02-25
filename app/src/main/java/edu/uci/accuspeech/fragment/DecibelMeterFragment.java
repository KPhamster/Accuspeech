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

//    public class SoundMeter {
//        static final private double EMA_FILTER = 0.6;
//        private MediaRecorder mRecorder = null;
//        private double mEMA = 0.0;
//        public void start() throws IOException{
//            if (mRecorder == null){
//                mRecorder = new MediaRecorder();
//                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                mRecorder.prepare();
//                mRecorder.start();
//                mEMA = 0.0;
//            }
//        }
//        public void stop(){
//            if (mRecorder != null){
//                mRecorder.stop();
//                mRecorder.release();
//                mRecorder = null;
//            }
//        }
//        public double getAmplitude(){
//            if (mRecorder != null)
//            {return (mRecorder.getMaxAmplitude()/2700.0);}
//            else
//            {return 0;}
//        }
//        public double getAmplitudeEMA(){
//            double amp = getAmplitude();
//            mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
//            return mEMA;
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.decibel_meter, container, false);
//        SoundMeter meter = new SoundMeter();
//        while (true){
//            double db = meter.getAmplitude();
//            String sound = Double.toString(db);
//            Log.d("Decibel Level:",sound);
//        }
//
//    }
}
