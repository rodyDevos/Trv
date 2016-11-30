
package com.igames2go.t4f.tasks;

import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class EnterContest extends AsyncTask<Void, Void, String> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private T4FApplication mApplication;	// 04/21/2014
    private String plaId;	// 04/21/2014
    private String contestId;
    private Activity activity;
    
    public EnterContest(Activity activity, String contestId, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
        this.contestId = contestId;
        
        mApplication = (T4FApplication) activity.getApplication();	// 04/21/2014
        plaId = mApplication.getPlayerID();
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Entering...");
    }

    @Override
    protected void onPostExecute(String result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected String doInBackground(Void... params) {
    	String result = "";
        try {
            JSONObject obj = enterContest(plaId, contestId);
            result = obj.getString("enterContest");
            
        } catch (Exception ex) {
            Log.e(TAG, "Exception in entering Contest: " + ex.getMessage());
        }

        return result;
    }
    
    
    private JSONObject enterContest(String plaId, String contestId) {
    	
    	String url = String
                .format(activity.getString(R.string.game_url)+"?f=enterContest&plaid=%s&contestid=%s", plaId, contestId);
        Log.e(TAG,"plaId::  "+plaId);
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
