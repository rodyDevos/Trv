package com.igames2go.t4f.Activities;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.igames2go.t4f.R;

@SuppressLint("SetJavaScriptEnabled")
public class AboutActivity extends Activity {

	private WebView browser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setVersion();
		
		browser = (WebView) findViewById(R.id.webView1);
		browser.setInitialScale(1);
		browser.getSettings().setLoadWithOverviewMode(true);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.getSettings().setBuiltInZoomControls(true);
		browser.setWebViewClient(new HelloWebViewClient());
		browser.getSettings().setUseWideViewPort(true);

		browser.loadUrl(getString(R.string.url_about_page));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	private void setVersion() {
		try {
			String versionName = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			((TextView) findViewById(R.id.tv_version)).setText(versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
			view.loadData(
					"<html><body>sorry, could not connect to th webpage please check internet connectivity or <a href='"
							+ failingUrl
							+ "'>"
							+ failingUrl
							+ "</a></body></html>", "text/html", "utf-8");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("url", "" + url);
			view.loadUrl(url);
			return true;
		}
	}
	public void cancel(View v){
		finish();
	}
}
