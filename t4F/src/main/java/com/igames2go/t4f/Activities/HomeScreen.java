
package com.igames2go.t4f.Activities;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.CallbackManager;

import com.facebook.login.LoginResult;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.GameAlertPOJO;
import com.igames2go.t4f.data.Gamealert;
import com.igames2go.t4f.data.GamesDataObject;
import com.igames2go.t4f.data.GamesDetail;
import com.igames2go.t4f.listener.FBLoadListener;
import com.igames2go.t4f.tasks.GcmRegistration;
import com.igames2go.t4f.tasks.LoadGameAlert;
import com.igames2go.t4f.tasks.LoadNewGameAlert;
import com.igames2go.t4f.tasks.LoadPlayerId;
import com.igames2go.t4f.tasks.LoadingHomeScreen;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DatabaseHandler;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.utils.ShowDialog;
import com.igames2go.t4f.view.CustomButton;
import com.igames2go.t4f.view.CustomImageButton;
import com.igames2go.t4f.view.CustomTextView;
import com.igames2go.t4f.view.SlideMenu;
import com.learn2crack.Profile;

public class HomeScreen extends AdActivity {

    private Context context;
    public  static final String TAG = "T4F";
    public  static final String GAME_ACTIVE = "ACTIVE";
    public  static final String GAME_ENDED = "ENDED";
    public  static final String GAME_ENDED_SOLO = "ENDED_SOLO";
    public  static final String GAME_1SOLO = "1SOLO";
    public  static final String GAME_PS_WTET = "WAITING TO END TURN";
    public  static final String GAME_PS_WFT = "WAITING FOR TURN";
    public  static final String GAME_PS_ENDED = "ENDED";
    public  static final String GAME_PS_WON = "WON";
    public static final String GAME_PS_DROPPED = "DROPPED";
    private ImageLoader loader;
    private GamesDataObject localViewGameObj = null;

    private static final int PLAYBYPLAY_ACTIVITY = 2;
    private static final int SCOREBOARD_ACTIVITY = 3;
    
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    private AtomicInteger msgId = new AtomicInteger();
    private SharedPreferences prefs;
    private static String regid;
    private T4FApplication app;
    private SlideMenu slidemenu;
    
    CustomImageButton fbLoginButton;
    CustomButton leaderboardButton;

    CallbackManager callbackManager;

    private static Dialog dialog;

    boolean isInit;
    public interface LoadingCompleteListener {
        public void onComplete(GamesDetail detail);
        public void onComplete(String str);
        public void onError(Object error);
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homescreen);
        app = (T4FApplication) getApplication();

        callbackManager = CallbackManager.Factory.create();

        isInit = true;
        v=((ImageView)findViewById(R.id.iv_pic));
    	
        loader = new ImageLoader(this);
        context = this.getApplicationContext();
        
        slidemenu = new SlideMenu(this);
        slidemenu.checkEnabled();
        
        leaderboardButton = (CustomButton) findViewById(R.id.leaderboard);
        fbLoginButton = (CustomImageButton) findViewById(R.id.fbLoginButton);
        
        if(T4FApplication.leaderboardHide || app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN)
        	leaderboardButton.setVisibility(View.GONE);
        
        if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN)
        	fbLoginButton.setVisibility(View.GONE);
        else{
        	if(FacebookManager.init())
        		fbLoginButton.setBackgroundResource(R.drawable.button_fb_home_logout);
        	else
        		fbLoginButton.setBackgroundResource(R.drawable.button_fb_home_login);
        	
        	fbLoginButton.setVisibility(View.VISIBLE);
        }
        
        fbLoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fbButtonPressed();
			}
    	});
        
     // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            regid = getRegistrationId(context);
            
            if (regid == null || regid.equals("")) {
                registerforGCMInBackground();
            }
            else
            {
                init(regid);
            }
        } else {
            Toast.makeText(this, "No valid Google Play Services APK found.",Toast.LENGTH_LONG).show();
        }

    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
     super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
     super.onRestoreInstanceState(savedInstanceState);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            ShowDialog.showLoadingDialog(HomeScreen.this, "Loading Facebook Info...");
            FacebookManager.getPersonalInfo(fbListener);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    private FacebookCallback<AppInviteDialog.Result> callback1 = new FacebookCallback<AppInviteDialog.Result>() {
        @Override
        public void onSuccess(AppInviteDialog.Result result) {

            dialog = DialogUtil.createDialog_title_divider_message_two_btn(HomeScreen.this,
                    getString(R.string.your_friends_have_been_invited),
                    true,
                    "",
                    -1,
                    R.drawable.button_ok,
                    new ButtonClickListener() {
                        @Override
                        public void onButtonClick(int s) throws NullPointerException {
                            dialog.dismiss();
                        }
                    },
                    -1);

            dialog.show();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };
    
    FBLoadListener fbListener = new FBLoadListener() {
    	public void onFBLogin() {};
    	public void onFBLogout() {
    		
    	};
    	public void onFBDone() {
    		
    		LoadPlayerId task = new LoadPlayerId(HomeScreen.this, new LoadingCompleteListener() {
				
				@Override
				public void onError(Object error) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onComplete(String str) {
					// TODO Auto-generated method stub
					String plaid = app.getLoginUser().getUserId();
					Log.d("Current Player Id", plaid);
					Log.d("FB Player Id", str);
					fbLoginButton.setBackgroundResource(R.drawable.button_fb_home_logout);
				}
				
				@Override
				public void onComplete(GamesDetail detail) {
					// TODO Auto-generated method stub
					
				}
			});
    		task.execute();
    	};
    };
    
    ImageView v;
    
    private void init(String regId)
    {
    	LoadingHomeScreen asynTask = new LoadingHomeScreen(this, mlistener,false, regId);
        asynTask.execute();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(getApplicationContext(), R.string.type, Toast.LENGTH_LONG).show();
//        FlurryAgent.onStartSession(this, "V4BVC6SCNNYBQ32W8R47");
        
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	
    	Log.d("T4F", "Restart");
    	app.resetPlayer();
    	/*
    	if(app.refresh) {
    		app.refresh = false;
    		refresh(findViewById(R.id.refresh));
    	}
    	*/
    	if(app.inviteFlag) {
    		invite();
    		app.inviteFlag = false;
    	}
    	//if(app.play_mode == T4FApplication.PLAY_MODE_NO_LOGIN){
    		if(TextUtils.isEmpty(app.autoturnmessage)==false) {
    			showAutoturnMessage(app.autoturntitle, app.autoturnmessage);
    			app.autoturnmessage = "";
    			app.autoturntitle = "";
    		}
    	//}
    }
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	Log.d("T4F", "Resume");
    	if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN)
        	fbLoginButton.setVisibility(View.GONE);
        else{
        	if(FacebookManager.init())
        		fbLoginButton.setBackgroundResource(R.drawable.button_fb_home_logout);
        	else
        		fbLoginButton.setBackgroundResource(R.drawable.button_fb_home_login);
        	
        	fbLoginButton.setVisibility(View.VISIBLE);
        }
    	
    	/*
    	if(app.refresh) {
    		app.refresh = false;
    		refresh(findViewById(R.id.refresh));
    	}
    	*/
    	
    	if(!isInit)
    		refresh(findViewById(R.id.refresh));
