
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.GameAlertPOJO;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadGameAlert extends AsyncTask<Void, Void, GameAlertPOJO> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameId;
    private String playerId;
    private Activity activity;
    private boolean toShowDialog;

    public LoadGameAlert(Activity activity, String gameId, boolean toShowDialog,
            LoadingListener listener) {
        this.activity = activity;
        this.gameId = gameId;
        this.toShowDialog = true;
        playerId = ((T4FApplication)activity.getApplication()).getPlayerID();
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (toShowDialog)
        {
            String message = activity.getResources().getString(
                    R.string.loading_scoreborad);
            ShowDialog.showLoadingDialog(activity, "Loading");
        }
    }

    @Override
    protected void onPostExecute(GameAlertPOJO gameStats) {
        if (toShowDialog)
            ShowDialog.removeLoadingDialog();
        
        if (mListener != null) {
            mListener.onLoadingComplete(gameStats);
        }
        super.onPostExecute(gameStats);
    }

    @Override
    protected GameAlertPOJO doInBackground(Void... params) {
        GameAlertPOJO gameStats = null;

        if (gameId == null)
            return null;

        try {
            String result = getGameStatistics(gameId);
            Gson gson = new Gson();
            gameStats = gson.fromJson(result, GameAlertPOJO.class);
            Log.e(TAG, "GameAlertPOJO: " + gameStats.toString());

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting GameStats: " + ex.getMessage());

        }

        return gameStats;
    }

    private String getGameStatistics(String gameId) {
        String result = null;
//        String url = "http://www.trivia4friends.com/t4fapi/gamev2dev.phpl?f=getGameAlert&gamid=1531&plaid=187";
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getGameAlert&gamid=%s&plaid=%s",
                        gameId,playerId);
         Log.e(TAG,"url::  "+url);
        try {
            result = (String) HttpManager.getResponse(url, false);
            Log.e(TAG,"result::  "+result);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
