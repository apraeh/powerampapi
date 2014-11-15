package com.maxmpz.poweramp.skinlib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RedirActivity extends ResourceWrappingActivity {
	private static final String TAG = "RedirActivity";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
       	startPoweramp();
       	finish();
    }

	private void startPoweramp() {
		Intent intent = new Intent(Intent.ACTION_MAIN).setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
			finish();
		} catch(Exception ex) {
			Toast.makeText(this, "Poweramp app can't be found", Toast.LENGTH_LONG).show();
			Log.e(TAG, "", ex);
		}
	}
}