
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SearchLimit;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class CheckSearchLimit extends AsyncTask<Void, Void, SearchLimit> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private Context context;

    public CheckSearchLimit(LoadingListener listener, Context context) {
    	this.context = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SearchLimit searchLimit) {
        if (mListener != null) {
            mListener.onLoadingComplete(searchLimit);
        }
        super.onPostExecute(searchLimit);
    }

    @Override
    protected SearchLimit doInBackground(Void... params) {

        String result = null;
        SearchLimit searchLimit = null;

        try {
            result = getSearchLimit();
            Gson gson = new Gson();
            searchLimit = gson.fromJson(result, SearchLimit.class);

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getSearchLimit: " + ex.getMessage());

        }

        return searchLimit;
    }

    private String getSearchLimit() {
        String result = null;
        String url = String
                .format(context.getString(R.string.game_url)+"?f=getSearchLimit");

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
