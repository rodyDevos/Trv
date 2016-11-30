package com.igames2go.t4f.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.flurry.android.FlurryAdListener;
import com.flurry.android.FlurryAdSize;
import com.flurry.android.FlurryAdType;
import com.flurry.android.FlurryAds;
import com.flurry.android.FlurryAgent;
import com.igames2go.t4f.R;

public class AddActivity extends Activity implements FlurryAdListener{

	LinearLayout ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ll = new LinearLayout(getApplicationContext());
		ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		ll.setBackgroundResource(R.drawable.trans);
		//setContentView(R.layout.ad_ll);
		setContentView(ll);
//		FlurryAgent.onStartSession(this, "V4BVC6SCNNYBQ32W8R47");
      FlurryAgent.onStartSession(this, "XPBHWTTV8CM5K3GZP45K");
      FlurryAds.setAdListener(this);
      new Thread(new Runnable() {
			
			@Override
			public void run() {
				showAd();
			}
		}).start();
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		finish();
	}
	private void showAd() {
		FlurryAds.fetchAd(this, "Ad Space 2", ll, FlurryAdSize.FULLSCREEN);
//		FlurryAds.fetchAd(this, findViewById(R.id.ll1).getTag()+"",
//	              (ViewGroup) findViewById(R.id.ll1),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, findViewById(R.id.ll2).getTag()+"",
//	              (ViewGroup) findViewById(R.id.ll2),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, findViewById(R.id.ll3).getTag()+"",
//	              (ViewGroup) findViewById(R.id.ll3),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, findViewById(R.id.ll4).getTag()+"",
//	              (ViewGroup) findViewById(R.id.ll4),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, findViewById(R.id.ll5).getTag()+"",
//	              (ViewGroup) findViewById(R.id.ll5),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, findViewById(R.id.ll6).getTag()+"",
//	              (ViewGroup) findViewById(R.id.ll6),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll7+"",
//	              (ViewGroup) findViewById(R.id.ll7),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll8+"",
//	              (ViewGroup) findViewById(R.id.ll8),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll9+"",
//	              (ViewGroup) findViewById(R.id.ll9),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll10+"",
//	              (ViewGroup) findViewById(R.id.ll10),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll11+"",
//	              (ViewGroup) findViewById(R.id.ll11),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll12+"",
//	              (ViewGroup) findViewById(R.id.ll12),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll13+"",
//	              (ViewGroup) findViewById(R.id.ll13),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll14+"",
//	              (ViewGroup) findViewById(R.id.ll14),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll15+"",
//	              (ViewGroup) findViewById(R.id.ll15),
//	              FlurryAdSize.BANNER_TOP);
//	      FlurryAds.fetchAd(this, R.id.ll16+"",
//	              (ViewGroup) findViewById(R.id.ll16),
//	              FlurryAdSize.BANNER_TOP);
//	      new CountDownTimer(60*1000, 10*1000) {
//			
//			@Override
//			public void onTick(long millisUntilFinished) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onFinish() {
//				showAd();
//				
//			}
//		}.start();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		FlurryAgent.onEndSession(this);
	}

	@Override
	public void onAdClicked(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdClosed(String arg0) {
		finish();
	}

	@Override
	public void onAdOpened(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onApplicationExit(String arg0) {
		finish();
		
	}

	@Override
	public void onRenderFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onVideoCompleted(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldDisplayAd(String arg0, FlurryAdType arg1) {
		
		return true;
	}

	@Override
	public void spaceDidFailToReceiveAd(String arg0) {
		Log.e("Flurry", arg0+"");
		
	}

	@Override
	public void spaceDidReceiveAd(String arg0) {
		
		if(FlurryAds.isAdReady(arg0))
		{
			FlurryAds.displayAd(this, arg0,ll);
		}
		
	}

	@Override
	public void onRendered(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
