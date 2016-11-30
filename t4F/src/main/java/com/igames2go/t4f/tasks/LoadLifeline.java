
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.Lifeline;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadLifeline extends AsyncTask<Void, Void, Lifeline> {

    private final String TAG = "T4F";

    private String gameplaid = null;
    private String increment = null;

    private LoadingListener mListener;
    Context activity;
    public LoadLifeline(String gameplaid, String increment,
            LoadingListener listener, Context activity) {
        this.gameplaid = gameplaid;
        this.increment = increment;
        mListener = listener;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Lifeline result) {
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected Lifeline doInBackground(Void... params) {

        return setLifeline();
    }

    private Lifeline setLifeline() {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setLifeline&gamplaid=%s&increment=%s",
                        gameplaid, increment);

        url = url.replace(" ", "%20");

        try {
            result = (String) HttpManager.getResponse(url, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Gson gson = new Gson();
            Lifeline lifeline = gson.fromJson(result, Lifeline.class);
            return lifeline;
        } catch (Exception ex) {
            Log.e(TAG, "Exception in Lifelines: " + ex.getMessage());

        }

        return null;
    }

}
