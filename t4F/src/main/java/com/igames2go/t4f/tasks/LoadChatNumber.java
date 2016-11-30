
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.ChatNumber;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadChatNumber extends AsyncTask<Void, Void, ChatNumber> {

    private final String TAG = "T4F";

    private String gameplaid = null;

    private LoadingListener mListener;
    private Context context;
    public LoadChatNumber(String gameplaid, LoadingListener listener, Context context) {
        this.gameplaid = gameplaid;
        mListener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ChatNumber result) {
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected ChatNumber doInBackground(Void... params) {

        return getChatNumber();
    }

    private ChatNumber getChatNumber() {
        String result = null;
        String url = String
                .format(context.getString(R.string.game_url)+"?f=getChatNumber&gamplaid=%s",
                        gameplaid);

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

        try {
            Gson gson = new Gson();
            ChatNumber chatnum = gson.fromJson(result, ChatNumber.class);
            return chatnum;
        } catch (Exception ex) {
            Log.e(TAG, "Exception in getChatNumber: " + ex.getMessage());

        }

        return null;
    }

}
