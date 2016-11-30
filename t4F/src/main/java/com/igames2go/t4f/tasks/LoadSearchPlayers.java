
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.PlayersDataObject;
import com.igames2go.t4f.data.SearchPlayers;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class LoadSearchPlayers extends AsyncTask<Void, Void, SearchPlayers> {

    private final String TAG = "T4F";

    private Activity activity;

    private T4FApplication mApplication;

    private String searchStr = null;
    private String setIndicator = null;

    private LoadingListener mListener = null;

    public LoadSearchPlayers(Activity activity, String searchStr,
            String setIndicator, LoadingListener listener) {
        this.activity = activity;
        this.searchStr = searchStr;
        this.setIndicator = setIndicator;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(R.string.searching);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(SearchPlayers players) {

        ShowDialog.removeLoadingDialog();

        if (mListener != null) {
            mListener.onLoadingComplete(players);
        }
        super.onPostExecute(players);
    }

    @Override
    protected SearchPlayers doInBackground(Void... params) {

        String result = null;
        SearchPlayers players = null;
        mApplication = (T4FApplication) activity.getApplication();

        String plaappId = mApplication.getPlayerAppID();

        if (plaappId != null) {
            try {
                result = searchPlayers(plaappId, searchStr, setIndicator);
                Gson gson = new Gson();
                players = gson.fromJson(result, SearchPlayers.class);
                if (players != null && !players.getSearchPlayers().isEmpty())
                {
                    removeBlockedPlayers(players);
                }

            } catch (Exception ex) {
                Log.e(TAG,
                        "Exception in getting Search Players: "
                                + ex.getMessage());

            }
        } else {
            // TODO: when plaappid is null redirect to login page
        }

        return players;
    }

    private String searchPlayers(String plaappid, String searchStr,
            String setIndicator) {
        String result = null;
        String versionName = "";
        try {
			versionName = activity.getPackageManager().getPackageInfo(
					activity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String url = String
                .format(activity.getString(R.string.game_url)
                        + "?f=searchPlayers&plaappid=%s&string=%s&set=%s&version=%s",
                        plaappid, searchStr, setIndicator, versionName);

        Log.d("Search Player Url", url);
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

        return result;
    }

    private void removeBlockedPlayers(SearchPlayers playersObj)
    {
        ArrayList<PlayersDataObject> playersList = (ArrayList<PlayersDataObject>) playersObj
                .getSearchPlayers();

        final Iterator<PlayersDataObject> iterator = playersList.iterator();
        while (iterator.hasNext()) {
            final PlayersDataObject item = iterator.next();
            if ((item.getBlocked()).equals("1")) {
                iterator.remove();
            }
        }
    }

}
