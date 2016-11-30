
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.data.PlayersDataObject;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendsListAdapter extends ArrayAdapter<PlayersDataObject> {

    public static final int FAVORITEFRIENDS = 1;
    public static final int BLOCKEDFRIENDS = 2;
    public static final int SEARCHFRIENDS = 3;

    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    public ArrayList<PlayersDataObject> items = null;
    private int friendsType = 0;
    private ImageLoader loader;

    public FriendsListAdapter(Activity ctx, int resourceId,
            ArrayList<PlayersDataObject> items, int friendsType) {

        super(ctx, resourceId, items);
        loader = new ImageLoader(ctx);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.items = items;
        this.friendsType = friendsType;
    }

    public FriendsListAdapter(Activity ctx, int resourceId,
            ArrayList<PlayersDataObject> items) {

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

        FriendsProfileHolder holder = null;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(resource, null);
            holder = new FriendsProfileHolder();
            holder.fb_profile_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.fb_name = (TextView) convertView
                    .findViewById(R.id.fb_picker_title);
            holder.option = (ImageButton) ((ViewStub) convertView
                    .findViewById(R.id.option_stub)).inflate();
            holder.badge = (ImageView)convertView
                    .findViewById(R.id.iv_award);
            holder.favorite = (ImageView)convertView
                    .findViewById(R.id.iv_fav);
            convertView.setTag(holder);
        } else {

            holder = (FriendsProfileHolder) convertView.getTag();

        }
        holder.favorite.setVisibility(View.INVISIBLE);
        holder.badge.setVisibility(View.INVISIBLE);
        FriendsDataObject obj = items.get(position).creatFriendDataObject();
        holder.fb_name.setText(obj.getPla_fb_name());
        convertView.setTag(R.id.btn_addtogame, position);
        
        T4FApplication application = (T4FApplication) context.getApplication();
        if (application.isAlreadyAdded(obj)) {
        	holder.option.setBackgroundResource(R.drawable.icon_added);
        }else{
        	holder.option.setBackgroundResource(R.drawable.icon_additionsign);
        }
        
        if (friendsType == BLOCKEDFRIENDS) {
            holder.option.setBackgroundResource(R.drawable.icon_redx);
        }
        if(obj.getFavorite().equals("1")) {
        	holder.favorite.setVisibility(View.VISIBLE);
        }
        
        if(!TextUtils.isEmpty(obj.getAward_badge_image())) {
    		loader.DisplayImage(
    			context.getString(R.string.url_images) + obj.getAward_badge_image(),
    			holder.badge, false);
    		holder.badge.setVisibility(View.VISIBLE);
        }
        loader.displayPlayerImage(obj.getPla_fb_id(), holder.fb_profile_pic, false);
        return convertView;
    }

    static class FriendsProfileHolder {
        ImageView fb_profile_pic;
        TextView fb_name;
        ImageButton option;
        ImageView badge;
        ImageView favorite;
    }

}
