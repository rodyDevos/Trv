package com.igames2go.t4f.Activities.SelectCategory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.igames2go.t4f.R;

import org.w3c.dom.Text;

public class SelectCategoryDetailsActivity extends Activity {

    private WebView browser;

    public Menu menuG;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(getLayoutInflater().inflate(R.layout.activity_select_category_details, null));

        String catTitle = getIntent().getStringExtra("title");
        String disclosureUrl = getIntent().getStringExtra("url");

        TextView txtTitle = (TextView) findViewById(R.id.title);
        txtTitle.setText(catTitle);

        browser = (WebView) findViewById(R.id.webkit);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        browser.setScrollbarFadingEnabled(false);

        browser.loadUrl(disclosureUrl);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.option, menu);
        boolean b = super.onCreateOptionsMenu(menu);
        return (b);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        // TODO Auto-generated method stub

        // Toast.makeText(this , "menuu opened", Toast.LENGTH_SHORT).show();
        MenuItem back = menu.findItem(R.id.back);
        if (browser.canGoBack()) {

            // back.setIcon(R.drawable.restore_f2);
        } else {
            back.setEnabled(false);
            // back.setIcon(R.drawable.restore);
        }

        MenuItem forward = menu.findItem(R.id.forward);
        if (browser.canGoForward()) {

            // forward.setIcon(R.drawable.forward_f2);
        } else {

            // forward.setIcon(R.drawable.forward);
        }
        forward.setEnabled(browser.canGoForward());
        back.setEnabled(browser.canGoBack());

        return super.onMenuOpened(featureId, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back) {
            browser.goBack();
            return (true);
        } else if (item.getItemId() == R.id.forward) {
            browser.goForward();
            return (true);
        } else if (item.getItemId() == R.id.stop) {
            browser.stopLoading();
            return (true);
        } else if (item.getItemId() == R.id.refresh) {
            browser.reload();
            return (true);
        }

        return (super.onOptionsItemSelected(item));
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
            // view.loadUrl("<html><body>sorry, could not connect to Restock.com please check internet connectivity</body></html>");
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

            // dialog.dismiss();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("url", "" + url);
            view.loadUrl(url);
            // menuG.findItem(R.id.stop).setIcon(R.drawable.stop_f2);

            return true;
        }
    }

    public void cancel(View v){
        finish();
    }
}
