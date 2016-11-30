
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.TagPlayer;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class TagThePlayer extends AsyncTask<Void, Void, Void> {

    private final String TAG = "T4F";

    private Activity activity;

    private T4FApplication mApplication;
    String frndsfbId = null;
    String frndsfbname = null;
    String action = null;
    String frndsplaid = null;
    private LoadingListener mListener;

    public TagThePlayer(Activity activity, String frndsfbId,
            String frndsfbname, String frndsplaid, String action, LoadingListener listener) {
        this.activity = activity;
        this.frndsfbId = frndsfbId;
        this.frndsfbname = frndsfbname;
        this.frndsplaid = frndsplaid;
        this.action = action;
        mListener = listener;
    }

    public TagThePlayer(Activity activity, String frndsfbId,
            String frndsfbname, String frndsplaid, String action) {
        this.activity = activity;
        this.frndsfbId = frndsfbId;
        this.frndsplaid = frndsplaid;
        this.frndsfbname = frndsfbname;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        if (mListener != null) {
            mListener.onLoadingComplete();
        }
        super.onPostExecute(result);
    }

    @Override
    protected Void doInBackground(Void... params) {

        mApplication = (T4FApplication) activity.getApplication();

        if (tagPlayer()) {
            Log.e(TAG, "tagPlayer is successful");
        } else {
            Log.e(TAG, "tagPlayer is failed");
        }
        return null;
    }

    private boolean tagPlayer() {
        if (frndsfbId == null || frndsfbname == null || frndsplaid == null || action == null)
            return false;

        String appId = activity.getResources().getString(R.string.appid);
        String plaId = mApplication.getPlayerID();
        String result = null;

        if (plaId != null) {

            appId = ((appId == null) ? "" : appId);
            try {
                result = tagPlayer(appId, plaId, frndsfbId, frndsfbname, frndsplaid, action);
                Gson gson = new Gson();
                TagPlayer tagPla = gson.fromJson(result, TagPlayer.class);
                int tagPlayerResult = Integer.parseInt(tagPla
                        .gettagPlayerResult());
                if (tagPlayerResult > 0)
                    return true;
            } catch (Exception ex) {
                Log.e(TAG, "Exception in tagPlayer: " + ex.getMessage());

            }

        } else {
            // TODO: when plaid is null redirect to login page
        }
        return false;
    }

    private String tagPlayer(String appId, String plaId, String frndsfbId,
            String frndsfbname, String frndsplaid, String action) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=tagPlayer&appid=%s&plaid=%s&plafb_id=%s&plafb_name=%s&what=%s&friend_plaid=%s",
                        appId, plaId, frndsfbId, frndsfbname, action, frndsplaid);

        url = url.replace(" ", "%20");

        Log.e(TAG, "url in tagPlayer: " + url);

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
