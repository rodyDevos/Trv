
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.LeaderboardPlayersDataObject;
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
import java.util.ArrayList;
import java.util.List;

public class LoadLeaderboardPlayers extends AsyncTask<Void, Void, List<LeaderboardPlayersDataObject>> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private T4FApplication mApplication;	// 04/21/2014
    private String plaappId;	// 04/21/2014
    private String contestId;
    private String group;
    private String friends;
    
    private Activity activity;
    
    
    public LoadLeaderboardPlayers(Activity activity, String contestId, String group, String friends, LoadingListener listener) {
        this.activity = activity;
        this.contestId = contestId;
        this.group = group;
        this.friends = friends;
        mListener = listener;
        mApplication = (T4FApplication) activity.getApplication();
        this.plaappId = mApplication.getPlayerAppID();
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Loading...");
    }

    @Override
    protected void onPostExecute(List<LeaderboardPlayersDataObject> obj) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(obj);
        }
        super.onPostExecute(obj);
    }

    @Override
    protected List<LeaderboardPlayersDataObject> doInBackground(Void... params) {
    	ArrayList<LeaderboardPlayersDataObject> playerList = new ArrayList<LeaderboardPlayersDataObject>();

        try {
        	JSONObject result = getLeaderboard(plaappId, contestId, group, friends);
            
            JSONArray players = result.getJSONArray("leaderboard");
            if(players.length() > 0){
            	for(int i = 0; i < players.length(); i++){
            		JSONObject player = players.getJSONObject(i);
            		LeaderboardPlayersDataObject playerObject = new LeaderboardPlayersDataObject();
            		playerObject.parseJSONObject(player);
            		playerList.add(playerObject);
            	}
            }
            
        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting Contests: " + ex.getMessage());

        }

        return playerList;
    }
    

    private JSONObject getLeaderboard(String plaappId, String contestId, String group, String friends) {
    	
    	String url = String
                .format(activity.getString(R.string.game_url)+"?f=getLeaderboard&plaappid=%s&contestid=%s&group=%s",
                        plaappId, contestId, group);
    	if(friends.length() > 0){
    		url = url + "&friends=" + friends;
    	}
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
