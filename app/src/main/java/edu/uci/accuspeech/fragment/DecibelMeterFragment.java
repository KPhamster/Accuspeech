package edu.uci.accuspeech.fragment;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import edu.uci.accuspeech.R;

public class DecibelMeterFragment extends Fragment {

    private AsyncMeter asyncMeter = null;
    private TextView textView;
    private Button button;

    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.decibel_meter, container, false);
        textView = (TextView) rootView.findViewById(R.id.decibelText);
        button = (Button) rootView.findViewById(R.id.decibelButton);
        final CharSequence start = getResources().getString(R.string.start);
        final CharSequence stop = getResources().getString(R.string.stop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals(start)) {
                    button.setText(stop);
                    if (asyncMeter == null) {
                        asyncMeter = new AsyncMeter(textView);
                        asyncMeter.execute();
                    }
                } else {
                    turnOff();
                }
            }
        });
        return rootView;
    }

    private void turnOff() {
        if (asyncMeter != null) {
            asyncMeter.cancel(true);
            asyncMeter = null;
            textView.setText(R.string.decibel_meter_off);
            button.setText(R.string.start);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        turnOff();
    }

    private class AsyncMeter extends AsyncTask<Void, Integer, Boolean> {

        private MediaRecorder mRecorder = null;
        private TextView textView;

        AsyncMeter(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // START
            if (mRecorder == null){
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                try {
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (Exception e) {
                    System.out.println("Prepare Error");
                    mRecorder.release();
                    mRecorder = null;
                    return false;
                }
            }




            // UPDATES
            while (true) {
                if (isCancelled()) {
                    break;
                }
                int amp = mRecorder.getMaxAmplitude();
                int db = (int) (20*Math.log10(amp));
                if (db < 0) {
                    db = 0;
                }
                publishProgress(db);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }

            // END
            if (mRecorder != null){
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
            return true;
        }

        // Runs on the main thread and updates the UI with the sound levels
        protected void onProgressUpdate(Integer... progress) {
            if (!isCancelled()) {
                textView.setText("db: " + progress[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(!result) {
                textView.setText(R.string.audio_in_use);
                button.setText(R.string.start);
            }
        }
    }
}
