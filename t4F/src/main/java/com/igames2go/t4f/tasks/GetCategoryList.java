
package com.igames2go.t4f.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.Categories;
import com.igames2go.t4f.data.LeaderboardPlayersDataObject;
import com.igames2go.t4f.data.SelCategoriesDataObject;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
public class GetCategoryList extends AsyncTask<Void, Void, List<SelCategoriesDataObject>> {

    private final String TAG = "T4F";
    private Activity activity;
    private T4FApplication mApplication;
    private LoadingListener mListener;

    public GetCategoryList(Activity activity, LoadingListener listener) {
        this.activity = activity;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(R.string.loading);
        ShowDialog.showLoadingDialog(activity, message);

    }

    @Override
    protected void onPostExecute(List<SelCategoriesDataObject> categoryList) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onLoadingComplete(categoryList);
        }
        super.onPostExecute(categoryList);
    }

    @Override
    protected List<SelCategoriesDataObject> doInBackground(Void... params) {
        ArrayList<SelCategoriesDataObject> categoryList = new ArrayList<SelCategoriesDataObject>();
        String plaappId = null;

        mApplication = (T4FApplication) activity.getApplication();

        plaappId = mApplication.getPlayerAppID();
        if (plaappId == null)
            return null;

        try {
            JSONObject result = getCategories(plaappId);

            JSONArray categories = result.getJSONArray("getCatList");
            if(categories.length() > 0){
                for(int i = 0; i < categories.length(); i++){
                    JSONObject category = categories.getJSONObject(i);
                    SelCategoriesDataObject categoryObject = new SelCategoriesDataObject();
                    categoryObject.parseJSONObject(category);
                    categoryList.add(categoryObject);
                }
            }

        } catch (Exception ex) {
            Log.e(TAG, "Exception in getting Categories: " + ex.getMessage());

        }

        return categoryList;
    }

    private JSONObject getCategories(String plaappId) {
        String result = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getCatList&plaappid=%s", plaappId);
        JSONObject json = null;

        Log.e(TAG, "url: " + url);
        try {
            result = (String) HttpManager.getResponse(url, false);

            Log.d("Result", result);
            json = new JSONObject(result);

        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json;
    }

}
