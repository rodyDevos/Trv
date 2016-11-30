
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.EndOfTurn;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

public class DoEndOfTurn extends AsyncTask<Void, Void, EndOfTurn> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameplaId;
    private String catId;
    private String isSurprise;
    private String quesId;
    private String ansResult;
    private String time;
    private String totaltimeallowed;
    private T4FApplication mApplication;
    private Activity activity;

    public DoEndOfTurn(Activity activity, String gameplaId, String catId,
            String isSurprise, String quesId, String ansResult, String time,
            String totalTime, LoadingListener listener) {
        this.activity = activity;
        this.gameplaId = gameplaId;
        this.catId = catId;
        this.isSurprise = isSurprise;
        this.quesId = quesId;
        this.ansResult = ansResult;
        this.time = time;
        mListener = listener;
        totaltimeallowed = totalTime;
        mApplication = (T4FApplication)activity.getApplication();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.submittinganswer);
        ((T4FApplication)activity.getApplication()).refresh = true;
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(EndOfTurn result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected EndOfTurn doInBackground(Void... params) {

        // String totaltimeallowed =
        // activity.getResources().getString(R.string.timer_seconds);
        String os = activity.getResources().getString(R.string.os);
        try {
            String result = endOfTurn(gameplaId, catId, isSurprise, quesId,
                    ansResult, time, totaltimeallowed, os);
            Log.e(TAG, "result: " + result.toString());
            Gson gson = new Gson();
            EndOfTurn endTurn = gson.fromJson(result, EndOfTurn.class);
            Log.e(TAG, "endTurn: " + endTurn.toString());
            
            if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN) {
                String url = String
                        .format(activity.getString(R.string.game_url)+"?f=autoTurn&gamplaid=%s&os=%s",
                                gameplaId, os);
                try {
                    String str = (String)HttpManager.getResponse(url, false);
                    if(TextUtils.isEmpty(str)==false){
                    	try {
                    		Log.e("str", str);
							str = str.replace("{", "").replace("}", "").replace("[", "").replace("]", "").replace("\"autoturn\"", "").replace("\"", "").replace(":", "").replace("alert_title", "").replace("alert_body", "").replaceAll("\n", "");
							Log.e("str", str);
							String[] autoturn= str.split(",");
							T4FApplication app = ((T4FApplication)activity.getApplication());
							app.autoturntitle = autoturn[0].trim();
							app.autoturnmessage = autoturn[1].trim();
							Log.e("str", app.autoturntitle+"");
							Log.e("str", app.autoturnmessage+"");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//                    	String str1=str.substring(str.indexOf("\"alert_title\":\"")+("\"alert_title\":\"").length(),str.indexOf("\"alert_body\":"));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            return endTurn;

        } catch (Exception ex) {
            Log.e(TAG, "Exception in EndOfTurn: " + ex.getMessage());
        }

        return null;
    }

    private String endOfTurn(String gameplaId, String catId, String isSurprise,
            String quesId, String ansResult, String time,
            String totaltimeallowed, String os) {

        String result = null;
        
        try {
			String version = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
			String url = String
	                .format(activity.getString(R.string.game_url)+"?f=endOfTurn&gamplaid=%s&catid=%s&surprise=%s&queid=%s&answer=%s&time=%s&timeallowed=%s&os=%s&version=%s",
	                        gameplaId, catId, isSurprise, quesId, ansResult, time,
	                        totaltimeallowed, os, version);
	        Log.i("URL", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + url);
	        
	        url = url.replaceAll(" ", "%20");
	        result = (String) HttpManager.getResponse(url, false);
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
