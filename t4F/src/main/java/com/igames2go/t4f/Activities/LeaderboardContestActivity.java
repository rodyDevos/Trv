package com.igames2go.t4f.Activities;

import java.util.ArrayList;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.LeaderboardContestsDataObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class LeaderboardContestActivity extends Activity {

	private final String TAG = "Leaderboard";
	private final int LEADERBOARD_CURRENT_CONTESTS = 1;
	private final int LEADERBOARD_ENDED_CONTESTS = 2;
	private final int LEADERBOARD_UPCOMING_CONTESTS = 3;
	
	private final int LEADERBOARDCONTESTDEATIL_ACTIVITY = 1;
	
	private ImageButton endedContestsButton;
	private ImageButton currentContestsButton;
	private ImageButton upcomingContestsButton;
	private TextView contestOptionLabel;
	private ListView contestListView;
	
	T4FApplication mApplication;
    private LeaderboardContestListAdapter adapter = null;
    
    ArrayList<LeaderboardContestsDataObject> contests;
    ArrayList<LeaderboardContestsDataObject> endedContests;
    ArrayList<LeaderboardContestsDataObject> currentContests;
    ArrayList<LeaderboardContestsDataObject> upcomingContests;
    int contestsOption;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard_contest);
		
		mApplication = ((T4FApplication)getApplication());
		
		endedContestsButton = (ImageButton)findViewById(R.id.btnEnded);
		currentContestsButton = (ImageButton)findViewById(R.id.btnCurrent);
		upcomingContestsButton = (ImageButton)findViewById(R.id.btnUpcoming);
		contestOptionLabel = (TextView)findViewById(R.id.contestOptionTextView);
		contestListView = (ListView)findViewById(R.id.contestListView);
		
		contests = new ArrayList<LeaderboardContestsDataObject>();
		endedContests = new ArrayList<LeaderboardContestsDataObject>();
		currentContests = new ArrayList<LeaderboardContestsDataObject>();
		upcomingContests = new ArrayList<LeaderboardContestsDataObject>();
		
		if(mApplication.getContests().size() > 0){
			for(LeaderboardContestsDataObject contest : mApplication.getContests()){
				if(contest.getContestPeriod().equalsIgnoreCase("ended"))
					endedContests.add(contest);
				else if(contest.getContestPeriod().equalsIgnoreCase("current"))
					currentContests.add(contest);
				else if(contest.getContestPeriod().equalsIgnoreCase("upcoming"))
					upcomingContests.add(contest);
			}
		}
		
		contestsOption = LEADERBOARD_CURRENT_CONTESTS;
		contests.addAll(currentContests);
		
		selectCurrentContests(null);
		
		Log.e(TAG, "item size: " + contests.size());
		adapter = new LeaderboardContestListAdapter(this, R.layout.layout_leaderboardcontestlistadapter, contests);
        contestListView.setAdapter(adapter);
        
        contestListView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
        		LeaderboardContestsDataObject contest = contests.get(position);
        		Log.d("Selected Contest", contest.toString());
        		
        		Intent i = getIntent();
                i.putExtra("contest", contest);
                setResult(RESULT_OK, i);
                LeaderboardContestActivity.this.finish();
        	} 
        });
        
        if (!contests.isEmpty()) {
            
        } else {
            
            Log.e(TAG, "Contest List is empty");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setMessage("There is no contest right now. Please try again later.");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard_contest, menu);
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LEADERBOARDCONTESTDEATIL_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                	int isUpdate = data.getIntExtra("isUpdated", 0);
                	if(isUpdate == 1){
                		
                		endedContests = new ArrayList<LeaderboardContestsDataObject>();
                		currentContests = new ArrayList<LeaderboardContestsDataObject>();
                		upcomingContests = new ArrayList<LeaderboardContestsDataObject>();
                		
                		if(mApplication.getContests().size() > 0){
                			for(LeaderboardContestsDataObject contest : mApplication.getContests()){
                				if(contest.getContestPeriod().equalsIgnoreCase("ended"))
                					endedContests.add(contest);
                				else if(contest.getContestPeriod().equalsIgnoreCase("current"))
                					currentContests.add(contest);
                				else if(contest.getContestPeriod().equalsIgnoreCase("upcoming"))
                					upcomingContests.add(contest);
                			}
                		}
                		
                		refreshContestTable();
                	}
                }
                break;
        }
    }
	
	private void refreshContestTable(){
		
		switch (contestsOption) {
		case LEADERBOARD_ENDED_CONTESTS:
			adapter.refreshData(endedContests);
			break;
		case LEADERBOARD_CURRENT_CONTESTS:
			adapter.refreshData(currentContests);	
			break;
		case LEADERBOARD_UPCOMING_CONTESTS:
			adapter.refreshData(upcomingContests);
			break;
		default:
			break;
		}
		
	}
	
	public void selectEndedContests(View v){
		
		contestOptionLabel.setText("Ended Contests");
		
		endedContestsButton.setImageResource(R.drawable.leader_ended_on);
		currentContestsButton.setImageResource(R.drawable.leader_current_off);
		upcomingContestsButton.setImageResource(R.drawable.leader_upcoming_off);
		
		if (contestsOption != LEADERBOARD_ENDED_CONTESTS) {
	        contestsOption = LEADERBOARD_ENDED_CONTESTS;
	        refreshContestTable();
	    }
	}
	public void selectCurrentContests(View v){
		
		contestOptionLabel.setText("Current Contests");
		
		endedContestsButton.setImageResource(R.drawable.leader_ended_off);
		currentContestsButton.setImageResource(R.drawable.leader_current_on);
		upcomingContestsButton.setImageResource(R.drawable.leader_upcoming_off);
		
		if (contestsOption != LEADERBOARD_CURRENT_CONTESTS) {
	        contestsOption = LEADERBOARD_CURRENT_CONTESTS;
	        refreshContestTable();
	    }
	}
	public void selectUpcomingContests(View v){
		
		contestOptionLabel.setText("Upcoming Contests");
		
		endedContestsButton.setImageResource(R.drawable.leader_ended_off);
		currentContestsButton.setImageResource(R.drawable.leader_current_off);
		upcomingContestsButton.setImageResource(R.drawable.leader_upcoming_on);
		
		if (contestsOption != LEADERBOARD_UPCOMING_CONTESTS) {
	        contestsOption = LEADERBOARD_UPCOMING_CONTESTS;
	        refreshContestTable();
	    }
	}
	public void goHome(View v){
		
		Intent i = getIntent();
        setResult(RESULT_CANCELED, i);
        this.finish();
	}
}
