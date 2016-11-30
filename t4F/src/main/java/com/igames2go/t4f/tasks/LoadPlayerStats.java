
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.PlayerStats;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadPlayerStats extends AsyncTask<Void, Void, PlayerStats> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Activity activity;
    private T4FApplication mApplication;
    private String plaId = null;

    public LoadPlayerStats(Activity activity, String plaId,
            LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
        this.plaId = plaId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.loading_stats);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(PlayerStats result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected PlayerStats doInBackground(Void... params) {
        PlayerStats playerstats = null;
        String appId = activity.getResources().getString(R.string.appid);

        if (plaId != null) {
            try {
                String result = getPlayerStats(appId, plaId);
                Gson gson = new Gson();
                playerstats = gson.fromJson(result, PlayerStats.class);
                Log.e(TAG, "Player Stats: " + playerstats.toString());

            } catch (Exception ex) {
                Log.e(TAG,
                        "Exception in getting Player stats: " + ex.getMessage());

            }
        } else {
            // TODO : to load login page as plaid is null;
            Log.e(TAG, "PlayerId is null ... Please login again: ");
        }

        return playerstats;
    }

    private String getPlayerStats(String appId, String plaId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getPlayerStats&appid=%s&plaid=%s",
                        appId, plaId);

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
