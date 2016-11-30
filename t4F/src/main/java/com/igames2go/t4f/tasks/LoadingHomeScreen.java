
package com.igames2go.t4f.tasks;

import com.google.gson.Gson;
import com.igames2go.t4f.Activities.HomeScreen.LoadingCompleteListener;
import com.igames2go.t4f.Activities.PlayerName;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.GamesDetail;
import com.igames2go.t4f.data.LoginUser;
import com.igames2go.t4f.data.PlayerAppId;
import com.igames2go.t4f.data.PlayerId;
import com.igames2go.t4f.data.PlayerImage;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ShowDialog;
import com.learn2crack.library.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadingHomeScreen extends AsyncTask<Void, Void, GamesDetail> {

    private final String TAG = "T4F";

    private Activity activity;

    private JSONObject gFacebookUser;
    private LoginUser gLoginUser;
    
    private T4FApplication mApplication;

    private LoadingCompleteListener mListener;
    private boolean isRefresh = false;
    private String registrationId = null;
    SharedPreferences prefs;
    
    public LoadingHomeScreen(Activity activity,
            LoadingCompleteListener listener, boolean isRefresh, String registrationId) {
        this.activity = activity;
        mListener = listener;
        this.isRefresh = isRefresh;
        this.registrationId = registrationId;
        prefs = this.activity.getSharedPreferences(
    				this.activity.getApplication().getPackageName(), Context.MODE_PRIVATE);
        mApplication = (T4FApplication) activity.getApplication();
        gLoginUser = mApplication.getLoginUser();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String message = activity.getResources().getString(
                R.string.loadinggames);
        ShowDialog.showLoadingDialog(activity, message);

    }

    @Override
    protected void onPostExecute(GamesDetail games) {
        ShowDialog.removeLoadingDialog();
        if (mListener != null) {
            mListener.onComplete(games);
        }
        super.onPostExecute(games);
    }

    @Override
    protected GamesDetail doInBackground(Void... params) {
        
    	if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN){
    		String playerId = gLoginUser.getUserId() + "";
        	String app_id = activity.getResources().getString(R.string.appid);
        	setPlayerInfo(app_id, playerId);
        	mApplication.setPlayerImage(getPlayerImageName(app_id, playerId));
    	}
    	
        if (isRefresh || setPlayer()) // to get PlayerId
        {
    	    Log.e(TAG, "setPlayer is successful");
            if (getPlayerAppIdApi()) // to get PlayerAppId
            {
                Log.e(TAG, "getPlayerAppId is successful");

                return getGames("");

            } else {
                // getPlayerAppIdApi failed, now call getPlayerAppIdApi again or
                // go to login page
            }
	    } else {
	        // setPlayer failed, now load again or go to login page
	    }
        return null;
    }

    private boolean setPlayer() {
        String result = null;
        String app_id = activity.getResources().getString(R.string.appid);
        String fb_id = null;
        String fb_name = null;
        String os = activity.getResources().getString(R.string.os);
        String os_version = android.os.Build.VERSION.SDK_INT+"";
        String device_id = "";
        String device_model = android.os.Build.DEVICE;
        String locale = "en_US";
        String location = null;
        String nologplayerid = "";
        //String email = mApplication.email;
        
        app_id = ((app_id == null) ? "" : app_id);
                
        /*
        if (mApplication.play_mode == T4FApplication.PLAY_MODE_FB_LOGIN ) {
        	if(gFacebookUser != null){
	            fb_id = gFacebookUser.getId();
	            fb_name = gFacebookUser.getName();
	            try {
	                location = gFacebookUser.getLocation().getCity();
	                
	            } catch (Exception e) {
	            	e.printStackTrace();
	                location = "";
	            }
        	}
        }else */
        if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN ) {
        	String playerId = gLoginUser.getUserId() + "";
        	
            nologplayerid = playerId;
            fb_name = gLoginUser.getUserName();
           
        }else if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN){
        	
        }
        

        device_id = mApplication.getDeviceID();

        fb_id = ((fb_id == null) ? "" : fb_id);
        fb_name = ((fb_name == null) ? "You" : fb_name);
        os = ((os == null) ? "" : os);
        os_version = ((os_version == null) ? "" : os_version);
        device_id = ((device_id == null) ? "" : device_id);
        device_model = ((device_model == null) ? "" : device_model);
        locale = ((locale == null) ? "" : locale);
        location = ((location == null) ? "" : location);
        registrationId = ((registrationId == null) ? "" : registrationId);

        result = setPlayer(app_id, fb_id, fb_name, os, os_version, device_id,
                device_model, locale, location,"",registrationId, nologplayerid);

        Gson gson = new Gson();

        try {

            PlayerId plaid = gson.fromJson(result, PlayerId.class);
            mApplication.setPlayerID(plaid.getPlaid());
            
            int playerId = Integer.parseInt(plaid.getPlaid());
            Log.e(TAG, "PlaId : " + playerId);
            if (playerId > 0) {
            	if(((T4FApplication)activity.getApplication()).getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN) {
            		
            		if (prefs.contains("nologplayerid")== false ) {
						prefs.edit().putString("nologplayerid",
								"" + playerId + "").commit();
						Log.e("setPlayerid", playerId + "");
						
					}
            	}
                return true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Exception in setPlayer(): " + ex.getMessage());

        }

        return false;
    }

    private String setPlayer(String app_id, String fb_id, String fb_name,
            String os, String os_version, String device_id,
            String device_model, String locale, String location,String email, String regID, String nologplayerid1) {
    	String nologplayerid = "";
    	
    	if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN) {
    		
    		nologplayerid = prefs.getString("nologplayerid", "");
    		Log.e("getPlayerid",nologplayerid+" iiii");
    	}else if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN){
    		nologplayerid = nologplayerid1;
    	}
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

    private boolean getPlayerAppIdApi() {
        String appId = activity.getResources().getString(R.string.appid);
        String playerId = null;
        String result = null;

        appId = ((appId == null) ? "" : appId);
        try {
            playerId = mApplication.getPlayerID();
            playerId = ((playerId == null) ? "" : playerId);

            result = getPlayerAppIdApi(appId, playerId);

            Gson gson = new Gson();
            PlayerAppId plaappid = gson.fromJson(result, PlayerAppId.class);
            mApplication.setPlayerAppID(plaappid.getPlaappid());

            int playerappid = Integer.parseInt(plaappid.getPlaappid());
            Log.e(TAG, "plaappid : " + plaappid.getPlaappid() + " in int "
                    + playerappid);
            if (playerappid > 0)
                return true;
        } catch (Exception ex) {
            Log.e(TAG, "Exception in getPlayerAppIdApi(): " + ex.getMessage());
        }

        return false;
    }

    private String getPlayerAppIdApi(String appId, String playerId) {
        String resPlaId = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getplaapp_id&appid=%s&plaid=%s",
                        appId, playerId);

        url = url.replaceAll(" ", "%20");
        Log.d("Get PlayerAppId", url);
        try {
            resPlaId = (String) HttpManager.getResponse(url, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resPlaId;
    }

    private String getPlayerName(String appId, String playerId) {
        String playerName = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getPlayerName&appid=%s&plaid=%s",
                        appId, playerId);

        url = url.replaceAll(" ", "%20");

        try {
        	playerName = (String) HttpManager.getResponse(url, false);
        	Gson gson = new Gson();
            PlayerName name = gson.fromJson(playerName, PlayerName.class);
            
            return name.toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerName;
    }
    private void setPlayerInfo(String appId, String playerId) {
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getPlayerLoginInfo&appid=%s&plaid=%s",
                        appId, playerId);

        url = url.replaceAll(" ", "%20");

        try {
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
            Log.d("Email URL", url);
            
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.getJSONFromUrl(url, params);
            
            Log.d("Info", json.getJSONArray("playerlogininfo").toString());
            Log.d("Info", json.getJSONArray("playerlogininfo").getJSONObject(0).toString());
            JSONObject playerInfo = json.getJSONArray("playerlogininfo").getJSONObject(0);
            
            gLoginUser.setFirstName(playerInfo.getString("player_firstname"));
            gLoginUser.setLastName(playerInfo.getString("player_lastname"));
            gLoginUser.setEmail(playerInfo.getString("player_email"));
            gLoginUser.setUserName(playerInfo.getString("player_name"));
            gLoginUser.setUserImageName(playerInfo.getString("pla_image"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getPlayerImageName(String appId, String playerId) {
        String playerImageName = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getPlayerImage&appid=%s&plaid=%s",
                        appId, playerId);

        url = url.replaceAll(" ", "%20");

        try {
        	playerImageName = (String) HttpManager.getResponse(url, false);
        	Gson gson = new Gson();
        	PlayerImage image = gson.fromJson(playerImageName, PlayerImage.class);
            
            return image.toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return playerImageName;
    }
    
    private GamesDetail getGames(String gameId) {
        String appId = activity.getResources().getString(R.string.appid);
        String plaId = null;
        String result = null;

        appId = ((appId == null) ? "" : appId);
        try {
            plaId = mApplication.getPlayerID();
            plaId = ((plaId == null) ? "" : plaId);
            Log.e(TAG,"plaid : "+plaId);
            result = getGames(appId, plaId, gameId);
            Log.i("Game Result", result);
            Gson gson = new Gson();
            GamesDetail games = gson.fromJson(result, GamesDetail.class);
            // Log.e(TAG,"games : "+ games);
            // int getgameid =
            // Integer.parseInt(games.getGames().get(0).getGam_id());
             

            return games;
        } catch (Exception ex) {
            Log.e(TAG, "Exception in getGames(): " + ex.getMessage());
        }

        return null;
    }

    private String getGames(String appId, String plaId, String gameId) {
        String resPlaId = null;
        String url = String
                .format(activity.getString(R.string.game_url)+"?f=getGames&appid=%s&plaid=%s&gamid=%s",
                        appId, plaId, gameId);

        url = url.replaceAll(" ", "%20");

        try {
            resPlaId = (String) HttpManager.getResponse(url, false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resPlaId;
    }

}
