package edu.uci.accuspeech;

import android.os.Bundle;

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GainControlFragment())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NoiseSuppressionFragment())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BassBoostControlFragment())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EqualizerControlFragment())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DecibelMeterFragment())
                    .commit();
        }
    }
}
