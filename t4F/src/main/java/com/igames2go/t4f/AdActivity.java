package com.igames2go.t4f;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.flurry.android.FlurryAgent;
import com.flurry.android.ads.FlurryAdBanner;
import com.flurry.android.ads.FlurryAdBannerListener;
import com.flurry.android.ads.FlurryAdErrorType;

public class AdActivity extends Activity {
	
//	public abstract String getAPIKey();
	boolean adEnabled = true;
//	private LinearLayout bannerAdLayout;

	public static final String TAG = AdActivity.class.getSimpleName();

	private FlurryAdBanner mFlurryAdBanner = null;
	private String mAdSpaceName = "Test Space";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(getString(R.string.ad_enabled).equals("1"))
		{
			FlurryAgent.setLogEnabled(false);
			//FlurryAgent.setLocation(18.9750f, 72.8258f);;
			FlurryAgent.init(this, getString(R.string.flurry_appid));
		}
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.ad_layout);
		View v=  getLayoutInflater().inflate(layoutResID, null);
		FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		v.setLayoutParams(param);
		((LinearLayout)findViewById(R.id.layput_container)).addView(v);
	}
	
	@Override
	public void setContentView(View v) {
		super.setContentView(R.layout.ad_layout);
		//View v=  getLayoutInflater().inflate(layoutResID, null);
		FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		v.setLayoutParams(param);
		((LinearLayout)findViewById(R.id.layput_container)).addView(v);
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(getString(R.string.ad_enabled).equals("1"))
		{
			FlurryAgent.onEndSession(this);
		}

	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(getString(R.string.ad_enabled).equals("1"))
		{
			Log.d(TAG, "Fetching Ad");
			FlurryAgent.onStartSession(this);
			showAd();
		}
	}

	protected void showAd() {
		LinearLayout bannerAdLayout = (LinearLayout) findViewById(R.id.banner_layout);
		bannerAdLayout.removeAllViews();
		mFlurryAdBanner = new FlurryAdBanner(this, bannerAdLayout, mAdSpaceName);
		mFlurryAdBanner.setListener(bannerAdListener);
		mFlurryAdBanner.fetchAd();
	}


	FlurryAdBannerListener bannerAdListener = new FlurryAdBannerListener() {
		@Override
		public void onFetched(FlurryAdBanner adBanner) {
			Log.i(TAG,"onFetched");
			adBanner.displayAd();
		}
		@Override
		public void onError(FlurryAdBanner adBanner, FlurryAdErrorType adErrorType, int errorCode) {
			Log.i(TAG,"error fetching ad code : " + errorCode );
			adBanner.destroy();
		}
		@Override
		public void onRendered(FlurryAdBanner adBanner) {}
		@Override
		public void onShowFullscreen(FlurryAdBanner adBanner) {}
		@Override
		public void onCloseFullscreen(FlurryAdBanner adBanner) {}
		@Override
		public void onAppExit(FlurryAdBanner adBanner) {}
		@Override
		public void onClicked(FlurryAdBanner adBanner) {}
		@Override
		public void onVideoCompleted(FlurryAdBanner adBanner) {}
	};
}
