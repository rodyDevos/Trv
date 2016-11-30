
package com.igames2go.t4f.Application;

import com.facebook.FacebookSdk;

import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.data.LeaderboardContestsDataObject;
import com.igames2go.t4f.data.LoginUser;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class T4FApplication extends Application {
	public static boolean soundSettting = true;
	//public static boolean leaderboardHide = true;
	// 03/19/2015 true hides, false shows leaderboard 
	public static boolean leaderboardHide = false;
	public static boolean storeHide = true;
	
	public static final int PLAY_MODE_NO_LOGIN = 0;
	public static final int PLAY_MODE_LOGIN = 1;
	
	public static final int PROFILE_NEW = 0;
	public static final int PROFILE_UPDATE = 1;
	
	//public static final int PLAY_MODE_FB_LOGIN = 2;
	
    public HashMap<String, BitmapDrawable> map = new HashMap<String, BitmapDrawable>();
    private JSONObject gFacebookUser;
    //public FriendPickerFragment friendPickerFragment;
    private String gPlayerID = "";
    private String gPlayerImage = "";
    private String gPlayerAppID = "";
    public boolean refresh = false;
    private ArrayList<FriendsDataObject> players = new ArrayList<FriendsDataObject>();
    private ArrayList<LeaderboardContestsDataObject> contests = new ArrayList<LeaderboardContestsDataObject>();
	public String email;
	public boolean inviteFlag = false;
    public String autoturntitle="";
	public String autoturnmessage="";
	private LoginUser loginUser;
	
	private SharedPreferences pref;
	public void addPlayer(FriendsDataObject properties) {
        if (players == null)
            players = new ArrayList<FriendsDataObject>();
        if (players.size() < 3)
            players.add(properties);
    }

    // public void resetGame() {
    // players= null;
    // }

    public void setContests(ArrayList<LeaderboardContestsDataObject> contests){
    	this.contests = contests;
    }
    public ArrayList<LeaderboardContestsDataObject> getContests(){
    	return contests;
    }
    public void resetContests() {
    	contests = new ArrayList<LeaderboardContestsDataObject>();
    }
    
    public ArrayList<FriendsDataObject> getPlayer() {
        return players;
    }

    public void resetPlayer() {
        players = new ArrayList<FriendsDataObject>();
    }

    /*
    public FriendPickerFragment getFragment() {
        return friendPickerFragment;
    }
    */

    public void removePlayer(FriendsDataObject obj) {
        // if(player !=null)
        // players.remove(player);
        if (players == null || players.size() == 0)
            return;
        for (FriendsDataObject friendsDataObject : players) {
            if (obj.getPla_fb_id().equalsIgnoreCase(
                    friendsDataObject.getPla_fb_id())) {
                players.remove(friendsDataObject);
                return;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        soundSettting = getSoundSetting();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    private boolean getSoundSetting() {
    	SharedPreferences prefs =getSharedPreferences("setting",MODE_PRIVATE); 
    	return prefs.getBoolean("sound", true);
    	
    }
    
    public JSONObject getFacebookUser() {
        return gFacebookUser;
    }

    public void setFacebookUser(JSONObject gFacebookUser) {
        /*Toast.makeText(getApplicationContext(),
                "hello=" + gFacebookUser.getId(), 1000).show();*/
        this.gFacebookUser = gFacebookUser;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser gLoginUser) {
        /*Toast.makeText(getApplicationContext(),
                "hello=" + gFacebookUser.getId(), 1000).show();*/
        this.loginUser = gLoginUser;
    }
    
    public String getPlayerID() {
    	pref = getSharedPreferences("app_info", MODE_PRIVATE);
        return pref.getString("gPlayerID", "");
    }

    public void setPlayerID(String gPlayerID) {
        
        pref = getSharedPreferences("app_info", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
        editor.putString("gPlayerID", gPlayerID);
		editor.commit();
    }

    public String getPlayerAppID() {
    	pref = getSharedPreferences("app_info", MODE_PRIVATE);
        return pref.getString("gPlayerAppID", "");
    }

    public void setPlayerAppID(String gPlayerAppID) {
    	pref = getSharedPreferences("app_info", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
        editor.putString("gPlayerAppID", gPlayerAppID);
		editor.commit();
    }

    /*
    public void setFragment(FriendPickerFragment fragment) {
        friendPickerFragment = fragment;

    }
    */

    public boolean isAlreadyAdded(FriendsDataObject obj) {
        if (players == null || players.size() == 0)
            return false;
        for (FriendsDataObject friendsDataObject : players) {
            if (obj.getPla_id().equalsIgnoreCase(
                    friendsDataObject.getPla_id())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyAdded(String pid) {
        if (players == null || players.size() == 0)
            return false;
        for (FriendsDataObject friendsDataObject : players) {
            if (pid.equalsIgnoreCase(
                    friendsDataObject.getPla_fb_id())) {
                return true;
            }
        }
        return false;
    }

    public String getDeviceID()
    {
        String deviceId = null;
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(
                Context.TELEPHONY_SERVICE);
        if (tm != null)
            deviceId = tm.getDeviceId();
        if (deviceId == null || deviceId.length() == 0)
            deviceId = Secure.getString(getApplicationContext().getContentResolver(),
                    Secure.ANDROID_ID);

        return deviceId;
    }

	public String getPlayerImage() {
		return gPlayerImage;
	}

	public void setPlayerImage(String gPlayerImage) {
		this.gPlayerImage = gPlayerImage;
	}
	
	public void resetPlayerInfo(){
		gPlayerID = "";
	    gPlayerImage = "";
	    gPlayerAppID = "";
	}

	public int getPlay_mode() {
		pref = getSharedPreferences("app_info", MODE_PRIVATE);
		return pref.getInt("play_mode", PLAY_MODE_NO_LOGIN);
	}

	public void setPlay_mode(int play_mode) {
		pref = getSharedPreferences("app_info", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
        editor.putInt("play_mode", play_mode);
		editor.commit();
	}
}
