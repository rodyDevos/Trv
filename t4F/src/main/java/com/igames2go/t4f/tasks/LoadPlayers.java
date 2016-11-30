
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.GetPlayers;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadPlayers extends AsyncTask<Void, Void, GetPlayers> {

    private final String TAG = "T4F";

    private Activity activity;

    private T4FApplication mApplication;

    public static final int FAVORITES = 0;
    public static final int BLOCKED = 1;
    public static final int RANDOM = 2;

    private int groupType = -1;
    private LoadingListener mListener;

    public LoadPlayers(Activity activity, int groupType,
            LoadingListener listener) {
        this.activity = activity;
        this.groupType = groupType;
        mListener = listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = null;

        switch (groupType) {
            case FAVORITES: {
                message = activity.getResources().getString(
                        R.string.loadfavoritefrnds);
                break;
            }
            case BLOCKED: {
                message = activity.getResources().getString(
                        R.string.loadblockedfrnds);
                break;
            }
            case RANDOM: {
                message = activity.getResources().getString(
                        R.string.loadrandomfrnds);
                break;
            }

            default:
                message = "";
        }
        ShowDialog.showLoadingDialog(activity, message);

    }

    @Override
    protected void onPostExecute(GetPlayers players) {

        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(players);
        }
        super.onPostExecute(players);
    }

    @Override
    protected GetPlayers doInBackground(Void... params) {

        mApplication = (T4FApplication) activity.getApplication();

        return getPlayers();

    }

    private GetPlayers getPlayers() {
        String groupOfPlayers = null;
        String plaappId = null;
        String result = null;
        GetPlayers getPlayers = null;

        switch (groupType) {
            case FAVORITES: {
                groupOfPlayers = activity.getResources().getString(
                        R.string.favorites);
                break;
            }
            case BLOCKED: {
                groupOfPlayers = activity.getResources()
                        .getString(R.string.blocked);
                break;
            }
            case RANDOM: {
                groupOfPlayers = activity.getResources().getString(R.string.random);
                break;
            }

            default:
                groupOfPlayers = "";
        }

        plaappId = mApplication.getPlayerAppID();

        if (plaappId != null) {
            try {
                result = getPlayers(plaappId, groupOfPlayers);
                Gson gson = new Gson();
                getPlayers = gson.fromJson(result, GetPlayers.class);

                // Log.e(TAG, "GetPlayers getPla_fb_id " +
                // getPlayers.getFavourites().get(0).getPla_fb_id());
                // Log.e(TAG, "GetPlayers getPla_id " +
                // getPlayers.getFavourites().get(0).getPla_id());

            } catch (Exception ex) {
                Log.e(TAG, "Exception in getPlayers: " + ex.getMessage());
                ex.printStackTrace();

            }
        } else {
            // TODO: when plaappid is null redirect to login page
        }

        return getPlayers;
    }

    private String getPlayers(String plaappId, String groupOfPlayers) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getPlayers&plaappid=%s&what=%s",
                        plaappId, groupOfPlayers);

        url = url.replace(" ", "%20");

        // Log.e(TAG, "url: "+url);

        try {
            result = (String) HttpManager.getResponse(url, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Log.e (TAG,"result: "+result);
        return result;
    }

}