//    	checkPlayServices();
    }
    private Map<String, ArrayList<GamesDataObject>> map;
    protected OnClickListener playNowListener = new OnClickListener() {
    	@Override
      public void onClick(View v) {
    	final GamesDataObject obj = (GamesDataObject) v.getTag();
    	LoadGameAlert alert = new LoadGameAlert(HomeScreen.this, obj.getGam_id()+"", true, new LoadingListener() {
			
			@Override
			public void onLoadingComplete() {
			}
			
			@Override
			public void onLoadingComplete(Object obj1) {
				if(obj1 != null){
					if(obj1 instanceof GameAlertPOJO){
						Gamealert alertObj = ((GameAlertPOJO)obj1).getGamealert();
						if(alertObj.getAlerttitle().length() == 0){
							Intent intent = new Intent(getApplicationContext(),
				                    GameOptionsActivity.class);
				            intent.putExtra("game_id", obj.getGam_id() + "");
				            //intent.putExtra("game_status", obj.getGam_status());
				            intent.putExtra("gameplaid", obj.getGampla_id() + "");
				            intent.putExtra("lifeline", obj.getGampla_lifelines() + "");
				            startActivity(intent);
				            return;
						}
						String image = "";
						if(alertObj.getAlertimage().equals("") == false)
							image = "http://www.trivia4friends.com/t4fapi/images/"+alertObj.getAlertimage();
						DialogUtil.createDialog_title_bitmap_message_2_button(HomeScreen.this, alertObj.getAlerttitle(), 
								image, alertObj.getAlerttext(), R.drawable.button_ok, 
								(alertObj.getAlertoption().trim().length()==0 ? -1:R.drawable.button_decline), obj, 1, null);
					}
				}
			}
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
		});
    	alert.execute();
//        
//            GamesDataObject obj = (GamesDataObject) v.getTag();
//            Intent intent = new Intent(getApplicationContext(),
//                    GameOptionsActivity.class);
//            intent.putExtra("game_id", obj.getGam_id() + "");
//            intent.putExtra("gameplaid", obj.getGampla_id() + "");
//            intent.putExtra("lifeline", obj.getGampla_lifelines() + "");
//            startActivity(intent);
        }
    };

    protected OnClickListener viewGameListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        	final GamesDataObject obj = (GamesDataObject) v.getTag();
        	LoadGameAlert alert = new LoadGameAlert(HomeScreen.this, obj.getGam_id()+"", true, new LoadingListener() {
				
				@Override
				public void onLoadingComplete() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(Object obj1) {
					if(obj1 != null){
						if(obj1 instanceof GameAlertPOJO){
							Gamealert alertObj = ((GameAlertPOJO)obj1).getGamealert();
							if(alertObj.getAlerttitle().length()==0){
					            localViewGameObj = obj;
					            Intent intent = new Intent(getApplicationContext(),
					                    ScoreboardActivity.class);
					            intent.putExtra("game_id", obj.getGam_id() + "");
					            intent.putExtra("game_pla_id", obj.getGampla_id() + "");
					            if ((obj.getGampla_status()).equals(GAME_PS_DROPPED)
					                    || (obj.getGampla_status()).equals(GAME_ENDED) || (obj.getGampla_status()).equals(GAME_PS_WON))
					                intent.putExtra("resigned", "1");
					            else
					                intent.putExtra("resigned", "0");
					
					            startActivityForResult(intent, SCOREBOARD_ACTIVITY);
								return;
							}
							String image = "";
							if(alertObj.getAlertimage().equals("") == false)
							 image = "http://www.trivia4friends.com/t4fapi/images/"+alertObj.getAlertimage();
							DialogUtil.createDialog_title_bitmap_message_2_button(HomeScreen.this, alertObj.getAlerttitle(), 
									image, alertObj.getAlerttext(), R.drawable.button_ok, 
									(alertObj.getAlertoption().trim().length()==0 ? -1:R.drawable.button_decline), obj, 2, null);
						}
					}
					
				}
				
				@Override
				public void onError(Object error) {
					// TODO Auto-generated method stub
					
				}
			});
        	alert.execute();
