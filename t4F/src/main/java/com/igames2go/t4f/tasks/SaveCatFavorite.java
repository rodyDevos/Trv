
package com.igames2go.t4f.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SaveCatFavorite extends
        AsyncTask<Void, Void, String> {

    private final String TAG = "T4F";
    private LoadingListener mListener;

    private String catId;
    private int flag;
    private Activity activity;
    private T4FApplication mApplication;

    public SaveCatFavorite(Activity activity, String catId, int flag, LoadingListener listener) {
        mListener = listener;
        this.catId = catId;
        this.flag = flag;
        this.activity = activity;
        
        mApplication = (T4FApplication)activity.getApplication();	// 04/21/2014        
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Saving...");
    }

    @Override
    protected void onPostExecute(String result) {
    	ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected String doInBackground(Void... params) {
    	String result = null;
    	
    	String os = activity.getResources().getString(R.string.os);
    	String plaappid = mApplication.getPlayerAppID();
    	
        if (plaappid == null || plaappid.length() == 0) {
            return null;
        }

        try {
        	JSONObject obj = setCatFavorite(plaappid, catId, flag);
            result = obj.getString("setCatFavorite");

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in Saving Category Favorite Status: "
                            + ex.getMessage());

        }

        return result;
    }

    private JSONObject setCatFavorite(String plaappId, String catId, int flag) {
    	
        String result = null;
        JSONObject json = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setCatFavorite&plaappid=%s&catid=%s&flag=%s",
                        plaappId, catId, flag);

        Log.e(TAG, "url: " + url);
        try {
            result = (String) HttpManager.getResponse(url, false);
            Log.d("Result", result);
	        json = new JSONObject(result);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return json;
    }

}
