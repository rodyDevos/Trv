
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.FriendProperties;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.ImageButton;

import java.io.IOException;

public class CheckFriendProperties
{

    private final String TAG = "T4F";

    private Activity activity;

    private ImageButton mButton;

    private T4FApplication mApplication;

    private String friendsfb_Id = null;
    private JSONObject mGraphUser;
    private LoadingListener mListener = null;

    public CheckFriendProperties(Activity activity, JSONObject graphUser,
            ImageButton button) {
        this.activity = activity;
        this.friendsfb_Id = graphUser.optString("id");
        mGraphUser = graphUser;
        this.mButton = button;

    }

    public CheckFriendProperties(Activity activity, String friendsfb_Id, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
        this.friendsfb_Id = friendsfb_Id;
    }

    protected void onPostExecute(final FriendProperties friendProperties) {
        if (mListener != null) {
            mListener.onLoadingComplete(friendProperties);
        }
        else
        {
            if (friendProperties != null) {
                if (friendProperties.getFriendProperties().get(0)
                        .getPla_fb_name().trim().length() == 0)
                    mButton.setBackgroundResource(R.drawable.icon_request);
                else
                    mButton.setBackgroundResource(R.drawable.icon_additionsign);

                // friendProperties.getFriendProperties().get(0).setGraphUser(mGraphUser);
                mButton.setTag(friendProperties.getFriendProperties()
                        .get(0));
            }

        }
    }

    public void doInBackground() {

        mApplication = (T4FApplication) activity.getApplication();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                final FriendProperties friendProperties = getFriendProperties();
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        onPostExecute(friendProperties);
                    }
                });

            }
        };
        Thread thread = new Thread(r);
        thread.start();

    }

    private FriendProperties getFriendProperties() {

        String result = null;
        String appId = activity.getResources().getString(R.string.appid);
        String plaappId = mApplication.getPlayerAppID();

        if (plaappId != null) {

            appId = ((appId == null) ? "" : appId);
            try {
                result = getFriendProperties(appId, plaappId, friendsfb_Id);
                Gson gson = new Gson();
                FriendProperties getFriendProperties = gson.fromJson(result,
                        FriendProperties.class);
                return getFriendProperties;
            } catch (Exception ex) {
                Log.e(TAG, "Exception in getPlayers: " + ex.getMessage());

            }

        } else {
            // TODO: when plaappid is null redirect to login page
        }

        return null;
    }

    private String getFriendProperties(String appId, String plaappId,
            String friendsfbid) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getFriendProperties&appid=%s&plaappid=%s&friendfbid=%s",
                        appId, plaappId, friendsfbid);

        url = url.replaceAll(" ", "%20");

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
