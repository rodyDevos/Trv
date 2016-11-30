
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.Messages;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadMessages extends AsyncTask<Void, Void, Messages> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameId;
    private String gameplaId;
    private Activity activity;

    public LoadMessages(Activity activity, String gameId, String gameplaId,
            LoadingListener listener) {
        this.activity = activity;
        this.gameId = gameId;
        this.gameplaId = gameplaId;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.loading_msgs);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(Messages result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected Messages doInBackground(Void... params) {
        Messages msgs = null;

        if (gameId == null || gameplaId == null)
            return null;

        try {
            String result = getMessages(gameId, gameplaId);
            Gson gson = new Gson();
            msgs = gson.fromJson(result, Messages.class);
            Log.e(TAG, "Messages: " + msgs.toString());

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting Messages: " + ex.getMessage());

        }

        return msgs;
    }

    private String getMessages(String gameId, String gameplaId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getMessages&gamid=%s&gamplaid=%s",
                        gameId, gameplaId);

        Log.e(TAG, "url: " + url);
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
