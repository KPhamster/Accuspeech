package edu.uci.accuspeech;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class VersionWarningActivity extends Activity {

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_wrong_version);
    }

    public void ok(View view){
        finish();
    }
}
