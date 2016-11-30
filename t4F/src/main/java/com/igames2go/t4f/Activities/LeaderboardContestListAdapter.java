
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.LeaderboardContestsDataObject;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderboardContestListAdapter extends ArrayAdapter<LeaderboardContestsDataObject> {

	private final int LEADERBOARDCONTESTDEATIL_ACTIVITY = 1;
	
    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    public ArrayList<LeaderboardContestsDataObject> items = null;
    private ImageLoader loader;

    public LeaderboardContestListAdapter(Activity ctx, int resourceId,
            ArrayList<LeaderboardContestsDataObject> items, int friendsType) {

        super(ctx, resourceId, items);
        loader = new ImageLoader(ctx);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.items = items;
    }

    public LeaderboardContestListAdapter(Activity ctx, int resourceId,
            ArrayList<LeaderboardContestsDataObject> items) {

        super(ctx, resourceId, items);
        loader = new ImageLoader(ctx);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.items = items;

    }

    public ArrayList<LeaderboardContestsDataObject> getData(){
    	return items;
    }
    public void refreshData(ArrayList<LeaderboardContestsDataObject> newItems){
    	items.clear();
    	items.addAll(newItems);
    	this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return (items == null ? 0 : items.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	final LeaderboardContestsDataObject obj = items.get(position);
    	
    	ContestHolder holder = null;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(resource, null);
            holder = new ContestHolder();
            holder.contestImageView = (ImageView) convertView.findViewById(R.id.contestImageView);
            holder.contestTitleLabel = (TextView) convertView.findViewById(R.id.contestNameLabel);
            holder.contestDurationLabel = (TextView) convertView.findViewById(R.id.contestDurationLabel);
            holder.signupImageView = (ImageView) convertView.findViewById(R.id.contestSignupImageView);
            holder.goDetailLayout = (LinearLayout) convertView.findViewById(R.id.goDetailLayout);
            
            convertView.setTag(holder);
        } else {

            holder = (ContestHolder) convertView.getTag();

        }
        
        holder.contestTitleLabel.setText(obj.getContestName());
        holder.contestDurationLabel.setText(obj.getContestBegDate() + " - " + obj.getContestEndDate());
        
        if(obj.getContestImage().length() > 0){
	        loader.DisplayImage(
	    			context.getString(R.string.url_images) + obj.getContestImage(),
	    			holder.contestImageView, false);
        }
        if(obj.getEntered().equalsIgnoreCase("0")) {
    		holder.signupImageView.setVisibility(View.VISIBLE);
        }else{
        	holder.signupImageView.setVisibility(View.INVISIBLE);
        }
        
        holder.goDetailLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, LeaderboardContestDetailActivity.class);
		        intent.putExtra("contest", obj);
		        context.startActivityForResult(intent, LEADERBOARDCONTESTDEATIL_ACTIVITY);
			}
		});
        
        return convertView;
    }

    static class ContestHolder {
        ImageView contestImageView;
        TextView contestTitleLabel;
        TextView contestDurationLabel;
        ImageView signupImageView;
        LinearLayout goDetailLayout;
    }

}
