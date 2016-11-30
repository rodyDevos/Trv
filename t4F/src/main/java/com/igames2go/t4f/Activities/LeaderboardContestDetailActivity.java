package com.igames2go.t4f.Activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;


import com.facebook.share.Sharer;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.LeaderboardContestsDataObject;
import com.igames2go.t4f.tasks.EnterContest;
import com.igames2go.t4f.tasks.GetAwards;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.CustomButton;
import com.igames2go.t4f.view.CustomTextView;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class LeaderboardContestDetailActivity extends Activity implements OnClickListener {

	private Button contestEnterButton;
	private ImageButton contestPostButton;
	
	private ImageView contestEnterImageView;
	private TextView contestTitleLabel;
	private ImageView contestTitleImageView;
	private TextView contestDurationLabel;
	private TextView contestFeeLabel;
	private TextView contestantsLabel;
	private WebView descriptionView;
	
	private CustomTextView dayCountTextView;
	private CustomTextView hourCountTextView;
	private CustomTextView minCountTextView;
	
	private CustomTextView daysTextView;
	private CustomTextView hoursTextView;
	private CustomTextView minsTextView;
	
	private LinearLayout countLayout;
	private CustomButton getAwardsButton; 
	private ImageView contestAwardImageView;
	
	private ImageLoader loader;
	private T4FApplication mApplication;
	private LeaderboardContestsDataObject contest;
	private static Dialog dialog;
	private int isUpdated;
	
	private Date contestBegDateTime, contestEndDateTime, allTimeDate;
	private SimpleDateFormat format;

	private CallbackManager callbackManager;

	int i = 0;
	Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
        	
        	checkContestTime();
        	
        	i++;
        	Log.d("i", i + "");
            timerHandler.postDelayed(this, 1000*60);
        }
    };
    
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard_contest_detail);

		callbackManager = CallbackManager.Factory.create();

		mApplication = (T4FApplication)getApplication();
		loader = new ImageLoader(this);
		contest = (LeaderboardContestsDataObject)getIntent().getSerializableExtra("contest");
		isUpdated = 0;
		
		contestTitleLabel = (TextView)findViewById(R.id.contestTitleTextView);
		contestTitleImageView = (ImageView)findViewById(R.id.contestTitleImageView);
		contestEnterButton = (Button)findViewById(R.id.contestEnterButton);
		contestEnterImageView = (ImageView)findViewById(R.id.contestEnterImageView);
		
		contestDurationLabel = (TextView)findViewById(R.id.contestDurationTextView);
		contestFeeLabel = (TextView)findViewById(R.id.contestFeeTextView);
		contestantsLabel = (TextView)findViewById(R.id.contestantTextView);
		descriptionView = (WebView)findViewById(R.id.contestDescriptionWebView);
		
		dayCountTextView = (CustomTextView)findViewById(R.id.dayCountTextView);
		hourCountTextView = (CustomTextView)findViewById(R.id.hourCountTextView);
		minCountTextView = (CustomTextView)findViewById(R.id.minCountTextView);
		daysTextView = (CustomTextView)findViewById(R.id.daysTextView);
		hoursTextView = (CustomTextView)findViewById(R.id.hoursTextView);
		minsTextView = (CustomTextView)findViewById(R.id.minsTextView);
		countLayout = (LinearLayout)findViewById(R.id.countLayout);
		getAwardsButton = (CustomButton)findViewById(R.id.getAwardsButton);
		contestAwardImageView = (ImageView)findViewById(R.id.contestAwardImageView);
		
		contestPostButton = (ImageButton)findViewById(R.id.contestPostButton);
		
		getAwardsButton.setOnClickListener(this);
		if(contest != null){
			Log.d("Contest", contest.toString());
			loader.displayPlayerImage(contest.getContestImage(), contestTitleImageView, false);
			contestTitleLabel.setText(contest.getContestName());
			contestDurationLabel.setText(contest.getContestBegDate() + " - " + contest.getContestEndDate());
			
			if(contest.getEntered().equalsIgnoreCase("1")){
				contestEnterImageView.setImageResource(R.drawable.leader_enter_contest_on);
				contestEnterButton.setClickable(false);
			}else{
				contestEnterImageView.setImageResource(R.drawable.leader_enter_contest_off);
				contestEnterButton.setClickable(true);
			}
			
			if(contest.getAllowFBPost().equalsIgnoreCase("1")){
				contestPostButton.setVisibility(View.VISIBLE);
			}else{
				contestPostButton.setVisibility(View.INVISIBLE);
			}
			contestFeeLabel.setText(contest.getContestFee());
			contestantsLabel.setText(contest.getContestants());
			
			descriptionView.setInitialScale(1);
			descriptionView.getSettings().setJavaScriptEnabled(true);
			descriptionView.getSettings().setUseWideViewPort(true);

			if(contest.getContestUrl().length() > 0)
				descriptionView.loadUrl(contest.getContestUrl());
		}
		
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			contestBegDateTime = format.parse(contest.getContestBegDateTime());
			contestEndDateTime = format.parse(contest.getContestEndDateTime());
			allTimeDate = format.parse("2010-12-31 00:00:00");
			
			timerHandler.postDelayed(timerRunnable, 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard_contest_detail, menu);
		return true;
	}

	private void updateCountView(long diffInSec){
		long days = 0;
		long hours = 0;
		long minutes = 0;
	    
		diffInSec = Math.abs(diffInSec);
	    days = diffInSec / (60 * 60 * 24);
	    hours = (diffInSec - (days * 60 * 60 * 24)) / (60 * 60);
	    minutes = (diffInSec - (days * 60 * 60 * 24) - (hours * 60 * 60)) / 60;
	    
	    dayCountTextView.setText(Math.abs(days) + "");
	    hourCountTextView.setText(Math.abs(hours) + "");
	    minCountTextView.setText(Math.abs(minutes) + "");
	    
	    if(Math.abs(days) <= 1)
	    	daysTextView.setText("day");
	    else
	    	daysTextView.setText("days");
	    
	    if(Math.abs(hours) <= 1)
	    	hoursTextView.setText("hour");
	    else
	    	hoursTextView.setText("hours");
	    
	    if(Math.abs(minutes) <= 1)
	    	minsTextView.setText("min");
	    else
	    	minsTextView.setText("mins");
	}
	private void checkContestTime(){
		Date currentDateTime = new Date();
		
		if(contestBegDateTime.before(allTimeDate)){
			// if contest begin date time is before 2010-12-31
			contestAwardImageView.setImageResource(R.drawable.leader_awards_alltime);
			countLayout.setVisibility(LinearLayout.INVISIBLE);
			getAwardsButton.setVisibility(View.INVISIBLE);
			
			timerHandler.removeCallbacks(timerRunnable);
			
		}else if(currentDateTime.before(contestBegDateTime)){
			// if contest is not started
			contestAwardImageView.setImageResource(R.drawable.leader_awards_not_started);
			countLayout.setVisibility(LinearLayout.VISIBLE);
			getAwardsButton.setVisibility(View.INVISIBLE);
			
			long diffInSec = TimeUnit.MILLISECONDS.toSeconds(currentDateTime.getTime() - contestBegDateTime.getTime());
			updateCountView(diffInSec);
			
		}else if(currentDateTime.before(contestEndDateTime)){
			// if contest is open
			contestAwardImageView.setImageResource(R.drawable.leader_awards);
			countLayout.setVisibility(LinearLayout.VISIBLE);
			getAwardsButton.setVisibility(View.INVISIBLE);
			
			Log.d("Contest Time", contestEndDateTime.toString());
			Log.d("Contest Time", currentDateTime.toString());
			long diffInSec = TimeUnit.MILLISECONDS.toSeconds(contestEndDateTime.getTime() - currentDateTime.getTime());
			updateCountView(diffInSec);
		}else{
			// if contest is finished
			contestAwardImageView.setImageResource(R.drawable.leader_awards_ended);
			countLayout.setVisibility(LinearLayout.INVISIBLE);
			getAwardsButton.setVisibility(View.VISIBLE);
			
			timerHandler.removeCallbacks(timerRunnable);
		}
	}
	public void enterContest(View v){
		
		String message = "By entering the \"" + contest.getContestName() + "\" contest, your ranking will be shown and you will be eligible for contest awards.";
		if(contest.getContestFee().length() > 0)
				message = message + "The entry fee is " + contest.getContestFee() + ".";
		
		dialog = DialogUtil.createDialog_title_divider_message_two_btn(this, 
				"Enter Contest", 
				true, 
				message, 
				R.drawable.button_cancel, 
				R.drawable.button_ok, 
				new ButtonClickListener() {
			
					@Override
					public void onButtonClick(int s) throws NullPointerException {
						if(s==R.drawable.button_ok)
						{
							contestEnter(contest.getContestId());		                	
						}
						dialog.dismiss();
						
					}
				},
				-1);
    	dialog.show();
	}
	public void postContest(View v){
		if(FacebookManager.init()) {
			dialog = DialogUtil.createDialog_title_divider_message_two_btn(this,
    				"Share Contest", 
    				false, 
    				getString(R.string.fb_ok_to_post_contest),
    				R.drawable.button_cancel, 
    				R.drawable.button_ok, 
    				new ButtonClickListener() {

		    			@Override
		    			public void onButtonClick(int s)
		    					throws NullPointerException {
		    				if (s == R.drawable.button_ok)
		    					post();
		    				dialog.dismiss();
		    			}
		    		}, 
		    		-1);
			dialog.show();
    	}
    	else
    	
    	{
    		dialog = DialogUtil.createDialog_title_divider_message_two_btn(this,
                    "Share Contest", 
                    false, 
                    getString(R.string.fb_not_logged_in),
                    -1, 
                    R.drawable.button_ok, 
                    new ButtonClickListener() {

                        @Override
                        public void onButtonClick(int s)
                                throws NullPointerException {
                      
                        	dialog.dismiss();
                        }
                    }, -1);
    		dialog.show();
    	}
	}
	public void getAwards(View v){
		
		GetAwards asyncTask = new GetAwards(this, contest.getContestId(),
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }
					@Override
                    public void onLoadingComplete(Object obj) {
						String message = "";
						String title = "";
						String imageName = "";
						
						if(obj == null){
							message = "Upexpected Error. Please try again later.";
							
                    		DialogUtil.createDialog_title_bitmap_message1(LeaderboardContestDetailActivity.this, 
                    				"Awards", 
                    				null, 
                    				"No Awards", 
                    				R.drawable.button_ok, null);
						}else{
							JSONObject result = (JSONObject)obj;
							
							try {
								title = result.getString("header");
								message = result.getString("message");
								imageName = result.getString("image");
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							if(imageName.length() == 0)
								imageName = null;
                    		DialogUtil.createDialog_title_bitmap_message1(LeaderboardContestDetailActivity.this, 
                    				title, 
                    				imageName, 
                    				message, 
                    				R.drawable.button_ok, null);
						}
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
	}	
	private void post(){

		/*
		Bundle params = new Bundle();
	    params.putString("name", contest.getContestName());
	    params.putString("caption", getString(R.string.fb_post_contest_message));
	    params.putString("description", getString(R.string.fb_post_description_android));
	    params.putString("link", getString(R.string.fb_post_link_android));
	    params.putString("picture", getString(R.string.fb_post_picture));
		*/

		FacebookManager.fbShare(
				contest.getContestName(),
				getString(R.string.fb_post_link_android),
				getString(R.string.fb_post_picture),
				getString(R.string.fb_post_description_android),
				this,
				callbackManager,
				new FacebookCallback<Sharer.Result>() {
					@Override
					public void onSuccess(Sharer.Result result) {
						dialog = DialogUtil.createDialog_title_divider_message_two_btn(LeaderboardContestDetailActivity.this,
								"",
								false,
								getString(R.string.fb_complete_post),
								-1,
								R.drawable.button_ok,
								new ButtonClickListener() {

									@Override
									public void onButtonClick(int s)
											throws NullPointerException {

										dialog.dismiss();
									}
								}, -1);
						dialog.show();
					}

					@Override
					public void onCancel() {

					}

					@Override
					public void onError(FacebookException error) {

					}
				}
		);

		/*
	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
				public void onComplete(Bundle values,
						FacebookException error) {
					// TODO Auto-generated method stub
	            	if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                    	dialog = DialogUtil.createDialog_title_divider_message_two_btn(LeaderboardContestDetailActivity.this,
	                                "", 
	                                false, 
	                                getString(R.string.fb_complete_post),
	                                -1, 
	                                R.drawable.button_ok, 
	                                new ButtonClickListener() {

	                                    @Override
	                                    public void onButtonClick(int s)
	                                            throws NullPointerException {
	                                  
	                                    	dialog.dismiss();
	                                    }
	                                }, -1);
	                		dialog.show();
	                    } else {
	                        // User clicked the Cancel button
	                        
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                    Toast.makeText(getApplicationContext(), 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT).show();
	                } else {
	                    // Generic, ex: network error
	                    Toast.makeText(getApplicationContext(), 
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT).show();
	                }
				}

	        })
	        .build();
	    feedDialog.show();
	    */
	}
	public void goBack(View v){
		
		timerHandler.removeCallbacks(timerRunnable);
		
		Intent i = getIntent();
        i.putExtra("isUpdated", isUpdated);
        setResult(RESULT_OK, i);
        this.finish();
        
	}
	private void contestEnter(String contestId){
		EnterContest asyncTask = new EnterContest(this, contestId,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }
					@Override
                    public void onLoadingComplete(Object obj) {
                        
                    	String result = (String)obj;
                    	String message = "";
                    	if(result.equalsIgnoreCase("1")){
                    		
                    		message = "You are now entered into the \"" + contest.getContestName() +"\" contest. Good Luck!";
                    		DialogUtil.createDialog_title_bitmap_message1(LeaderboardContestDetailActivity.this, 
                    				"Entered", 
                    				null, 
                    				message, 
                    				R.drawable.button_ok, null);
                    		
                    		contestEnterImageView.setImageResource(R.drawable.leader_enter_contest_on);
            				contestEnterButton.setClickable(false);
            				
                    		//if(mApplication.getContests().contains(contest)){
            				int index = -1;
            					for(int i = 0; i < mApplication.getContests().size(); i++){
            						if(contest.getContestId().equalsIgnoreCase(mApplication.getContests().get(i).getContestId())){
            							index = i;
            							break;
            						}
            					}
                    			if(index == -1)
                    				return;
                    			
                    			contest.setEntered("1");
                        		contest.setViewable("1");
                        		
                        		int contestants = 0;

                        		try {
                        			contestants = Integer.parseInt(contest.getContestants());
                        			contestants++;
                            		contest.setContestants(contestants+"");
                            		
                        		} catch(NumberFormatException nfe) {
                        			System.out.println("Could not parse " + nfe);
                        		} 
                        		
                        		mApplication.getContests().set(index, contest);
                        		
                        		isUpdated = 1;
                    		//}
                    		
                    	}else{
                    		message = "Upexpected Error. Please try again later.";
                    		DialogUtil.createDialog_title_bitmap_message1(LeaderboardContestDetailActivity.this, 
                    				"Error", 
                    				null, 
                    				message, 
                    				R.drawable.button_ok, null);
                    	}
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.getAwardsButton:
			getAwards(v);
			break;

		default:
			break;
		}
	}
}
