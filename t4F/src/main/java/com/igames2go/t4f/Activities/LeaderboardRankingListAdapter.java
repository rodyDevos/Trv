
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.LeaderboardPlayersDataObject;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderboardRankingListAdapter extends ArrayAdapter<LeaderboardPlayersDataObject> {

    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    public ArrayList<LeaderboardPlayersDataObject> items = null;
    private ImageLoader loader;

    public LeaderboardRankingListAdapter(Activity ctx, int resourceId, ArrayList<LeaderboardPlayersDataObject> items) {

        super(ctx, resourceId, items);
        loader = new ImageLoader(ctx);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.items = items;

    }

    @Override
    public int getCount() {
        return (items == null ? 0 : items.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	final LeaderboardPlayersDataObject obj = items.get(position);
    	PlayerHolder holder = null;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(resource, null);
            holder = new PlayerHolder();
            holder.noLabel = (TextView) convertView.findViewById(R.id.noLabel);
            holder.profileImageView = (ImageView) convertView.findViewById(R.id.profileImageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            holder.statsButton = (ImageButton) convertView.findViewById(R.id.statsButton);
            holder.pointLabel = (TextView)convertView.findViewById(R.id.pointLabel);
            holder.seperateImageView = (ImageView)convertView.findViewById(R.id.seperateImageView);
            holder.statsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, PlayerStatActivity.class);
			        intent.putExtra("player_id", obj.getPlaId());
			        context.startActivity(intent);
				}
			});
            
            convertView.setTag(holder);
        } else {

            holder = (PlayerHolder) convertView.getTag();

        }
        
        holder.noLabel.setText(obj.getRank());
        loader.displayPlayerImage(obj.getPlayerImage(), holder.profileImageView, false);
        holder.nameLabel.setText(obj.getPlayerName());
        holder.pointLabel.setText(obj.getPoints() + " pts.");
        holder.statsButton.setImageResource(R.drawable.icon_stats);
        
        if(obj.getAttribute1().equalsIgnoreCase("1"))
        	holder.seperateImageView.setVisibility(View.VISIBLE);
        else
        	holder.seperateImageView.setVisibility(View.INVISIBLE);
        return convertView;
    }

    static class PlayerHolder {
    	TextView noLabel;
        ImageView profileImageView;
        TextView nameLabel;
        TextView pointLabel;
        ImageButton statsButton;
        ImageView seperateImageView;
    }

}
