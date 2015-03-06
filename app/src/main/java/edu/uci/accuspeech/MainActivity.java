package edu.uci.accuspeech;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import edu.uci.accuspeech.fragment.BassBoostControlFragment;
import edu.uci.accuspeech.fragment.DecibelMeterFragment;
import edu.uci.accuspeech.fragment.EqualizerControlFragment;
import edu.uci.accuspeech.fragment.GainControlFragment;
import edu.uci.accuspeech.fragment.NoiseSuppressionFragment;

/**
 * Generated class when creating the project from scratch.
 */
public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.add(R.id.container, new GainControlFragment());
            tx.add(R.id.container, new NoiseSuppressionFragment());
            tx.add(R.id.container, new BassBoostControlFragment());
            tx.add(R.id.container, new EqualizerControlFragment());
            tx.add(R.id.container, new DecibelMeterFragment());
            tx.commit();
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, VersionWarningActivity.class);
            startActivity(intent);
        }
    }
}
