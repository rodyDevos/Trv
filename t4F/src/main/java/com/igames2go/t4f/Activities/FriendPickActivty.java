
package com.igames2go.t4f.Activities;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.view.CustomButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
//import android.widget.Toast;


public class FriendPickActivty extends AdActivity {
    public static final int REQUESTCODE_FB_FRIEND = 101;
    public static final int REQUESTCODE_FAVORITE_FRIEND = 102;
    T4FApplication mApplication;
    CustomButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickfriend);
        
        mApplication = (T4FApplication)getApplication();
        
        btn = new CustomButton(getApplicationContext());
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
    }

    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	TextView tv = (TextView) findViewById(R.id.label_choose_opponent);
    	int size = ((T4FApplication) getApplication()).getPlayer().size();
    	if(size == 0) {
    		tv.setText(R.string.label_first_opnt);
    	}
    	else {
    		tv.setText(R.string.label_choose_next_opponent_);
    	}
//    	Toast.makeText(getApplicationContext(), R.string.type, Toast.LENGTH_LONG).show();
    }
    Dialog d;
    public void favFriendClick(View v) {
        // Toast.makeText(getApplicationContext(), "favFriendClick",
        // 1000).show();
    	if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN){
    		d = DialogUtil.createDialog_title_divider_message_two_btn(this, this.getString(R.string.fb_alert_friends), true, this.getString(R.string.fb_alert_friends_body), R.drawable.button_ok, -1, new ButtonClickListener() {
				
				@Override
				public void onButtonClick(int s) throws NullPointerException {
					//session.requestNewPublishPermissions(new NewPermissionsRequest(activity, Arrays.asList(/*"user_birthday",*/ "publish_stream","email")));
					d.dismiss();
				}
			}, 4);
        	d.show();
    	}else{
	        startActivity(new Intent(this, FavoriteFriendsActivity.class));
	        FriendPickActivty.this.finish();
	        btn.performClick();
    	}
    }
    
    public void fbFriendClick(View v) {
        
    	if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN){
    		d = DialogUtil.createDialog_title_divider_message_two_btn(this, this.getString(R.string.fb_alert_friends), true, this.getString(R.string.fb_alert_friends_body), R.drawable.button_ok, -1, new ButtonClickListener() {
				
				@Override
				public void onButtonClick(int s) throws NullPointerException {
					//session.requestNewPublishPermissions(new NewPermissionsRequest(activity, Arrays.asList(/*"user_birthday",*/ "publish_stream","email")));
					d.dismiss();
				}
			}, 4);
        	d.show();
    	}else{
    		if(FacebookManager.init()){
	    		startActivity(new Intent(this, FacebookFriendsActivity.class));
	    		FriendPickActivty.this.finish();
	    		btn.performClick();
    		}else{
    			d = DialogUtil.createDialog_title_divider_message_two_btn(this, this.getString(R.string.fb_alert_friends), true, this.getString(R.string.fb_alert_friends_body), R.drawable.button_ok, -1, new ButtonClickListener() {
    				
    				@Override
    				public void onButtonClick(int s) throws NullPointerException {
    					//session.requestNewPublishPermissions(new NewPermissionsRequest(activity, Arrays.asList(/*"user_birthday",*/ "publish_stream","email")));
    					d.dismiss();
    				}
    			}, 4);
            	d.show();
    		}
    	}
        // getFriends();
    }

    public void searchFriendClick(View v) {
        // Toast.makeText(getApplicationContext(), "searchFriendClick",
        // 1000).show();
        startActivity(new Intent(this, FindAFriendActivity.class));
        FriendPickActivty.this.finish();
        btn.performClick();
    }

    public void randomFriendClick(View v) {
        // Toast.makeText(getApplicationContext(), "randomFriendClick",
        // 1000).show();
        startActivity(new Intent(this, RandomPickActivity.class));
        FriendPickActivty.this.finish();
        btn.performClick();
    }

    public void blockClicked(View v) {
        // Toast.makeText(getApplicationContext(), "friendOpt4Click",
        // 1000).show();
        startActivity(new Intent(this, BlockedFriendsActivity.class));
        btn.performClick();
    }

    public void onBack(View v) {
        finish();
    }

    /*
    private void getFriends() {
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Request friendRequest = Request.newMyFriendsRequest(activeSession,
                    new GraphUserListCallback() {
                        @Override
                        public void onCompleted(List<GraphUser> users,
                                Response response) {
                            // Log.i("INFO", response.toString());
                            String result = response.toString();
                            String checker = " responseCode: ";
                            if (!result.contains(checker)) {
                                // Log.e("napender",result);
                                Gson gson = new Gson();
                                JsonReader reader = new JsonReader(
                                        new StringReader(result));
                                reader.setLenient(true);
                                FB_Friends fbfriends = gson.fromJson(reader,
                                        FB_Friends.class);
                                for (FB_FriendsDataObject frnds : fbfriends
                                        .getFB_Friends()) {
                                    // Log.e("napender",frnds.toString());
                                }
                            } else {
                                // TODO: error occurred while getting friends
                                // data from FB
                                Log.e("ERROR",
                                        "Error occurred while getting friends data from FB and "
                                                + result);
                            }

                        }
                    });
            Bundle params = new Bundle();
            params.putString("fields", "id,name,picture");
            friendRequest.setParameters(params);
            friendRequest.executeAsync();
        }
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESTCODE_FB_FRIEND:
                handleFBFriend(resultCode, data);
                break;
            case REQUESTCODE_FAVORITE_FRIEND:
                handleFavFriend(resultCode, data);
                break;

            default:
                break;
        }
    }

    private void handleFavFriend(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String userId = data.getStringExtra("USER_ID");
//                Toast.makeText(getApplicationContext(), userId + "", 1000).show();
                break;

            default:
                break;
        }

    }

    private void handleFBFriend(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String userId = data.getStringExtra("USER_ID");
//                Toast.makeText(getApplicationContext(), userId + "", 1000).show();
                break;

            default:
                break;
        }

    }

}
