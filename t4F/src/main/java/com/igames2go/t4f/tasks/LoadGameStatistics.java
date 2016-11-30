
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.GameStatistics;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadGameStatistics extends AsyncTask<Void, Void, GameStatistics> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameId;
    private Activity activity;
    private boolean toShowDialog;

    public LoadGameStatistics(Activity activity, String gameId, boolean toShowDialog,
            LoadingListener listener) {
        this.activity = activity;
        this.gameId = gameId;
        this.toShowDialog = toShowDialog;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (toShowDialog)
        {
            String message = activity.getResources().getString(
                    R.string.loading_stats);
            ShowDialog.showLoadingDialog(activity, message);
        }
    }

    @Override
    protected void onPostExecute(GameStatistics gameStats) {
        if (toShowDialog)
            ShowDialog.removeLoadingDialog();
        
        if (mListener != null) {
            mListener.onLoadingComplete(gameStats);
        }
        super.onPostExecute(gameStats);
    }

    @Override
    protected GameStatistics doInBackground(Void... params) {
        GameStatistics gameStats = null;

        if (gameId == null)
            return null;

        try {
            String result = getGameStatistics(gameId);
            Gson gson = new Gson();
            gameStats = gson.fromJson(result, GameStatistics.class);
            Log.e(TAG, "GameStats: " + gameStats.toString());

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting GameStats: " + ex.getMessage());

        }

        return gameStats;
    }

    private String getGameStatistics(String gameId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getGameStats&gamid=%s",
                        gameId);
        Log.e(TAG,"url::  "+url);
        try {
            result = (String) HttpManager.getResponse(url, false);
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
