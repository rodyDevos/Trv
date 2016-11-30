
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.HomeScreen.LoadingCompleteListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.PlayerId;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class LoadPlayerId extends AsyncTask<Void, Void, String> {

    private final String TAG = "T4F";

    private Activity activity;
    private T4FApplication mApplication;
    private LoadingCompleteListener mListener;
    
    public LoadPlayerId(Activity activity,
            LoadingCompleteListener listener) {
        this.activity = activity;
        mListener = listener;
        mApplication = (T4FApplication) activity.getApplication();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShowDialog.showLoadingDialog(activity, "Loading...");
    }

    @Override
    protected void onPostExecute(String playerId) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onComplete(playerId);
        }
        super.onPostExecute(playerId);
    }

    @Override
    protected String doInBackground(Void... params) {
    	return setPlayer();
    }

    private String setPlayer() {
        String result = null;
        String app_id = activity.getResources().getString(R.string.appid);
        String fb_id = FacebookManager.fbId;
        String fb_name = FacebookManager.fbName;
        String os = activity.getResources().getString(R.string.os);
        String os_version = android.os.Build.VERSION.SDK_INT+"";
        String device_id = "";
        String device_model = android.os.Build.DEVICE;
        String locale = "en_US";
        String location = null;
        String nologplayerid = "";
        //String email = mApplication.email;
        
        app_id = ((app_id == null) ? "" : app_id);
        
        device_id = mApplication.getDeviceID();

        fb_id = ((fb_id == null) ? "" : fb_id);
        fb_name = ((fb_name == null) ? "You" : fb_name);
        os = ((os == null) ? "" : os);
        os_version = ((os_version == null) ? "" : os_version);
        device_id = ((device_id == null) ? "" : device_id);
        device_model = ((device_model == null) ? "" : device_model);
        locale = ((locale == null) ? "" : locale);
        location = ((location == null) ? "" : location);
        
        result = setPlayer(app_id, fb_id, fb_name, os, os_version, device_id,
                device_model, locale, location,"","", nologplayerid);

        Gson gson = new Gson();

        try {

            PlayerId plaid = gson.fromJson(result, PlayerId.class);
            String playerId = plaid.getPlaid();
            return playerId;
        } catch (Exception ex) {
            Log.e(TAG, "Exception in setPlayer(): " + ex.getMessage());

        }

        return "0";
    }

    private String setPlayer(String app_id, String fb_id, String fb_name,
            String os, String os_version, String device_id,
            String device_model, String locale, String location,String email, String regID, String nologplayerid) {
    	
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=setPlayer&os=%s&osversion=%s&devicemodel=%s&appid=%s&fbid=%s&fbname=%s&deviceid=%s&locale=%s&location=%s&email=%s&regid=%s&nologplayerid=%s",
                        os,os_version,device_id,app_id, fb_id, fb_name, device_id, locale, location,email,regID,nologplayerid);
        String result = "";

        url = url.replaceAll(" ", "%20");
        Log.i("url:", url);
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
