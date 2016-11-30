
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SaveSettingsLoginsResult;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class SaveSettingsLogins extends
        AsyncTask<Void, Void, SaveSettingsLoginsResult> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
   
    private String plaapp_id;
    private String device_notify;
    private String device_id;
	private Context activity;

    public SaveSettingsLogins(String plaapp_id,
            String device_notify, String device_id, LoadingListener listener, Context context) {
        mListener = listener;
        this.plaapp_id = plaapp_id;
        this.device_notify = device_notify;
        this.device_id = device_id;
        activity = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SaveSettingsLoginsResult result) {
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected SaveSettingsLoginsResult doInBackground(Void... params) {
        SaveSettingsLoginsResult settingsResult = null;

        if (plaapp_id == null || plaapp_id.length() == 0 || device_notify == null
                || device_notify.length() == 0) {
            return null;
        }

        try {
            String result = setSettingsLogins(plaapp_id, device_notify, device_id);
            Gson gson = new Gson();
            settingsResult = gson.fromJson(result,
                    SaveSettingsLoginsResult.class);
            Log.e(TAG, "SaveSettingsLoginsResult: " + settingsResult.toString());

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in Saving SaveSettingsLogins: "
                            + ex.getMessage());

        }

        return settingsResult;
    }

    private String setSettingsLogins(String plaappId, String deviceNotify, String deviceId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setSettingsLogins&plaappid=%s&devicenotify=%s&deviceid=%s",
                        plaappId, deviceNotify, deviceId);

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
