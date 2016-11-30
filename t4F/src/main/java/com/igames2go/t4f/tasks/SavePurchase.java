
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

public class SavePurchase extends
        AsyncTask<Void, Void, String> {

    private final String TAG = "T4F";
    private LoadingListener mListener;

    private String productId;
    private String orderId;
    private Activity activity;
    private T4FApplication mApplication;

    public SavePurchase(Activity activity, String productId, String orderId, LoadingListener listener) {
        mListener = listener;
        this.productId = productId;
        this.orderId = orderId;
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

    	String plaappid = mApplication.getPlayerAppID();
    	
        if (plaappid == null || plaappid.length() == 0) {
            return null;
        }

        try {
        	JSONObject obj = setPurchase(plaappid, productId, orderId);
            result = obj.getString("setPurchase");

        } catch (Exception ex) {
            Log.e(TAG,
                    "Exception in Saving Purchase Status: "
                            + ex.getMessage());

        }

        return result;
    }

    private JSONObject setPurchase(String plaappId, String productId, String orderId) {
    	
        String result = null;
        JSONObject json = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setPurchase&plaappid=%s&productid=%s&orderid=%s",
                        plaappId, productId, orderId);

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
