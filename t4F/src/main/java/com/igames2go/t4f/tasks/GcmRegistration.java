
package com.igames2go.t4f.tasks;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.igames2go.t4f.Activities.LoadingListener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class GcmRegistration extends AsyncTask<Void, Void, String> {

    private final String TAG = "T4F";
    private final String SENDER_ID = "42464181777";
    private LoadingListener mListener;
    private Context context;

    public GcmRegistration(Context context, LoadingListener listener) {
        this.context = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String regId) {
        if (mListener != null) {
            mListener.onLoadingComplete(regId);
        }
        super.onPostExecute(regId);
    }

    @Override
    protected String doInBackground(Void... params) {
        String regid = "";
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

            if (gcm != null)
                regid = gcm.register(SENDER_ID);

        } catch (IOException ex) {
            Log.e(TAG, "Error :" + ex.getMessage());
        }
        return regid;
    }

}
