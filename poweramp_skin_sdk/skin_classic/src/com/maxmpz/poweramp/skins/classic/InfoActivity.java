package com.maxmpz.poweramp.skins.classic; // TODO: change to your package name

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class InfoActivity extends Activity {
	private static final String TAG = "InfoActivity";
	
	public static final String EXTRA_OPEN = "open";
	public static final String THEME = "theme";
	
	public static final String ACTION_SET_THEME = "com.maxmpz.audioplayer.action.SET_THEME";
	public static final String EXTRA_THEME_ID = "theme_id";
	public static final String EXTRA_THEME_PATH = "theme_path";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        // NOTE: framework has a bug/oversight - it can't do findViewById for negative ids, this means it's not possible to do the findViewById here,
        // as skin has negative ids for its resources. This is why search by tag is used.
        if(Build.VERSION.SDK_INT >= 11) { // Hide title for 11+
	        View icon = getWindow().getDecorView().findViewWithTag("icon");
	        if(icon != null) {
	        	icon.setVisibility(View.GONE);
	        }
	        View title = getWindow().getDecorView().findViewWithTag("title");
	        if(title != null) {
	        	title.setVisibility(View.GONE);
	        }
        }
    }

	public void onButton1Click(View v) { // Optional
		Intent intent = new Intent(Intent.ACTION_MAIN).setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.SettingsActivity");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_OPEN, THEME);
		try {
			startActivity(intent);
			finish();
		} catch(Exception ex) {
			Toast.makeText(this, "Poweramp app can't be found", Toast.LENGTH_LONG).show();
			Log.e(TAG, "", ex);
		}
	}
	
	private void setTheme(String name, int skin) {
		// Set theme
		Intent intent = new Intent(ACTION_SET_THEME).setPackage("com.maxmpz.audioplayer");
		intent.putExtra(EXTRA_THEME_ID, skin);
		intent.putExtra(EXTRA_THEME_PATH, getApplicationInfo().publicSourceDir);
		try {
			sendBroadcast(intent);
			Toast.makeText(this, "Theme set to " + name, Toast.LENGTH_SHORT).show();
		} catch(Exception ex) {
			Toast.makeText(this, "Poweramp app can't be found", Toast.LENGTH_LONG).show();
			Log.e(TAG, "", ex);
			return;
		}

		// Start Poweramp
		intent = new Intent(Intent.ACTION_MAIN).setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	public void onButton2Click(View v) { // Optional
		setTheme("Classic Skin", R.style.ClassicSkin); // TODO: change to your skin name
	}
}