//        	getAlertInfo(v); 
//            GamesDataObject obj = (GamesDataObject) v.getTag();
//            localViewGameObj = obj;
//            Intent intent = new Intent(getApplicationContext(),
//                    ScoreboardActivity.class);
//            intent.putExtra("game_id", obj.getGam_id() + "");
//            intent.putExtra("game_pla_id", obj.getGampla_id() + "");
//            if ((obj.getGampla_status()).equals(GAME_PS_DROPPED)
//                    || (obj.getGampla_status()).equals(GAME_ENDED) || (obj.getGampla_status()).equals(GAME_PS_WON))
//                intent.putExtra("resigned", "1");
//            else
//                intent.putExtra("resigned", "0");
//
//            startActivityForResult(intent, SCOREBOARD_ACTIVITY);
        }
    };

    public void getAlertInfo(View v) {
    	
    }
    private void showPlaybyPlay() {
        if (localViewGameObj != null) {
            Intent intent = new Intent(this, PlaybyPlayActivity.class);
            intent.putExtra("game_id", localViewGameObj.getGam_id() + "");
            intent.putExtra("game_pla_id", localViewGameObj.getGampla_id() + "");
            intent.putExtra("quesId", "0" + "");
            if ((localViewGameObj.getGampla_status()).equals(GAME_PS_DROPPED)
                    || (localViewGameObj.getGampla_status()).equals(GAME_ENDED))
                intent.putExtra("resigned", "1");
            else
                intent.putExtra("resigned", "0");

            startActivityForResult(intent, PLAYBYPLAY_ACTIVITY);
        } else
            Toast.makeText(this, "something is wrong...", Toast.LENGTH_LONG)
                    .show();
    }

    private void showScoreBoard() {
        if (localViewGameObj != null) {
            Intent intent = new Intent(getApplicationContext(),
                    ScoreboardActivity.class);
            intent.putExtra("game_id", localViewGameObj.getGam_id() + "");
            intent.putExtra("game_pla_id", localViewGameObj.getGampla_id() + "");
            if ((localViewGameObj.getGampla_status()).equals(GAME_PS_DROPPED)
                    || (localViewGameObj.getGampla_status()).equals(GAME_ENDED))
                intent.putExtra("resigned", "1");
            else
                intent.putExtra("resigned", "0");

            startActivityForResult(intent, SCOREBOARD_ACTIVITY);
        } else
            Toast.makeText(this, "something is wrong...", Toast.LENGTH_LONG)
                    .show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    	callbackManager.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PLAYBYPLAY_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().containsKey("hasResigned")) {
                        String result = data.getStringExtra("hasResigned");
                        if (result.equals("1")) {
                            LoadingHomeScreen asynTask = new LoadingHomeScreen(
                                    this, mlistener, true, regid);
                            asynTask.execute();
                        }
                    } else if (data.getExtras().containsKey("openScoreBoard")) {
                        String result = data.getStringExtra("openScoreBoard");
                        if (result.equals("1")) {
                            localViewGameObj.setGampla_status(GAME_PS_DROPPED);
                            showScoreBoard();
                        } else if (result.equals("2")) {
                            showScoreBoard();
                        }
                    }
                }
                break;

            case SCOREBOARD_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().containsKey("hasResigned")) {
                        String result = data.getStringExtra("hasResigned");
                        if (result.equals("1")) {
                            LoadingHomeScreen asynTask = new LoadingHomeScreen(
                                    this, mlistener, true, regid);
                            asynTask.execute();
                        }
                    } else if (data.getExtras().containsKey("openPlayByPlay")) {
                        String result = data.getStringExtra("openPlayByPlay");
                        if (result.equals("1")) {
                            localViewGameObj.setGampla_status(GAME_PS_DROPPED);
                            showPlaybyPlay();
                        } else if (result.equals("2")) {
                            showPlaybyPlay();
                        }
                    }
                }
                break;
        }
    }

    private HorizontalListView gv;
