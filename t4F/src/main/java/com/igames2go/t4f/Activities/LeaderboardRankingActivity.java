package com.igames2go.t4f.Activities;

import java.util.ArrayList;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FacebookFriendsDataObject;
import com.igames2go.t4f.data.FacebookFriendsList;
import com.igames2go.t4f.data.LeaderboardContestsDataObject;
import com.igames2go.t4f.data.LeaderboardPlayersDataObject;
import com.igames2go.t4f.tasks.LoadLeaderboardContests;
import com.igames2go.t4f.tasks.LoadLeaderboardPlayers;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.FontFitTextView;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class LeaderboardRankingActivity extends Activity {

	private final String TAG = "Leaderboard";
	private final int LEADERBOARD_PLAYERS_FACEBOOK = 1;
	private final int LEADERBOARD_PLAYERS_FAVORITE = 2;
	private final int LEADERBOARD_PLAYERS_ALL = 3;
	
	private final int LEADERBOARDCONTESTLIST_ACTIVITY = 1;
	
	private ImageButton facebookPlayersButton;
	private ImageButton favoritePlayersButton;
	private ImageButton allPlayersButton;
	private ImageButton prevButton;
	private ImageButton nextButton;
	private FontFitTextView contestTitleLabel;
	private ImageView contestTitleImageView;
	private ListView playersListView;
	
    private LeaderboardRankingListAdapter adapter = null;
    ArrayList<LeaderboardContestsDataObject> contests;
    ArrayList<LeaderboardPlayersDataObject> players;
    int currentContestIndex;
    int playersOption;
    private T4FApplication mApplication;
    private ImageLoader loader;
    private static Dialog dialog;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard_ranking);
		
		mApplication = (T4FApplication)getApplication();
		loader = new ImageLoader(this);
		
		prevButton = (ImageButton)findViewById(R.id.btnPrev);
		nextButton = (ImageButton)findViewById(R.id.btnNext);
		facebookPlayersButton = (ImageButton)findViewById(R.id.btnFriend);
		favoritePlayersButton = (ImageButton)findViewById(R.id.btnFavorite);
		allPlayersButton = (ImageButton)findViewById(R.id.btnAll);
		contestTitleLabel = (FontFitTextView)findViewById(R.id.contestTitleTextView);
		contestTitleImageView = (ImageView)findViewById(R.id.contestTitleImageView);
		playersListView = (ListView)findViewById(R.id.playersListView);
		
		contests = new ArrayList<LeaderboardContestsDataObject>();
		players = new ArrayList<LeaderboardPlayersDataObject>();
		
		adapter = new LeaderboardRankingListAdapter(this, R.layout.layout_leaderboardrankinglistadapter, players);
		playersListView.setAdapter(adapter);
        
		currentContestIndex = -1;
		loadContests();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard_ranking, menu);
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LEADERBOARDCONTESTLIST_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                	contests = mApplication.getContests();
                	LeaderboardContestsDataObject contest = (LeaderboardContestsDataObject)data.getSerializableExtra("contest");
                	Log.d("Contest", contest.toString());
                	
                	for(int i = 0; i< contests.size(); i++){
                		LeaderboardContestsDataObject obj = contests.get(i);
                		if(obj.getContestId().equalsIgnoreCase(contest.getContestId())){
                			currentContestIndex = i;
                			break;
                		}
                	}
                	showCurrentContest();
                }else{
                	this.goBack(null);
                }
                break;
        }
    }
	
	private void loadContests() {
		LoadLeaderboardContests asyncTask = new LoadLeaderboardContests(this,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @SuppressWarnings("unchecked")
					@Override
                    public void onLoadingComplete(Object obj) {
                        
                    	contests = (ArrayList<LeaderboardContestsDataObject>) obj;
                        Log.e(TAG, "item size: " + contests.size());
                        
                        for(int i = 0; i< contests.size(); i++){
                        	LeaderboardContestsDataObject contest = contests.get(i);
                        	Log.d("Name", contest.getContestName());
                        	if(contest.getDefault().equalsIgnoreCase("1")){
                        		currentContestIndex = i;
                        		//break;
                        	}
                        }
                        
                        selectAllPlayers(null);
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }
	
	private void loadContestPlayers(String contestId, String group, String friends) {
		
		LoadLeaderboardPlayers asyncTask = new LoadLeaderboardPlayers(this, contestId, group, friends,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @SuppressWarnings("unchecked")
					@Override
                    public void onLoadingComplete(Object obj) {
                        
                    	if(obj != null)
                    		players.addAll((ArrayList<LeaderboardPlayersDataObject>) obj);
                    	
                    	Log.e(TAG, "item size: " + players.size());
                    	adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }
	
	private void showCurrentContest(){
		players.clear();
		adapter.notifyDataSetChanged();
		
		Log.d("Current Contest Index", currentContestIndex+"");
		
		if(currentContestIndex == -1){
			prevButton.setImageResource(R.drawable.icon_arrow_left_disable);
			nextButton.setImageResource(R.drawable.icon_arrow_right_disable);
			return;
		}
		if(currentContestIndex == 0){
			prevButton.setImageResource(R.drawable.icon_arrow_left_disable);
		}else{
			prevButton.setImageResource(R.drawable.icon_arrow_left);
		}
		if(currentContestIndex == contests.size() - 1){
			nextButton.setImageResource(R.drawable.icon_arrow_right_disable);
		}else{
			nextButton.setImageResource(R.drawable.icon_arrow_right);
		}
		
		LeaderboardContestsDataObject contest = contests.get(currentContestIndex);
		
		contestTitleLabel.setText(contest.getContestName());
		
		if(contest.getContestImage().length() > 0){
			String url = this.getString(R.string.url_images)+contest.getContestImage();
			url = url.replaceAll(" ", "%20");
			
			loader.DisplayImage(url, contestTitleImageView, false);
		}
		
		if(contest.getViewable().equals("1")){
			String group = "";
			String friends = "";
			switch (playersOption) {
			case LEADERBOARD_PLAYERS_FACEBOOK:
				group = "friends";
				if(FacebookManager.init()){
					FacebookFriendsList fbFriends = (FacebookFriendsList) FacebookManager.fbFriends;
					
					ArrayList<FacebookFriendsDataObject> items = (ArrayList<FacebookFriendsDataObject>) fbFriends.getFacebookFriends();
		            
		            if (!items.isEmpty()) {
		            	FacebookFriendsDataObject obj = null;
		            	for(int i=0 ; i<items.size(); i++) {
		            		obj = items.get(i);
		            		if(friends.length() > 0)
		            			friends = friends + ",";
		            		friends = friends + obj.getId();
		            	}
		            }
				}
				Log.d("Leaderboard", friends);
				break;
			case LEADERBOARD_PLAYERS_FAVORITE:
				group = "favorites";
				break;
			case LEADERBOARD_PLAYERS_ALL:
				group = "all";
				break;
			default:
				break;
			}
			
			loadContestPlayers(contest.getContestId(), group, friends);
		}else{
			
			DialogUtil.createDialog_not_entered(LeaderboardRankingActivity.this, 
					"Not Entered", 
					null, 
					"", 
					R.drawable.button_ok, 
					null);
		}
	}
	public void showPrevContest(View v){
		if(currentContestIndex == 0){
			return;
		}
		currentContestIndex--;
		showCurrentContest();
	}
	public void showNextContest(View v){
		if(currentContestIndex == (contests.size() - 1)){
			return;
		}
		currentContestIndex++;
		showCurrentContest();
	}
	
	public void selectFacebookPlayers(View v){
		
		facebookPlayersButton.setImageResource(R.drawable.leader_friends_on);
		favoritePlayersButton.setImageResource(R.drawable.leader_favorites_off);
		allPlayersButton.setImageResource(R.drawable.leader_all_off);
		
		if (playersOption != LEADERBOARD_PLAYERS_FACEBOOK) {
	        playersOption = LEADERBOARD_PLAYERS_FACEBOOK;
	        showCurrentContest();
	    }
	}
	public void selectFavoritePlayers(View v){
		
		facebookPlayersButton.setImageResource(R.drawable.leader_friends_off);
		favoritePlayersButton.setImageResource(R.drawable.leader_favorites_on);
		allPlayersButton.setImageResource(R.drawable.leader_all_off);
		
		if (playersOption != LEADERBOARD_PLAYERS_FAVORITE) {
	        playersOption = LEADERBOARD_PLAYERS_FAVORITE;
	        showCurrentContest();
	    }
	}
	public void selectAllPlayers(View v){
		
		facebookPlayersButton.setImageResource(R.drawable.leader_friends_off);
		favoritePlayersButton.setImageResource(R.drawable.leader_favorites_off);
		allPlayersButton.setImageResource(R.drawable.leader_all_on);
		
		if (playersOption != LEADERBOARD_PLAYERS_ALL) {
	        playersOption = LEADERBOARD_PLAYERS_ALL;
	        showCurrentContest();
	    }
	}
	public void goBack(View v){
		finish();
	}
	public void goContestList(View v){
		Intent i = new Intent(this, LeaderboardContestActivity.class);
		startActivityForResult(i, LEADERBOARDCONTESTLIST_ACTIVITY);
	}
}
