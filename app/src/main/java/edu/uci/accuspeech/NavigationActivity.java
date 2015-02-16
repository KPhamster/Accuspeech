package edu.uci.accuspeech;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * This activity has all the button click event handlers for navigation
 */
public class NavigationActivity extends FragmentActivity {

    public void newRecording(View view) {
        startActivity(new Intent(this, RecordActivity.class));
    }

    public void pastRecordings(View view) {
        startActivity(new Intent(this, PastRecordingsActivity.class));
    }

    public void audioSettings(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
