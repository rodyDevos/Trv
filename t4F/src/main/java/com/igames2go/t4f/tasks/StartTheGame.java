
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.Activities.SelectCategory.SelectCategoryActivity.StartGameResult;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.data.GameId;
import com.igames2go.t4f.data.GamePlayerId;
import com.igames2go.t4f.data.StartedGameMessages;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class StartTheGame extends AsyncTask<Void, Void, StartGameResult> {

    private final String TAG = "T4F";

    private Activity activity;

    private T4FApplication mApplication;

    private ArrayList<String> playerIds;
    private LoadingListener mListener;
    StartGameResult mResult = null;
    private String categoryIds;

    public StartTheGame(Activity activity,
            ArrayList<String> playerIds, String categoryIds, StartGameResult result,
            LoadingListener listener) {
        this.activity = activity;
        this.playerIds = playerIds;
        mListener = listener;
        mResult = result;

        this.categoryIds = categoryIds;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.startinggame);
        ShowDialog.showLoadingDialog(activity, message);

    }

    @Override
    protected void onPostExecute(StartGameResult result) {
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        ShowDialog.removeLoadingDialog();
        super.onPostExecute(result);
    }

    @Override
    protected StartGameResult doInBackground(Void... params) {
        String gameid = null;
        String gameplaid = null;

        if (mResult == null)
            return null;

        mApplication = (T4FApplication) activity.getApplication();

        gameid = startGame(categoryIds);

        Log.e(TAG, "gameId: " + gameid);
        if (gameid != null && Integer.parseInt(gameid) <= 0) {
            Log.e(TAG, "Error in getting gameId: ");
            return null;
        }

        gameplaid = setGamePlayer(gameid, mApplication.getPlayerID(), "add");
        Log.e(TAG, "setGamePlayer[" + 0 + "]" + " result: " + gameplaid);

        if (playerIds != null) {
            for (int index = 0; index < playerIds.size(); index++) {
                String gameplaid_1 = setGamePlayer(gameid, playerIds.get(index), "add");
                Log.e(TAG, "setGamePlayer[" + index + 1 + "]" + " result: "
                        + gameplaid_1);

            }
        } else {
            Log.e(TAG, "Error: Friends player list is null");

            return null;
        }

        if (startGameMessages(gameid,
                activity.getResources().getString(R.string.os)))
            Log.e(TAG, "startGameMessages is succesfull");
        else
            Log.e(TAG, "startGameMessages is unsuccesfull");

        mResult.setGameId(gameid);
        mResult.setGamePlaID(gameplaid);
        return mResult;
    }

    private String startGame(String categoryIds) {
        String resGameId = null;
        String plaappId = mApplication.getPlayerAppID();

        if (plaappId != null) {
            try {
                resGameId = startGame(plaappId, categoryIds);
                Gson gson = new Gson();
                GameId gameid = gson.fromJson(resGameId, GameId.class);
                if (gameid != null)
                    return gameid.getGameId();
            } catch (Exception ex) {
                Log.e(TAG, "Exception in startGame: " + ex.getMessage());
            }

        } else {
            // TODO: when plaappid is null redirect to login page
        }

        return null;
    }

    private String startGame(String plaappId, String categoryIds) {
        String resGameId = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=startGame&plaappid=%s&catstring=%s",
                        plaappId, categoryIds);

        url = url.replace(" ", "%20");

        try {
            resGameId = (String) HttpManager.getResponse(url, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resGameId;
    }

    private String setGamePlayer(String gameId, String playerId, String action) {
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setGamePlayer&gamid=%s&plaid=%s&action=%s",
                        gameId, playerId, action);

        url = url.replace(" ", "%20");

        try {
            String result = (String) HttpManager.getResponse(url, false);
            Gson gson = new Gson();
            GamePlayerId gameplaid = gson.fromJson(result, GamePlayerId.class);
            if (gameplaid != null)
                return gameplaid.getGamePlayerId();

        } catch (Exception ex) {
            Log.e(TAG, "Exception in startGamePlayer: " + ex.getMessage());
        }

        return null;
    }

    private boolean startGameMessages(String gameId, String os) {
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=startGameMessages&gamid=%s&os=%s",
                        gameId, os);

        url = url.replace(" ", "%20");

        try {
            String result = (String) HttpManager.getResponse(url, false);
            Gson gson = new Gson();
            StartedGameMessages gamemsg = gson.fromJson(result,
                    StartedGameMessages.class);
            if (gamemsg != null) {
                int res_gamemsg = Integer.parseInt(gamemsg
                        .getStartGameMessages());

                if (res_gamemsg > 0)
                    return true;
            }

        } catch (Exception ex) {
            Log.e(TAG, "Exception in startGameMessages: " + ex.getMessage());
        }

        return false;
    }

}
