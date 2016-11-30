
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.QuesWithAns;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadQuestion extends AsyncTask<Void, Void, QuesWithAns> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameId;
    private String catId;
    private T4FApplication mApplication;	// 04/21/2014
    private String plaappId;	// 04/21/2014
    private Activity activity;

    
    public LoadQuestion(Activity activity, String gameId, String catId,
            LoadingListener listener) {
        this.activity = activity;
        this.gameId = gameId;
        this.catId = catId;
        mListener = listener;
    }
    /*
    // 04/21/2014
    public LoadQuestion(Activity activity, String gameId, String catId, String plaappId,
            LoadingListener listener) {
        
    	mApplication = (T4FApplication) activity.getApplication();	// 04/21/2014

        this.activity = activity;
        this.gameId = gameId;
        this.catId = catId;
        this.plaappId = mApplication.getPlayerAppID();	// 04/21/2014
        mListener = listener;
    }
    */

    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.loading_ques);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(QuesWithAns ques) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(ques);
        }
        super.onPostExecute(ques);
    }

    @Override
    protected QuesWithAns doInBackground(Void... params) {
        QuesWithAns ques = null;

        if (gameId == null || catId == null)
            return null;

        try {
            //String result = getQuestion(gameId, catId);
        	// 04/21/2014
        	mApplication = (T4FApplication) activity.getApplication();	// 04/21/2014
            plaappId = mApplication.getPlayerAppID();	// 04/21/2014
            Log.e(TAG,"Larry plaappId::  "+plaappId);
            String result = getQuestion(gameId, catId, plaappId);
            
            Gson gson = new Gson();
            ques = gson.fromJson(result, QuesWithAns.class);
            Log.e(TAG, "Ques: " + ques.toString());

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting QuesWithAns: " + ex.getMessage());

        }

        return ques;
    }
    
    /*
    private String getQuestion(String gameId, String catId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getQuestion&gamid=%s&catid=%s",
                        gameId, catId);
        */
    	// 04/21/2014
        private String getQuestion(String gameId, String catId, String plaappId) {
            String result = null;
            String url = String
                    .format(activity.getString(R.string.game_url)+"?f=getQuestion&gamid=%s&catid=%s&plaappid=%s",
                            gameId, catId, plaappId);
            Log.e(TAG,"Larry plaappId::  "+plaappId);
            Log.e(TAG,"Larry url::  "+url);

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
