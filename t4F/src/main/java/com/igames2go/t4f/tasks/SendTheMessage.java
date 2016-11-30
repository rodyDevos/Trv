
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SendMsgResult;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class SendTheMessage extends AsyncTask<Void, Void, SendMsgResult> {

    private final String TAG = "T4F";
    private LoadingListener mListener;
    private String gameplaId;
    private String msgType;
    private String msgText;
    private String quesId;
    private Activity activity;

    public SendTheMessage(Activity activity, String gameplaId, String msgType,
            String msgText, String quesId, LoadingListener listener) {
        this.activity = activity;
        this.gameplaId = gameplaId;
        this.msgType = msgType;
        this.msgText = msgText;
        this.quesId = quesId;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources()
                .getString(R.string.sending_msg);
        ShowDialog.showLoadingDialog(activity, message);
    }

    @Override
    protected void onPostExecute(SendMsgResult result) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(result);
        }
        super.onPostExecute(result);
    }

    @Override
    protected SendMsgResult doInBackground(Void... params) {
        SendMsgResult msgresult = null;

        if (gameplaId == null || msgText == null || quesId == null)
            return null;

        try {
        	/*
        	msgText = msgText.replace("\"", "");
        	msgText = msgText.replace("&", "");
        	msgText = msgText.replace("=", "");
        	msgText = msgText.replace("http://", "");
        	msgText = msgText.replace("HTTP://", "");
        	*/
        	// 03/14/2014 lp
        	msgText = msgText.replace("\"", "");
        	msgText = msgText.replace("&", " and ");
        	msgText = msgText.replace("=", " equals ");
        	msgText = msgText.replace("http://", "http ");
        	msgText = msgText.replace("HTTP://", "HTTP ");
           	msgText = msgText.replace("https://", "https ");
        	msgText = msgText.replace("HTTPS://", "HTTPS ");

        	msgText = msgText.replace("%", " percent ");
        	msgText = msgText.replace("@", " at ");
        	msgText = msgText.replace("#", " pound_symbol ");
        	//msgText = msgText.replace("*", " at ");
        	//msgText = msgText.replace("+", " plus ");
        	//msgText = msgText.replace("-", " at ");
        	msgText = msgText.replace("<", "*");
        	msgText = msgText.replace(">", "*");
        	msgText = msgText.replace("~", "*");
        	msgText = msgText.replace("^", "*");
        	msgText = msgText.replace("{", "(");
        	msgText = msgText.replace("}", ")");
        	msgText = msgText.replace("[", "(");
        	msgText = msgText.replace("]", ")");
        	msgText = msgText.replace("|", "-");
        	//msgText = msgText.replace("/", " at ");
        	//msgText = msgText.replace("$", " at ");
        	//msgText = msgText.replace(":", " at ");
        	//msgText = msgText.replace(";", " at ");
        	
        	String result = sendMessage(gameplaId, msgType, msgText, quesId);
            Gson gson = new Gson();
            msgresult = gson.fromJson(result, SendMsgResult.class);
            Log.d(TAG, "Sending Message result: " + msgresult.toString());

        } catch (Exception ex) {
            Log.d(TAG, "Exception in Sending Message: " + ex.getMessage());

        }

        return msgresult;
    }

    private String sendMessage(String gameplaId, String msgType,
            String msgText, String quesId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=sendMessage&gamplaid=%s&messagetype=%s&messagetext=%s&queid=%s",
                        gameplaId, msgType, msgText, quesId);

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

}
