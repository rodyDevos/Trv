
package com.igames2go.t4f.tasks;

import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.utils.ShowDialog;
import com.learn2crack.library.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LoadRegNameDefaults extends AsyncTask<Void, Void, Object> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String fbName;
    private Activity activity;
    
    public LoadRegNameDefaults(Activity activity, String fbName, LoadingListener listener) {
        this.activity = activity;
        this.fbName = fbName;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Loading");
    }

    @Override
    protected void onPostExecute(Object result) {
        
    	ShowDialog.removeLoadingDialog();
        
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected Object doInBackground(Void... params) {
        try {
            Object result = getRegNameDefaults(fbName);
            return result;
        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting GameStats: " + ex.getMessage());
            return "";
        }
    }

    private Object getRegNameDefaults(String fbName) {
    	 
    	try {
    		
    		String url = String.format(activity.getString(R.string.game_url)+"?f=getRegNameDefaults&fbname=%s", 
            		URLEncoder.encode(fbName, "utf-8"));
            Log.e(TAG,"url::  "+url);
            
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("Email URL", url);
            
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.getJSONFromUrl(url, params);
            JSONObject result = json.getJSONArray("getregnamedefaults").getJSONObject(0);
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
