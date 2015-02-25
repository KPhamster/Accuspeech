package edu.uci.accuspeech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class DeleteVerificationActivity extends Activity {
    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_delete_verify);
    }

    public void delete(View view){
        finish();
    }

    public void cancel(View view){
        Intent intent = new Intent(this, SaveActivity.class);
        startActivity(intent);
        finish();
    }
}
