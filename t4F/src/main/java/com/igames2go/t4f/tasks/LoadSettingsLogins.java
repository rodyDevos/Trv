
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.SettingsLogins;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadSettingsLogins extends
        AsyncTask<Void, Void, SettingsLogins> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Activity activity;
    private T4FApplication mApplication;

    public LoadSettingsLogins(Activity activity, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.loading_settings_notification);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(SettingsLogins result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected SettingsLogins doInBackground(Void... params) {
        SettingsLogins settings = null;
        String app_id = null;
        String device_id = null;

        mApplication = (T4FApplication) activity.getApplication();

        app_id = activity.getResources().getString(R.string.appid);
        device_id = mApplication.getDeviceID();

        if (app_id == null || app_id.length() == 0 || device_id == null || device_id.length() == 0) {
            return null;
        }

        try {
            String result = getSettingsLogins(app_id, device_id);
            Gson gson = new Gson();
            settings = gson.fromJson(result, SettingsLogins.class);
            Log.e(TAG, "SettingsLogin: " + settings.toString());

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in getting Settings Logins: "
                            + ex.getMessage());

        }

        return settings;
    }

    private String getSettingsLogins(String appId, String deviceId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getSettingsLogins&appid=%s&deviceid=%s",
                        appId, deviceId);

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
