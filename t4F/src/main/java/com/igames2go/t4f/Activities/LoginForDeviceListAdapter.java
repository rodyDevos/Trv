
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SettingsLoginsDataObject;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class LoginForDeviceListAdapter extends ArrayAdapter<SettingsLoginsDataObject> {

    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    public ArrayList<SettingsLoginsDataObject> item = null;
    public ImageLoader imageLoader;

    public LoginForDeviceListAdapter(Activity ctx, int resourceId,
            ArrayList<SettingsLoginsDataObject> items) {

        super(ctx, resourceId, items);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        item = items;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return (item == null ? 0 : item.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LoginForDeviceHolder holder = null;
        if (convertView == null) {
            convertView = (LinearLayout) inflater.inflate(resource, null);
            holder = new LoginForDeviceHolder();
            holder.fb_profile_pic = (ImageView) ((ViewStub) convertView
                    .findViewById(R.id.fb_profile_pic_stub)).inflate();
            holder.fb_name = (TextView) convertView
                    .findViewById(R.id.fb_picker_title);
            holder.device_notify = (CheckBox) convertView
                    .findViewById(R.id.deviceNotify);

            convertView.setTag(holder);
        } else {

            holder = (LoginForDeviceHolder) convertView.getTag();

        }

        SettingsLoginsDataObject obj = item.get(position);
        holder.fb_name.setTag(obj);
        holder.device_notify.setTag(obj);

        holder.fb_name.setText(obj.getPla_fb_name());

        if(obj.getPla_fb_id().contains(".png") || obj.getPla_fb_id().contains(".jpg")){
        	imageLoader.DisplayImage(context.getString(R.string.url_images)
                    + obj.getPla_fb_id() , holder.fb_profile_pic, false);
        }else{
        	imageLoader.DisplayImage("https://graph.facebook.com/"
                + obj.getPla_fb_id()
                + "/picture", holder.fb_profile_pic, false);
        }
        
        if ((obj.getDevice_notify()).equals("-1"))
            holder.device_notify.setEnabled(false);

        else if ((obj.getDevice_notify()).equals("1"))
            holder.device_notify.setChecked(true);

        else
            holder.device_notify.setChecked(false);

        return convertView;
    }

    static class LoginForDeviceHolder {
       // ImageButton deleteBtn;
        ImageView fb_profile_pic;
        TextView fb_name;
        CheckBox device_notify;
    }

}
