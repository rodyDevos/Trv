
package com.igames2go.t4f.tasks;

import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadNewGameAlert extends AsyncTask<Void, Void, JSONObject> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Activity activity;
    private T4FApplication mApplication;
    
    public LoadNewGameAlert(Activity activity, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
        mApplication = (T4FApplication)activity.getApplication();	// 05/08/2014
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Loading...");
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
    	JSONObject result = null;
        try {
            JSONObject obj = getNewGameAlert();
            JSONArray alerts = obj.getJSONArray("newgame_alert");
            if(alerts.length() > 0){
            	result = alerts.getJSONObject(0);
            }
            
        } catch (Exception ex) {
            Log.e(TAG, "Exception in entering Contest: " + ex.getMessage());
        }

        return result;
    }
    
    
    private JSONObject getNewGameAlert() {
    	
    	String plaappid = mApplication.getPlayerAppID();
    	String url = String
                .format(activity.getString(R.string.game_url)+"?f=getNewGameAlert&plaappid=%s", plaappid);
        Log.e(TAG,"url::  "+url);
        url = url.replaceAll(" ", "%20");
        
        String result;
        JSONObject json = null;
		try {
			result = (String) HttpManager.getResponse(url, false);
			
			Log.d("Result", result);
	        json = new JSONObject(result);
	        
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return json;
    }

}
