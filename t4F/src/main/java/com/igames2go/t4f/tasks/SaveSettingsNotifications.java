
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.SaveSettingsNotificationsResult;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class SaveSettingsNotifications extends
        AsyncTask<Void, Void, SaveSettingsNotificationsResult> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Activity activity;
    private T4FApplication mApplication;
    private String notification;
    private String game_start;
    private String your_turn;
    private String anyones_turn;
    private String chat;

    public SaveSettingsNotifications(Activity activity, String notification,
            String game_start, String your_turn, String anyones_turn,
            String chat, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
        this.notification = notification;
        this.game_start = game_start;
        this.your_turn = your_turn;
        this.anyones_turn = anyones_turn;
        this.chat = chat;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.saving_settings_notification);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(SaveSettingsNotificationsResult result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected SaveSettingsNotificationsResult doInBackground(Void... params) {
        SaveSettingsNotificationsResult settingsResult = null;
        String plaapp_Id = null;

        mApplication = (T4FApplication) activity.getApplication();
        plaapp_Id = mApplication.getPlayerAppID();

        if (plaapp_Id == null || plaapp_Id.length() == 0) {
            // something is wrong....need to login again.
            return null;
        }

        try {
            String result = setSettingsNotifications(plaapp_Id);
            Gson gson = new Gson();
            settingsResult = gson.fromJson(result,
                    SaveSettingsNotificationsResult.class);
            Log.e(TAG, "SettingsResult: " + settingsResult.toString());

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in Saving Notification Settings: "
                            + ex.getMessage());

        }

        return settingsResult;
    }

    private String setSettingsNotifications(String plaappId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setSettingsNotification&plaappid=%s&notification=%s&game_start=%s&your_turn=%s&anyones_turn=%s&chat=%s",
                        plaappId, notification, game_start, your_turn,
                        anyones_turn, chat);

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
