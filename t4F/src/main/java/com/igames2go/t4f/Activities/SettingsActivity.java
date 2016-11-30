package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class SettingsActivity extends AdActivity {

	T4FApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_settings);
		
		mApplication = (T4FApplication)getApplication();
		setSoundSetting();
	}

	public void openNotification(View v) {
		startActivity(new Intent(getApplicationContext(),
				NotificationActivity.class));
	}
	
	private void setSoundSetting() {
		final CheckBox cb = (CheckBox) findViewById(R.id.cb_sound);
		cb.setChecked(getSoundSetting());
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setSoundSetting(cb.isChecked());

			}
		});
	}

	private void setSoundSetting(boolean b) {
		SharedPreferences.Editor editor = getSharedPreferences("setting",
				MODE_PRIVATE).edit();
		editor.putBoolean("sound", b);
		editor.commit();
		T4FApplication.soundSettting = b;
	}

	private boolean getSoundSetting() {
		SharedPreferences prefs = getSharedPreferences("setting", MODE_PRIVATE);
		return prefs.getBoolean("sound", true);

	}
	public void cancel(View v){
		finish();
	}
}
