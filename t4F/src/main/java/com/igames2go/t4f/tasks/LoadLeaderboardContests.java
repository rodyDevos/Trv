
package com.igames2go.t4f.tasks;

import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.LeaderboardContestsDataObject;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadLeaderboardContests extends AsyncTask<Void, Void, List<LeaderboardContestsDataObject>> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private T4FApplication mApplication;	// 04/21/2014
    private String plaappId;	// 04/21/2014
    private Activity activity;
    
    public LoadLeaderboardContests(Activity activity, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Loading...");
    }

    @Override
    protected void onPostExecute(List<LeaderboardContestsDataObject> contestList) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(contestList);
        }
        super.onPostExecute(contestList);
    }

    @Override
    protected List<LeaderboardContestsDataObject> doInBackground(Void... params) {
    	ArrayList<LeaderboardContestsDataObject> contestList = new ArrayList<LeaderboardContestsDataObject>();

        try {
        	mApplication = (T4FApplication) activity.getApplication();	// 04/21/2014
            plaappId = mApplication.getPlayerAppID();	// 04/21/2014
            JSONObject result = getContests(plaappId);
            
            JSONArray contests = result.getJSONArray("contests");
            if(contests.length() > 0){
            	for(int i = 0; i < contests.length(); i++){
            		JSONObject contest = contests.getJSONObject(i);
            		LeaderboardContestsDataObject contestObject = new LeaderboardContestsDataObject();
            		contestObject.parseJSONObject(contest);
            		contestList.add(contestObject);
            	}
            }
            
            mApplication.setContests(contestList);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting Contests: " + ex.getMessage());

        }

        return contestList;
    }
    
    
    private JSONObject getContests(String plaappId) {
    	
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getContests&plaappid=%s",
                        plaappId);
        Log.e(TAG,"plaappId::  "+plaappId);
        Log.e(TAG,"url::  "+url);
        url = url.replaceAll(" ", "%20");
        
        String result;
        JSONObject json = null;
		try {
			result = (String) HttpManager.getResponse(url, false);
			
			Log.d("Result", result);
	        json = new JSONObject(result);
	        
	      //JSONObject json = jsonParser.getJSONFromUrl(url, params);
	        
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
