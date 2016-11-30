
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FacebookFriendsList;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadFacebookFriends extends
        AsyncTask<Void, Void, FacebookFriendsList> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Activity activity;
    private String url = null;

    public LoadFacebookFriends(Activity activity, String url, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.loading_facebook_friends);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(FacebookFriendsList result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected FacebookFriendsList doInBackground(Void... params) {
        FacebookFriendsList friendslist = null;

        try {
            String result = getFacebookFriends(url);
            Gson gson = new Gson();
            friendslist = gson.fromJson(result, FacebookFriendsList.class);
            Log.e(TAG, "FacebookFriendsList: " + friendslist.toString());

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in getting facebook friends: "
                            + ex.getMessage());

        }

        return friendslist;
    }

    private String getFacebookFriends(String url) {
        String result = null;
        url = url.replaceAll(" ", "%20");
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
