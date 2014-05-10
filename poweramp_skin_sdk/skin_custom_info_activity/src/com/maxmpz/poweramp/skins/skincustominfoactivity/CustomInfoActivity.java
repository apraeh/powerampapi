package com.maxmpz.poweramp.skins.skincustominfoactivity; // TODO: change to your package name

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maxmpz.poweramp.skinlib.InfoActivity;

public class CustomInfoActivity extends InfoActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mLayoutId = R.layout.custom_activity_info; // Override info activity layout
		super.onCreate(savedInstanceState);
	}
	
	public void onCustomButton1(View v) { 
		Toast.makeText(this, "Custom Button 1 clicked!", Toast.LENGTH_SHORT).show();
	}
	
	public void onCustomButton2(View v) {
		Toast.makeText(this, "Custom Button 2 clicked!", Toast.LENGTH_SHORT).show();
	}
}
