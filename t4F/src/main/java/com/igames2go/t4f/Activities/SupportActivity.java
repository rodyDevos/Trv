package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.view.CustomTextView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SupportActivity extends Activity {

	private WebView browser;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_support);
		
		CustomTextView titleTextView = (CustomTextView)findViewById(R.id.title);
		titleTextView.setTextColor(getResources().getColor(R.color.nav_bar_title_color));
		
		browser = (WebView) findViewById(R.id.webView1);
		browser.setInitialScale(1);
		browser.getSettings().setLoadWithOverviewMode(true);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.getSettings().setBuiltInZoomControls(true);
		browser.setWebViewClient(new HelloWebViewClient());
		browser.getSettings().setUseWideViewPort(true);

		browser.loadUrl(getString(R.string.url_support_page));
		//browser.zoomOut();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.support, menu);
		return true;
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
