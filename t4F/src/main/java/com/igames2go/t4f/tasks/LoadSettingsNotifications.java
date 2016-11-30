
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.SettingsNotifications;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadSettingsNotifications extends
        AsyncTask<Void, Void, SettingsNotifications> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Activity activity;
    private T4FApplication mApplication;

    public LoadSettingsNotifications(Activity activity, LoadingListener listener) {
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
    protected void onPostExecute(SettingsNotifications result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected SettingsNotifications doInBackground(Void... params) {
        SettingsNotifications settings = null;
        String plaapp_Id = null;

        mApplication = (T4FApplication) activity.getApplication();
        plaapp_Id = mApplication.getPlayerAppID();

        if (plaapp_Id == null || plaapp_Id.length() == 0) {
            // something is wrong....need to login again.
            return null;
        }

        try {
            String result = getSettingsNotifications(plaapp_Id);
            Gson gson = new Gson();
            settings = gson.fromJson(result, SettingsNotifications.class);
            Log.e(TAG, "Settings: " + settings.toString());

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in getting Notification Settings: "
                            + ex.getMessage());

        }

        return settings;
    }

    private String getSettingsNotifications(String plaappId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getSettingsNotification&plaappid=%s",
                        plaappId);

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
