
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.Categories;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;

public class LoadCategories extends AsyncTask<Void, Void, Categories> {

    private final String TAG = "T4F";
    private Activity activity;
    private T4FApplication mApplication;
    private LoadingListener mListener;
    private String gameId;

    public LoadCategories(Activity activity, String gameId,
            LoadingListener listener) {
        this.activity = activity;
        this.gameId = gameId;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(R.string.loading);
        ShowDialog.showLoadingDialog(activity, message);

    }

    @Override
    protected void onPostExecute(Categories cat) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(cat);
        }
        super.onPostExecute(cat);
    }

    @Override
    protected Categories doInBackground(Void... params) {
        Categories cat = null;
        String plaId = null;

        if (gameId == null)
            return null;

        mApplication = (T4FApplication) activity.getApplication();

        plaId = mApplication.getPlayerID();
        if (plaId == null)
            return null;

        try {
            String result = getCategories(gameId, plaId);
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new StringReader(result));
            reader.setLenient(true);
            cat = gson.fromJson(reader, Categories.class);
            Log.e(TAG, "Categories: " + cat.toString());

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting Categories: " + ex.getMessage());

        }

        return cat;
    }

    private String getCategories(String gameId, String plaId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getCategories&gamid=%s&plaid=%s",
                        gameId, plaId);

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
