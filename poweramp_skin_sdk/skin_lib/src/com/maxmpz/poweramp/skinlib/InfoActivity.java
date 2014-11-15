package com.maxmpz.poweramp.skinlib; 

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class InfoActivity extends ResourceWrappingActivity {
	private static final String TAG = "InfoActivity";
	
	public static final String EXTRA_OPEN = "open";
	public static final String THEME = "theme";
	
	public static final String ACTION_SET_THEME = "com.maxmpz.audioplayer.action.SET_THEME";
	public static final String EXTRA_THEME_ID = "theme_id";
	public static final String EXTRA_THEME_PATH = "theme_path";
	
	private static final String PREFS_NAME = "prefs";
	private static final String PREF_THEMED_ICON = "themed_icon";
	private static final String PREF_HIDE_SKIN_ICON = "hide_skin_icon";
	
	protected SharedPreferences mPrefs;
	
	protected int mLayoutId = R.layout.activity_info;
	
	private static final class SkinId {
		int id;
		public SkinId(int id) {
			this.id = id;
		}
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        mPrefs = getSharedPreferences(PREFS_NAME, 0);
        
        setContentView(mLayoutId);
        // NOTE: framework has a bug/oversight - it can't do findViewById for negative ids, this means it's not possible to do the findViewById here,
        // as skin has negative ids for its resources. This is why search by tag is used.
        View root = getWindow().getDecorView();
		if(Build.VERSION.SDK_INT >= 11) { // Hide title for 11+
	        View icon = root.findViewWithTag("icon");
	        if(icon != null) {
	        	icon.setVisibility(View.GONE);
	        }
	        View title = root.findViewWithTag("title");
	        if(title != null) {
	        	title.setVisibility(View.GONE);
	        }
        }
       	((CheckBox)root.findViewWithTag("toggle_themed_icon")).setChecked(mPrefs.getBoolean(PREF_THEMED_ICON, false));
       	((CheckBox)root.findViewWithTag("hide_skin_icon")).setChecked(mPrefs.getBoolean(PREF_HIDE_SKIN_ICON, false));
       	
       	generateSkinSelectButtons(root);
    }
	
	private void generateSkinSelectButtons(View root) {
		ViewGroup group = (ViewGroup)root.findViewWithTag("button_layout");
		LayoutInflater inflater = getLayoutInflater();
		final Resources r  = getResources();
		
		int skinsArrayId = r.getIdentifier("poweramp_skins", "array", getPackageName());
		int namesArrayId = r.getIdentifier("poweramp_skin_names", "array", getPackageName());
		//Log.w(TAG, "skinsArrayId=0x" + Long.toHexString(skinsArrayId) + " namesArrayId=0x" + Long.toHexString(namesArrayId));
		//String[] names = r.getStringArray(R.array.poweramp_skin_names);
		String[] names = r.getStringArray(namesArrayId);
		//TypedArray array = r.obtainTypedArray(R.array.poweramp_skins);
		TypedArray array = r.obtainTypedArray(skinsArrayId);
		if(array != null && array.length() > 0) {
			for(int i = 0; i < array.length(); i++) {
				int themeId = array.getResourceId(i, 0);
				Button button = (Button)inflater.inflate(R.layout.skin_button, group, false);
				button.setText(getString(R.string.set_skin_s, names[i]));
				button.setTag(new SkinId(themeId));
				group.addView(button);
			}
		}
		array.recycle();
	}

	public void onClick(View v) {
		final int id = v.getId();
		if(id == R.id.toggle_themed_icon) {
			toggleThemedIcon();
		} else if(id == R.id.hide_skin_icon) {
			toggleSkinIcon();
		} else if(v.getTag() instanceof SkinId) {
			setPowerampTheme(((SkinId)v.getTag()).id);
		}
	}
	
	protected void toggleSkinIcon() {
		boolean hide = !mPrefs.getBoolean(PREF_HIDE_SKIN_ICON, false);
		mPrefs.edit().putBoolean(PREF_HIDE_SKIN_ICON, hide).commit();
		
		PackageManager pm = getPackageManager();
		// Toggle this activity icon
		pm.setComponentEnabledSetting(new ComponentName(this, getClass()), 
				!hide ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}

	protected void toggleThemedIcon() {
		boolean enable = !mPrefs.getBoolean(PREF_THEMED_ICON, false);
		mPrefs.edit().putBoolean(PREF_THEMED_ICON, enable).commit();

		PackageManager pm = getPackageManager();
		// Toggle redirect icon
		pm.setComponentEnabledSetting(new ComponentName(this, RedirActivity.class), // 
				enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}

	protected void setPowerampTheme(int skin) {
		try {
			Intent intent = new Intent(ACTION_SET_THEME).setPackage("com.maxmpz.audioplayer");
			intent.putExtra(EXTRA_THEME_ID, skin);
			intent.putExtra(EXTRA_THEME_PATH, getApplicationInfo().publicSourceDir);
			sendBroadcast(intent);

			// Start Poweramp
			intent = new Intent(Intent.ACTION_MAIN).setClassName("com.maxmpz.audioplayer", "com.maxmpz.audioplayer.StartupActivity");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		} catch(Exception ex) {
			Toast.makeText(this, "Poweramp not installed", Toast.LENGTH_LONG).show();
			Log.e(TAG, "", ex);
			return;
		}
	}
}