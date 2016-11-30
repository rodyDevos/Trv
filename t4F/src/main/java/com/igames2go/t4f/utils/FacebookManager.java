package com.igames2go.t4f.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FB_Friends;
import com.igames2go.t4f.data.FB_FriendsDataObject;
import com.igames2go.t4f.data.FacebookFriendsDataObject;
import com.igames2go.t4f.data.FacebookFriendsList;
import com.igames2go.t4f.listener.FBLoadListener;
import com.learn2crack.Profile;

public class FacebookManager {
	
	public static String fbId = "";
	public static String fbName = "";
	public static String fbEmail = "";
	public static FacebookFriendsList fbFriends;
	public static boolean init() {

		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null;
	}

	/*
	public static Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback, List<String> permissions) {
	    Session.OpenRequest openRequest = new Session.OpenRequest(activity)
	    										.setPermissions(permissions)
	    										.setCallback(callback)
	    										.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
	    
		//Session.OpenRequest openRequest = new Session.OpenRequest(activity).setCallback(callback);
	    Session session = new Session.Builder(activity).build();
	    if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI) {
	        Session.setActiveSession(session);
	        session.openForRead(openRequest);
	        return session;
	    }
	    return null;
	}
	*/


	public static void fbLogin(Activity activity, CallbackManager callbackManager, FacebookCallback callback, List<String> permissions){

		LoginManager.getInstance().registerCallback(callbackManager, callback);
		LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
	}

	public static void getPersonalInfo(final FBLoadListener listener){

		AccessToken accessToken = AccessToken.getCurrentAccessToken();

		if (accessToken != null){
			GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
				@Override
				public void onCompleted(JSONObject user, GraphResponse response) {
					if (user != null) {
						Log.d("Facebook", "Facebook login success");

						fbId = user.optString("id");//user id
						fbName = user.optString("name");
						fbEmail = user.optString("email");

						//loadUserInfo(facebook_id, name, email, location, birthday, gender, profile_picture);
						//listener.onFBDone();
						getFriends(listener);
					} else {
						Log.d("Facebook", "No Facebook User Info");
						listener.onFBDone();
					}
				}
			});

			request.executeAsync();
		}

		/*
		if (session.isOpened()) {
    		
			Request.newMeRequest(session, new Request.GraphUserCallback() {

			  // callback after Graph API response with user object
			  @Override
			  public void onCompleted(GraphUser user, Response response) {
				  Log.d("Facebook", response.toString());
				  if (user != null) {
					  Log.d("Facebook", "Facebook login success");
					  Log.d("Facebook", user.toString());
					  
					  fbId = user.getId();//user id
                      fbName= user.getName();
                      fbEmail = user.asMap().get("email").toString();
                      
                      //loadUserInfo(facebook_id, name, email, location, birthday, gender, profile_picture);
                      //listener.onFBDone();
                      getFriends(session, listener);
				  }else{
					  Log.d("Facebook", "No Facebook User Info");
					  listener.onFBDone();
				  }
			  }
			}).executeAsync();
			
    	}else{
    		
    	}
    	*/
	}
	
	public static void getFriends(final FBLoadListener listener){

		AccessToken accessToken = AccessToken.getCurrentAccessToken();

		if (accessToken != null){
			GraphRequest friendRequest = GraphRequest.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {

				@Override
				public void onCompleted(JSONArray array, GraphResponse response) {

					List<JSONObject> friends = new ArrayList<JSONObject>();
					for (int i = 0; i < array.length(); i++) {
						try {
							friends.add(array.getJSONObject(i));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					Log.i("INFO", friends.toString());

					class FriendComparator implements Comparator<JSONObject> {
						@Override
						public int compare(JSONObject user1, JSONObject user2) {
							return user1.optString("name").compareTo(user2.optString("name"));
						}
					}

					Collections.sort(friends, new FriendComparator());

					fbFriends = new FacebookFriendsList();
					List<FacebookFriendsDataObject> data = new ArrayList<FacebookFriendsDataObject>();
					if (friends != null) {
						for (JSONObject friend : friends) {
							FacebookFriendsDataObject fbObj = new FacebookFriendsDataObject();
							fbObj.setId(friend.optString("id"));
							fbObj.setName(friend.optString("name"));
							data.add(fbObj);
						}
					}
					fbFriends.setFacebookFriends(data);
					listener.onFBDone();
				}
			});

			Bundle params = new Bundle();
			params.putString("fields", "id,name,picture");
			params.putString("limit", "5000");
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
		/*
		if (session.isOpened()) {
    		
			Request friendRequest = Request.newMyFriendsRequest(session, new GraphUserListCallback() {
                @Override
                public void onCompleted(List<GraphUser> users, Response response) {
                    Log.i("INFO", response.toString());

                    class FriendComparator implements Comparator<GraphUser> {
                        @Override
                        public int compare(GraphUser user1, GraphUser user2) {
                            return user1.getName().compareTo(user2.getName());
                        }
                    }
                    
                    Collections.sort(users, new FriendComparator());
                    Log.d("Friends=", users.toString());
                	fbFriends = new FacebookFriendsList();
                	List<FacebookFriendsDataObject> data = new ArrayList<FacebookFriendsDataObject>();
                	if(users != null){
	                	for(GraphUser user: users){
	                		FacebookFriendsDataObject fbObj = new FacebookFriendsDataObject();
	                		fbObj.setId(user.getId());
	                		fbObj.setName(user.getName());
	                		data.add(fbObj);
	                	}
                	}
                	fbFriends.setFacebookFriends(data);
                	listener.onFBDone();
                }
            });
		*/
	}
	
	public static void fbLogout(Context context) {

		LoginManager.getInstance().logOut();
		/*
	    Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(context);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	    }
	    */

	    fbId = "";
	    fbName = "";
	    fbEmail = "";
	}

	public static void fbShare(String title, String url, String imageUrl, String description, Activity activity, CallbackManager callbackManager, FacebookCallback<Sharer.Result> callback){

		ShareDialog shareDialog = new ShareDialog(activity);
		shareDialog.registerCallback(callbackManager, callback);

		if (ShareDialog.canShow(ShareLinkContent.class)){
			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentUrl(Uri.parse(url))
					.setContentTitle(title)
					.setImageUrl(Uri.parse(imageUrl))
					.setContentDescription(description)
					.build();

			shareDialog.show(linkContent);
		}

	}
	public static void fbInvite(String appLinkUrl, String previewImageUrl, Activity activity, CallbackManager callbackManager, FacebookCallback<AppInviteDialog.Result> callback){

		AppInviteDialog inviteDialog = new AppInviteDialog(activity);
		inviteDialog.registerCallback(callbackManager, callback);

		if (AppInviteDialog.canShow()) {
			AppInviteContent content = new AppInviteContent.Builder()
					.setApplinkUrl(appLinkUrl)
					.setPreviewImageUrl(previewImageUrl)
					.build();
			inviteDialog.show(content);
		}
	}
}
