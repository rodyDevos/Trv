
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FacebookFriendsDataObject;
import com.igames2go.t4f.data.FriendProperties;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.tasks.CheckFriendProperties;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class FacebookFriendsListAdapter extends ArrayAdapter<FacebookFriendsDataObject> {
    private final String TAG = "T4F";
    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    public ArrayList<FacebookFriendsDataObject> items = null;
    public ImageLoader imageLoader;

    public FacebookFriendsListAdapter(Activity ctx, int resourceId,
            ArrayList<FacebookFriendsDataObject> items) {
        super(ctx, resourceId, items);
        app = (T4FApplication) ctx.getApplication();
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        this.items = items;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return (items == null ? 0 : items.size());
    }

    private T4FApplication app;
    FriendsProfileHolder holder = null;

    private class LoaderAsyncTask extends AsyncTask<Void, Void, BitmapDrawable> {

        ImageView v;

        public LoaderAsyncTask(ImageView v, String fbID) {
            super();
            this.v = v;
            this.fbID = fbID;
        }

        String fbID;

        @Override
        protected BitmapDrawable doInBackground(Void... params) {
            BitmapDrawable bmp = null;
            try {

                final InputStream is = (InputStream) HttpManager
                        .getResponse(
                                "https://graph.facebook.com/"
                                        + fbID
                                        + "/picture", true);
                bmp = new BitmapDrawable(is);
                app.map.put(fbID, bmp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            super.onPostExecute(result);
            if (result == null)
            {
                v.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                return;
            }
            v.setImageDrawable(result);

        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(resource, null);
            holder = new FriendsProfileHolder();
            holder.fb_profile_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.fb_name = (TextView) convertView
                    .findViewById(R.id.fb_picker_title);
            holder.fb_index = (TextView) convertView
                    .findViewById(R.id.fb_index);
            holder.option = (ImageButton) convertView.findViewById(R.id.fb_picker_opt);
            holder.badge = (ImageView) convertView.findViewById(R.id.iv_award);
            holder.favorite = (ImageView) convertView.findViewById(R.id.iv_fav);
            convertView.setTag(holder);
        } else {
            holder = (FriendsProfileHolder) convertView.getTag();
        }
        
        holder.fb_profile_pic
                .setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        holder.fb_name.setText("");
        holder.option.setVisibility(View.INVISIBLE);
        holder.badge.setVisibility(View.INVISIBLE);
        holder.favorite.setVisibility(View.INVISIBLE);
        convertView.findViewById(R.id.fb_row).setTag(R.id.btn_addtogame, position);

        final View convertViewObj = convertView;

        final FacebookFriendsDataObject fbfrndobj = items.get(position);
        holder.fb_name.setText(fbfrndobj.getName());
        if(((FacebookFriendsActivity)context).map.get(fbfrndobj.getName().charAt(0)+"")==position) {
        	holder.fb_index.setVisibility(View.VISIBLE);
        	holder.fb_index.setText(fbfrndobj.getName().charAt(0)+"");
        }
        else {
        	holder.fb_index.setVisibility(View.GONE);
        }
        /*
        imageLoader.DisplayImage("https://graph.facebook.com/"
                + fbfrndobj.getId()
                + "/picture", holder.fb_profile_pic, false);
        */
        if(fbfrndobj.getId().contains(".png") || fbfrndobj.getId().contains(".jpg")){
        	imageLoader.DisplayImage(
        			context.getString(R.string.url_images) + fbfrndobj.getId(),
        			holder.fb_profile_pic, false);
    	}else{
    		imageLoader.DisplayImage("https://graph.facebook.com/" + fbfrndobj.getId() + "/picture", holder.fb_profile_pic, false);
    	}
        
        if (fbfrndobj.isUpdated == false) {
            CheckFriendProperties asynTask = new CheckFriendProperties(context, fbfrndobj.getId(),
                    new LoadingListener() {

                        @Override
                        public void onLoadingComplete(Object obj) {
                            handleData((FriendProperties) obj, fbfrndobj, convertViewObj, position);
                        }

                        @Override
                        public void onError(Object error) {

                        }

                        @Override
                        public void onLoadingComplete() {

                        }

                    });
            asynTask.doInBackground();
        }
        else {
            updateOptionButton(fbfrndobj, convertViewObj);
        }
        holder.badge.bringToFront();
        return convertView;
    }

    static class FriendsProfileHolder {
    	TextView fb_index;
    	ImageView fb_profile_pic;
        TextView fb_name;
        ImageButton option;
        ImageView badge;
        ImageView favorite;
    }

    private void handleData(FriendProperties obj, FacebookFriendsDataObject fbfrndobj,
            View convertView, int position) {
        if (obj != null) {
            ArrayList<FriendsDataObject> item = (ArrayList<FriendsDataObject>) obj
                    .getFriendProperties();
            if (!item.isEmpty()) {
                FriendsDataObject itemobj = item.get(0);
                fbfrndobj.setPla_id(itemobj.getPla_id());
                fbfrndobj.setFavorite(itemobj.getFavorite());
                fbfrndobj.setBlocked(itemobj.getBlocked());
                fbfrndobj.setPlaapp_id(itemobj.getPlaapp_id());
                fbfrndobj.setAward_badge_image(itemobj.getAward_badge_image());
                fbfrndobj.isUpdated = true;
                items.remove(position);
                items.add(position, fbfrndobj);
                updateOptionButton(fbfrndobj, convertView);
                // final FriendsDataObject frndDataObj =
                // fbfrndobj.creatFriendDataObject();
            } else {
                Log.e(TAG, "FriendsDataObject search is empty");
            }
        } else {
            Log.e(TAG, "FriendProperties Object is null");
        }
    }

    private void updateOptionButton(FacebookFriendsDataObject fbfrndobj,
            View convertView) {
        FriendsProfileHolder holderObj = (FriendsProfileHolder) convertView.getTag();
        Log.i("T4F", fbfrndobj.toString());
        if (holderObj != null)
        {
            // T4FApplication app = (T4FApplication) context.getApplication();
            holderObj.option.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(fbfrndobj.getAward_badge_image())){
        		imageLoader.DisplayImage(
        			context.getString(R.string.url_images) + fbfrndobj.getAward_badge_image(),
        			holderObj.badge, false);
        		holderObj.badge.setVisibility(View.VISIBLE);
        		holderObj.badge.bringToFront();
            }
            if(fbfrndobj.getFavorite().equalsIgnoreCase("1")) {
            	holderObj.favorite.setVisibility(View.VISIBLE); 
            }
            if (app.isAlreadyAdded(fbfrndobj.getId())) {
                holderObj.option.setBackgroundResource(R.drawable.icon_added);
            } else {

                if (fbfrndobj.getPla_id() != null)
                {
                    if ((fbfrndobj.getPla_id()).equals("0"))
                    {
                        holderObj.option.setBackgroundResource(R.drawable.icon_request);
                    }
                    else
                    {
                        holderObj.option
                                .setBackgroundResource(R.drawable.icon_additionsign);
                    }

                }

            }
            // convertView.setTag(R.id.btn_addtogame, frndDataObj);
        }

    }

}