//    private SoundPool mShortPlayer;
//    private int soundID;
    private BaseAdapter adapter = new BaseAdapter() {
    	int substractionconstant = 1;
        @Override
        public int getCount() {
        	if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN)
        		return gameIDList.size();
        	else if(app.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN)
        		return gameIDList.size() + 1;
			return gameIDList.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//        	if(T4FApplication.soundSettting)
//            	mShortPlayer.play(soundID, 0.99f, 0.99f, 0, 0, 1);
        	if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN)
        		substractionconstant = 0;
        	else if(app.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN)
        		substractionconstant = 1;
        	
        	int[] ids = {
                    R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4
            };
            if (convertView == null) {
                convertView = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.game_item, null);
            }
            if(app.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN){
            	if (position == 0) {
	                convertView.findViewById(R.id.tv_turn).setVisibility(
	                        View.INVISIBLE);
	                convertView.findViewById(R.id.tv_gameinfo1).setVisibility(
	                        View.INVISIBLE);
	                convertView.findViewById(R.id.tv_gameinfo2).setVisibility(
	                        View.INVISIBLE);
	                convertView.findViewById(R.id.btn_play_now).setVisibility(
	                        View.INVISIBLE);
	                convertView.findViewById(R.id.tl_profile).setVisibility(
	                        View.GONE);
	                convertView.findViewById(R.id.ll_game).setBackgroundResource(
	                        R.drawable.activegame_background_startnew_plain);
	                convertView.findViewById(R.id.ll_game).setOnClickListener(
	                        new OnClickListener() {
	
	                            @Override
	                            public void onClick(View v) {
	                                newGame(v);
	                            }
	                        });
	                return convertView;
            	}else{
            		convertView.findViewById(R.id.tv_turn).setVisibility(
                            View.VISIBLE);
                    convertView.findViewById(R.id.tv_gameinfo1).setVisibility(
                            View.VISIBLE);
                    convertView.findViewById(R.id.tv_gameinfo2).setVisibility(
                            View.VISIBLE);
                    convertView.findViewById(R.id.btn_play_now).setVisibility(
                            View.VISIBLE);
                    convertView.findViewById(R.id.tl_profile).setVisibility(
                            View.VISIBLE);
                    
                    convertView.findViewById(R.id.ll_game).setBackgroundResource(
                            R.drawable.activegame_background_plain);
                    convertView.findViewById(R.id.ll_game).setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                }
                            });
            	}
                
            } else {
                convertView.findViewById(R.id.tv_turn).setVisibility(
                        View.VISIBLE);
                convertView.findViewById(R.id.tv_gameinfo1).setVisibility(
                        View.VISIBLE);
                convertView.findViewById(R.id.tv_gameinfo2).setVisibility(
                        View.VISIBLE);
                convertView.findViewById(R.id.btn_play_now).setVisibility(
                        View.VISIBLE);
                convertView.findViewById(R.id.tl_profile).setVisibility(
                        View.VISIBLE);
                convertView.findViewById(R.id.ll_game).setBackgroundResource(
                        R.drawable.activegame_background_plain);
                convertView.findViewById(R.id.ll_game).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                            }
                        });

            }
            for (int i : ids) {
                convertView.findViewById(i).setVisibility(View.INVISIBLE);
                ((ImageView) convertView.findViewById(i).findViewById(
                        R.id.iv_pic))
                        .setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                ((ImageView) convertView.findViewById(i).findViewById(
                        R.id.iv_award)).setImageResource(R.drawable.trans);
                ((ImageView) convertView.findViewById(i).findViewById(
                        R.id.iv_ribbon)).setImageResource(R.drawable.trans);
                convertView.findViewById(i).findViewById(R.id.player_no_more)
                        .setVisibility(View.INVISIBLE);
            }
            int i = 0;
            ImageView playNow = (ImageView) convertView
                    .findViewById(R.id.btn_play_now);
            CustomTextView turnDetail = (CustomTextView) convertView
                    .findViewById(R.id.tv_turn);
            LinearLayout imageLL;
            if(gameIDList.size()== 0)
            	return new View(getApplicationContext());
            for (GamesDataObject obj : map.get(gameIDList.get(position - substractionconstant))) {
                
                System.out.println(obj.getGam_status());
                String fbid = "";
                String pid = "";
                boolean isMe = false;
                
                /*
                if(app.play_mode == T4FApplication.PLAY_MODE_FB_LOGIN) {
                	fbid = app.getFacebookUser().getId();
                	
                	if (obj.getPla_fb_id().equals(fbid) ) {
                        playNow.setTag(obj);
                        isMe = true;
                    }else{
                    	isMe = false;
                    }
                }else
                */ 
                if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN){
                	if(obj.getPla_fb_name().equalsIgnoreCase("you")) {
                		playNow.setTag(obj);
                		isMe = true;
                	}else{
                		isMe = false;
                	}
                }else if(app.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN){
                	pid = app.getLoginUser().getUserId();
                	Log.d("pID1", pid);
                	Log.d("pID2", obj.getPlaid());
                	if(obj.getPlaid().equals(pid)) {
                		playNow.setTag(obj);
                		isMe = true;
                	}else{
                		isMe = false;
                	}
                }
                
                if (obj.getGam_status().equals(GAME_ACTIVE)) {
                    System.out.println("active");

                    if (isMe) {
//                          playNow.setTag(obj);
                          if (obj.getGampla_status().equals(GAME_PS_WTET)) {

                              playNow.setImageResource(R.drawable.i_button_activegame_play_now);
                              turnDetail.setText("");
                              turnDetail
                                      .setBackgroundResource(R.drawable.i_button_activegame_your_turn);
                              playNow.setOnClickListener(playNowListener);
                          } else if (obj.getGampla_status().equals(GAME_PS_WFT)) {
                              playNow.setImageResource(R.drawable.i_button_activegame_view_game2);
                              playNow.setOnClickListener(viewGameListener);
                          } else if (obj.getGampla_status().equals(GAME_PS_ENDED)) {
                              playNow.setImageResource(R.drawable.i_button_activegame_get_update);
                              playNow.setOnClickListener(viewGameListener);
                          } else if (obj.getGampla_status().equals(GAME_PS_WON)) {
                              playNow.setImageResource(R.drawable.i_button_activegame_view_game2);
                              playNow.setOnClickListener(viewGameListener);
                          } else if (obj.getGampla_status().equals(
                                  GAME_PS_DROPPED)) {
                              playNow.setImageResource(R.drawable.i_button_activegame_get_update);
                              playNow.setOnClickListener(viewGameListener);
                          }

                    } else {
                          if (obj.getGampla_status().equals(GAME_PS_WTET)) {
                              String name = obj.getPla_fb_name()+" ";
//                              if (name.length() > 3)
                              name = name.substring(0, name.indexOf(" "));
//                              turnDetail.setTextSize(11);
                              turnDetail.setText(name + "'s Turn");
                              turnDetail
                                      .setBackgroundResource(R.drawable.i_button_activegame_not_your_turn);
                          //    playNow.setTag(obj);
  							  playNow.setImageResource(R.drawable.i_button_activegame_view_game2);
                              playNow.setOnClickListener(viewGameListener);
                          }
                    }
                } else if (obj.getGam_status().equals(GAME_ENDED)) {
                    System.out.println("ended");
                    playNow.setImageResource(R.drawable.i_button_activegame_view_game4);
                    playNow.setOnClickListener(viewGameListener);
                    //playNow.setTag(obj);
                    if (obj.getPla_fb_id().equals(fbid)) {
                        if (obj.getGampla_status().equals(GAME_PS_WON)) {
                            // playNow.setImageResource(R.drawable.i_button_activegame_you_won);
                            turnDetail.setText("You Won!");
                            turnDetail
                                    .setBackgroundResource(R.drawable.i_button_activegame_ended);
                        }

                    } else {
                        if (obj.getGampla_status().equals(GAME_PS_WON)) {
                            // playNow.setImageResource(R.drawable.i_button_activegame_you_won);
                        	 String name = obj.getPla_fb_name()+" ";
                           name = name.substring(0, name.indexOf(" "));
                        	turnDetail.setText(name + " Won");
                            turnDetail
                                    .setBackgroundResource(R.drawable.i_button_activegame_ended);
                        }
                    }

                } else if(obj.getGam_status().equals(GAME_1SOLO)) {
                	System.out.println("1Solo");
                	convertView.findViewById(R.id.ll_game).setBackgroundResource(
                            R.drawable.activegame_background_plain_solo);
                	if (isMe) {
//                      playNow.setTag(obj);
	                      if (obj.getGampla_status().equals(GAME_PS_WTET)) {
	
	                          playNow.setImageResource(R.drawable.i_button_activegame_play_now);
	                          turnDetail.setText("");
	                          turnDetail
	                                  .setBackgroundResource(R.drawable.i_button_activegame_for_fun);
	                          playNow.setOnClickListener(playNowListener);
	                      } else if (obj.getGampla_status().equals(GAME_PS_WFT)) {
	                          playNow.setImageResource(R.drawable.i_button_activegame_view_game2);
	                          playNow.setOnClickListener(viewGameListener);
	                      } else if (obj.getGampla_status().equals(GAME_PS_ENDED)) {
	                          playNow.setImageResource(R.drawable.i_button_activegame_get_update);
	                          playNow.setOnClickListener(viewGameListener);
	                      } else if (obj.getGampla_status().equals(GAME_PS_WON)) {
	                          playNow.setImageResource(R.drawable.i_button_activegame_view_game2);
	                          playNow.setOnClickListener(viewGameListener);
	                      } else if (obj.getGampla_status().equals(
	                              GAME_PS_DROPPED)) {
	                          playNow.setImageResource(R.drawable.i_button_activegame_get_update);
	                          playNow.setOnClickListener(viewGameListener);
	                      }
	
	                } else {
	                      if (obj.getGampla_status().equals(GAME_PS_WTET)) {
	                          String name = obj.getPla_fb_name()+" ";
	//                          if (name.length() > 3)
	                          name = name.substring(0, name.indexOf(" "));
	//                          turnDetail.setTextSize(11);
	                          turnDetail.setText(name + "'s Turn");
	                          turnDetail
	                                  .setBackgroundResource(R.drawable.i_button_activegame_not_your_turn);
	                      //    playNow.setTag(obj);
								  playNow.setImageResource(R.drawable.i_button_activegame_view_game2);
	                          playNow.setOnClickListener(viewGameListener);
	                      }
	                }
                	//turnDetail.setVisibility(View.GONE);
                } else if(obj.getGam_status().equals(GAME_ENDED_SOLO)) {
                	convertView.findViewById(R.id.ll_game).setBackgroundResource(
                            R.drawable.activegame_background_plain_solo);
                	
                	System.out.println("Ended Solo");
                    playNow.setImageResource(R.drawable.i_button_activegame_view_game4);
                    playNow.setOnClickListener(viewGameListener);
                    //playNow.setTag(obj);
                    if (obj.getPla_fb_id().equals(fbid)) {
                        if (obj.getGampla_status().equals(GAME_PS_WON)) {
                            // playNow.setImageResource(R.drawable.i_button_activegame_you_won);
                            turnDetail.setText("You Won!");
                            turnDetail
                                    .setBackgroundResource(R.drawable.i_button_activegame_ended);
                        }

                    } else {
                        if (obj.getGampla_status().equals(GAME_PS_WON)) {
                            // playNow.setImageResource(R.drawable.i_button_activegame_you_won);
                        	 String name = obj.getPla_fb_name()+" ";
                           name = name.substring(0, name.indexOf(" "));
                        	turnDetail.setText(name + " Won");
                            turnDetail
                                    .setBackgroundResource(R.drawable.i_button_activegame_ended);
                        }
                    }
                    //turnDetail.setVisibility(View.GONE);
                }
                imageLL = (LinearLayout) convertView.findViewById(ids[i]);
                imageLL.setVisibility(View.VISIBLE);
                setImage(imageLL, obj);

                if (i == 0) {
                    TextView tv_gameinfo1 = (TextView) convertView
                            .findViewById(R.id.tv_gameinfo1);
                    TextView tv_gameinfo2 = (TextView) convertView
                            .findViewById(R.id.tv_gameinfo2);
                    if (tv_gameinfo1 != null)
                        tv_gameinfo1.setText(obj.getGam_info1());
                    if (tv_gameinfo2 != null)
                        tv_gameinfo2.setText(obj.getGam_info2());
                }

                i++;
            }
            if(position == (getCount()-1)) {
            	ViewGroup.LayoutParams param =   convertView.getLayoutParams();
            	if(param != null)
            		param.width+=160;
            }
            return convertView;
        }
    };

    ArrayList<String> gameIDList = new ArrayList<String>();

    public LoadingCompleteListener mlistener = new LoadingCompleteListener() {

        @Override
        public void onComplete(GamesDetail details) {
        	
        	CustomTextView tv = (CustomTextView) findViewById(R.id.name);
        	
        	if(app.getPlay_mode()==T4FApplication.PLAY_MODE_LOGIN){

            	tv.setText(app.getLoginUser().getUserName());
            	Log.d("Player Name:", app.getLoginUser().getUserName());
            	Log.d("Player Image:", app.getPlayerImage());
            	loader.displayPlayerImage(app.getPlayerImage(), v, false);
            	ImageLoader.readMap(getApplicationContext());
            }else{
            	
        		tv.setText("You");
            	findViewById(R.id.invite).setVisibility(View.INVISIBLE);
            	loader.displayPlayerImage("fake_you.png", v, false);
            	ImageLoader.readMap(getApplicationContext());
            }
        	
            map = new HashMap<String, ArrayList<GamesDataObject>>();
            gameIDList = new ArrayList<String>();
            ArrayList<GamesDataObject> list = null;
            
            if (details == null) {
                return;
            }
            for (GamesDataObject obj : details.getGames()) {
            	if(obj.getGam_id().equals("0"))
            		continue;
                if (!map.containsKey(obj.getGam_id())) {
                    list = new ArrayList<GamesDataObject>();
                    list.add(obj);
                    gameIDList.add(obj.getGam_id());
                    map.put(obj.getGam_id(), list);
                } else {
                    map.get(obj.getGam_id()).add(obj);
                }
            }

            gv = (HorizontalListView) findViewById(R.id.gv_games);
            if(list == null || list.size() == 0 || (app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN && list.size()==1))
        	{
        		 DisplayMetrics displaymetrics = new DisplayMetrics();
        	        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        	        int height = displaymetrics.heightPixels;
        	        int width = (int) (displaymetrics.widthPixels*.25);
        	        findViewById(R.id.ll_hsv_parent).setPadding(width, gv.getPaddingTop(), gv.getPaddingRight(), gv.getPaddingBottom());
        	}
            else
            {
            	findViewById(R.id.ll_hsv_parent).setPadding(5,2,5,2);
            }
            gv.setAdapter(adapter);
            Log.i("328462874872468723", map.size() + 	"");
			
            isInit = false;
        }

        @Override
        public void onError(Object error) {

        }

		@Override
		public void onComplete(String str) {
			// TODO Auto-generated method stub
			
		}
    };

    private void setImage(LinearLayout v, final GamesDataObject obj) {
    	final ImageView pic = (ImageView) v.findViewById(R.id.iv_pic);
    	final ImageView ribbon = (ImageView) v.findViewById(R.id.iv_ribbon);
    	v.findViewById(R.id.iv_award).setVisibility(View.GONE);
    	final TextView tv_count = (TextView)v.findViewById(R.id.tv_award);
    	final ImageView player_no_more = (ImageView) v
    			.findViewById(R.id.player_no_more);
    	TextView tv = (TextView) v.findViewById(R.id.tv_name);
    	tv.setTextColor(Color.BLACK);
//    	tv.setTextSize(9);
    	tv.setText(obj.getPla_fb_name());
    	tv.setVisibility(View.VISIBLE);
    	if ((obj.getPlace()).equals("1"))
    		ribbon.setBackgroundResource(R.drawable.ribbon_blue);
    	else if ((obj.getPlace()).equals("2"))
    		ribbon.setBackgroundResource(R.drawable.ribbon_red);
    	else if (obj.getPlace().equals("3"))
    		ribbon.setBackgroundResource(R.drawable.ribbon_yellow);
    	else 
    		ribbon.setBackgroundResource(R.drawable.trans);
    	
    	if(obj.getGampla_point_tot().equals("0"))
    		ribbon.setBackgroundResource(R.drawable.trans);
    		
    	if((obj.getGampla_status()).equals("ENDED")) {
    		player_no_more.setVisibility(View.VISIBLE);
    		player_no_more.setBackgroundResource(R.drawable.icon_no_more_play);
    	}
    	else if((obj.getGampla_status())
    			.equals("DROPPED")) {
    		player_no_more.setVisibility(View.VISIBLE);
    		player_no_more.setBackgroundResource(R.drawable.icon_resigned);
    	} else
    		player_no_more
    		.setVisibility(View.INVISIBLE);
    	    	
    	loader.displayPlayerImage(obj.getPla_fb_id(), pic, false);
    	
//    	if(!TextUtils.isEmpty(obj.getAward_badge_image()))
//    		loader.DisplayImage(
//    			getString(R.string.url_images) + obj.getAward_badge_image(),
//    			award_badge, false);
    	tv_count.setVisibility(View.INVISIBLE);
    	if(TextUtils.isEmpty(obj.getGampla_point_tot())==false)
    	{
    		tv_count.setVisibility(View.VISIBLE);
    		tv_count.setText(obj.getGampla_point_tot());
    		tv_count.setTextColor(Color.WHITE);
    		//correctWidth(tv_count, 15);
    	}
    	player_no_more.bringToFront();

    }
    
    public void correctWidth(TextView textView, int desiredWidth)
    {
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(textView.getTypeface());
        float textSize = textView.getTextSize();
        paint.setTextSize(textSize);
        String text = textView.getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }
    class ProfileImageDownloader extends AsyncTask<String, Void, Bitmap> {
    	  ImageView bmImage;

    	  public ProfileImageDownloader(ImageView bmImage) {
    	      this.bmImage = bmImage;
    	  }

    	  protected Bitmap doInBackground(String... urls) {
    	      String url = urls[0];
    	      
    	      URL fbAvatarUrl = null;
              Bitmap fbAvatarBitmap = null;
              
              try {
                  fbAvatarUrl = new URL(url);
                  fbAvatarBitmap = BitmapFactory.decodeStream(fbAvatarUrl.openConnection().getInputStream());
              } catch (Exception e) {
                  e.printStackTrace();
              }
              
              return fbAvatarBitmap;
    	  }

    	  protected void onPostExecute(Bitmap result) {
    	      bmImage.setImageBitmap(result);
    	  }
    	}

    
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        try {
			int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			if (resultCode != ConnectionResult.SUCCESS) {
			    if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
			        GooglePlayServicesUtil.getErrorDialog(resultCode, this,
			                PLAY_SERVICES_RESOLUTION_REQUEST).show();
			    } else {
			        Toast.makeText(this, "This device does not supported - Google Play Services.", Toast.LENGTH_LONG).show();
			        finish();
			    }
			    return false;
			}
			return true;
		} catch (Exception e) {
			
			return true;
		}
    }
    
    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId == null || registrationId.equals("")) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }
    
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private int getAppVersion() {
        try {
            PackageInfo packageInfo = getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(HomeScreen.class.getSimpleName(),
                MODE_PRIVATE);
    }
    

    private void registerforGCMInBackground() {
        GcmRegistration asynTask = new GcmRegistration(this, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        String regId = (String) obj;
                        storeRegistrationId(regId);
                        init(regId);
                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                    }

                });
        asynTask.execute();
    }
    
    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion();
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    public void newGame(View v) {
    	
    	LoadNewGameAlert task = new LoadNewGameAlert(HomeScreen.this, new LoadingListener() {
			
			@Override
			public void onLoadingComplete() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(Object obj) {
				// TODO Auto-generated method stub
				JSONObject json = (JSONObject)obj;
				if(json == null){
					
				}else{
					try {
						String newgame_alert_title = json.getString("newgame_alert_title");
						String newgame_alert_message = json.getString("newgame_alert_message");
						final int game_continue = json.getInt("continue");
						
						if(newgame_alert_title.isEmpty() || newgame_alert_message.isEmpty()){
							startActivity(new Intent(HomeScreen.this, FriendPickActivty.class));
						}else{
							dialog = DialogUtil.createDialog_title_divider_message_two_btn(HomeScreen.this, newgame_alert_title, true, newgame_alert_message, R.drawable.button_ok, -1, 
									new ButtonClickListener() {
										
										@Override
										public void onButtonClick(int s) throws NullPointerException {
											if(s==R.drawable.button_ok)
											{
												if(game_continue == 1){
													startActivity(new Intent(HomeScreen.this, FriendPickActivty.class));
											        
												}
											}
											dialog.dismiss();
											
										}
									}, -1);
							dialog.show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		task.execute();
    }

    public void onStatsClick(View v) {
        if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN)
        {
        	DialogUtil.createDialog_title_bitmap_message1(this, "", null, getString(R.string.no_login_player_statistics_message), R.drawable.button_ok, null);
        	return;
        }
    	String playerId = app.getPlayerID();
        openPlayerStat(playerId);
    }
    
    private void openPlayerStat(String playerId) {
    	Intent intent = new Intent(HomeScreen.this, PlayerStatActivity.class);
        intent.putExtra("player_id", playerId);
        startActivity(intent);
    }

    public void onBackClick(final View v) {
    	/*
    	if(app.play_mode == T4FApplication.PLAY_MODE_FB_LOGIN) {
	    	dialog = DialogUtil.createDialog_title_divider_message_two_btn(this, getString(R.string.logout_title), true, getString(R.string.logout_messsage), R.drawable.button_no, R.drawable.button_yes, new ButtonClickListener() {
				
				@Override
				public void onButtonClick(int s) throws NullPointerException {
					if(s==R.drawable.button_yes)
					{
						((LoginButton)v).setOnClickListener(((LoginButton)v).listener);
				    	((LoginButton)v).confirmLogout=false;
				    	((LoginButton)v).listener.onClick(v);
				    	finish();
					}
					dialog.dismiss();
					
				}
			}, -1); 
	    	
    	}else 
    	*/
    	final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
    	if(app.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN) {
	    	dialog = DialogUtil.createDialog_title_divider_message_two_btn(this, getString(R.string.logout_title), true, getString(R.string.logout_messsage), R.drawable.button_no, R.drawable.button_yes, new ButtonClickListener() {
				
				@Override
				public void onButtonClick(int s) throws NullPointerException {
					if(s==R.drawable.button_yes)
					{						
                    	db.storeLoginPlayerId(0);
                    	//FacebookManager.fbLogout(HomeScreen.this);
                    	app.resetPlayerInfo();
				    	finish();
					}
					dialog.dismiss();
					
				}
			}, -1);
	    	dialog.show();
    	}else if(app.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN) {
    		db.storeNoLoginPlayerId(0);
    		app.resetPlayerInfo();
    		finish();
    	}
    }

    public void refresh(View v) {
        LoadingHomeScreen asynTask = new LoadingHomeScreen(this, mlistener,true, regid);
        asynTask.execute();
//    	sendRequestDialog();
    }
    public void invite(View v) {
    	invite();
    }
    public void goLeaderboard(View v) {
    	leaderboardButtonPressed();
    }
    
    public void showSlideMenu(View v){
    	if(slidemenu.isShown())
    		slidemenu.hide();
    	else
    		slidemenu.show();
    }
    public void sendRequestDialog() {

        FacebookManager.fbInvite("https://fb.me/969637949744865", getString(R.string.fb_post_picture), this, callbackManager, callback1);
        //Bundle params = new Bundle();
        //params.putString("message", context.getString(R.string.fb_apprequest_message));

        /*
        WebDialog requestsDialog = (
            new WebDialog.RequestsDialogBuilder(context,
                Session.getActiveSession(),
                params))
                .setOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                        FacebookException error) {
                        if (error != null) {
                            if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(context, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final String requestId = values.getString("request");
                            if (requestId != null) {
                                dialog = DialogUtil.createDialog_title_divider_message_two_btn(context, context.getString(R.string.your_friends_have_been_invited), true, "", -1, R.drawable.button_ok, new ButtonClickListener() {
									
									@Override
									public void onButtonClick(int s) throws NullPointerException {
										dialog.dismiss();
										
									}
								}, -1);
                            } else {
                                Toast.makeText(context, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        }   
                    }
                })
                .build();
        requestsDialog.show();
        */
    }
    Dialog d;
    private void invite() {
    	if(FacebookManager.init()){
	    	d = DialogUtil.createDialog_title_divider_message_two_btn(this,
	                getString(R.string.invite_friends), true, getString(R.string.fb_invite_friends_message),
	                R.drawable.button_not_now, R.drawable.button_choose_which_friends, new ButtonClickListener() {
	
	                    @Override
	                    public void onButtonClick(int s)
	                            throws NullPointerException {
	                        if (s == R.drawable.button_choose_which_friends)
	                            sendRequestDialog();
	                        d.dismiss();
	                    }
	                }, -1);
	        d.show();
    	}else{
    		
    		d = DialogUtil.createDialog_title_divider_message_two_btn(
    				this,
    				getString(R.string.invite_friends), 
                    true, 
                    getString(R.string.no_login_invite_message),
                    -1, R.drawable.button_ok, new ButtonClickListener() {

                        @Override
                        public void onButtonClick(int s)
                                throws NullPointerException {
                      
                            d.dismiss();
                        }
                    }, -1);
            d.show();
    	}
	}
    private void showAutoturnMessage(String title, String message) {
    	 DialogUtil.createDialog_title_bitmap_message1(this, title, null, message, R.drawable.button_ok, new ButtonClickListener() {

             @Override
             public void onButtonClick(int s)
                     throws NullPointerException {
             }
         });
	}
    
    public void refreshButtonPressed(){
    	refresh(null);
    }
    public void inviteButtonPressed(){
    	invite();
    }
    public void fbButtonPressed(){
    	if(FacebookManager.init()){
			Log.d("Facebook", "Logged In");
			
			dialog = DialogUtil.createDialog_title_divider_message_two_btn(
					HomeScreen.this, 
					"Logout of Facebook?", 
					true, 
					"Disconnect from Facebook and continue without accessing your Facebook friends.", 
					R.drawable.button_no, 
					R.drawable.button_yes, 
					new ButtonClickListener() {
				
						@Override
						public void onButtonClick(int s) throws NullPointerException {
							if(s==R.drawable.button_yes)
							{
								FacebookManager.fbLogout(HomeScreen.this);
								fbLoginButton.setBackgroundResource(R.drawable.button_fb_home_login);
							}
							
							dialog.dismiss();
						}
					},
					-1);
			dialog.show();
			
		}else{
			Log.d("Facebook", "Logged Out");
			ArrayList<String> permissions = new ArrayList<String>();
	        permissions.add("email");
			permissions.add("public_profile");
			permissions.add("user_friends");
            FacebookManager.fbLogin(HomeScreen.this, callbackManager, callback, permissions);
		}
    }
    public void statsButtonPressed(){
    	onStatsClick(null);
    }
    public void leaderboardButtonPressed(){
    	Intent i = new Intent(this, LeaderboardRankingActivity.class);
    	startActivity(i);
    }
    public void storeButtonPressed(){
    	
    }
    public void profileButtonPressed(){
    	Intent i = new Intent(this, Profile.class);
    	i.putExtra("viewOption", T4FApplication.PROFILE_UPDATE);
    	startActivity(i);
    }
    public void settingButtonPressed(){
    	startActivity(new Intent(this, SettingsActivity.class));
    }
    public void helpButtonPressed(){
    	 startActivity(new Intent(this, HelpActivity.class));
    }
    public void supportButtonPressed(){
    	startActivity(new Intent(this, SupportActivity.class));
    }
    public void rateButtonPressed(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rate_url))));
    }
    public void buyAdButtonPressed(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.buy_url))));
    }
    public void aboutButtonPressed(){
    	startActivity(new Intent(this, AboutActivity.class));
    }
    public void logoutButtonPressed(){
    	onBackClick(null);
    }
}
