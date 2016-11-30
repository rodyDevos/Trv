package com.igames2go.t4f.Activities;

import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.flurry.android.ads.FlurryAdBannerListener;
import com.flurry.android.ads.FlurryAdErrorType;
import com.igames2go.t4f.R;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.ads.FlurryAdBanner;

public class AdActivity extends Activity implements FlurryAdListener {
	
//	public abstract String getAPIKey();
	boolean adEnabled = true;
	private LinearLayout bannerAdLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(getString(R.string.ad_enabled).equals("1"))
		{
			FlurryAgent.setLogEnabled(false);
			FlurryAgent.init(this, getString(R.string.flurry_appid));


			FlurryAds.setAdListener(this);
			FlurryAds.setLocation(18.9750f, 72.8258f);

			FlurryAgent.onStartSession(this);

			bannerAdLayout = (LinearLayout) findViewById(R.id.banner_layout);
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
			Log.d("Flurry", "Fetching Ad");

			showAd();
		}
	}

	protected void showAd() {
		FlurryAds.fetchAd(this, getString(R.string.flurry_ad_space1), (LinearLayout) findViewById(R.id.banner_layout),FlurryAdSize.BANNER_BOTTOM);
	}


    @Override
    public void onAdClicked(String arg0) {
//    	Toast.makeText(getApplicationContext(), "onAdClick-"+arg0, 1000).show();
    }

    @Override
    public void onAdClosed(String arg0) {

    }

    @Override
    public void onAdOpened(String arg0) {

    }

    @Override
    public void onApplicationExit(String arg0) {

    }

    @Override
    public void onRenderFailed(String arg0) {
		Log.d("Flurry", "Render Failed");
    }

    @Override
    public void onVideoCompleted(String arg0) {

    }

    @Override
    public boolean shouldDisplayAd(String arg0, FlurryAdType arg1) {
        return true;
    }

    @Override
    public void spaceDidFailToReceiveAd(String arg0) {
		Log.d("Flurry", "Ad receive failed");
    }

    @Override
    public void spaceDidReceiveAd(String arg0) {
        //FlurryAds.displayAd(this, getString(R.string.flurry_ad_space1), (LinearLayout) findViewById(R.id.ad));

		Log.d("Flurry", "Ad Fetched");

		FlurryAds.displayAd(this, getString(R.string.flurry_ad_space1), (LinearLayout) findViewById(R.id.banner_layout));

    }

	@Override
	public void onRendered(String arg0) {
		// TODO Auto-generated method stub

		Log.d("Flurry", "Ad Rendered");
		
	}
}
