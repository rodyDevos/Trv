
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SurpriseCategory;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadSurpriseCategory extends
        AsyncTask<Void, Void, SurpriseCategory> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameId;
    Context activity;
    public LoadSurpriseCategory(String gameId, LoadingListener listener, Context context) {
        this.gameId = gameId;
        mListener = listener;
        activity = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SurpriseCategory surpriseCat) {
        if (mListener != null) {
            mListener.onLoadingComplete(surpriseCat);
        }
        super.onPostExecute(surpriseCat);
    }

    @Override
    protected SurpriseCategory doInBackground(Void... params) {
        SurpriseCategory surpriseCat = null;

        if (gameId == null)
            return null;

        try {
            String result = getSurpriseCategory(gameId);
            Gson gson = new Gson();
            surpriseCat = gson.fromJson(result, SurpriseCategory.class);
            Log.e(TAG, "surpriseCat: " + surpriseCat.toString());

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in getting SurpriseCategory: " + ex.getMessage());

        }

        return surpriseCat;
    }

    private String getSurpriseCategory(String gameId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getSurpriseCat&gamid=%s",
                        gameId);

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
