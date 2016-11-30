
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.MessagesDataObject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatListAdapter extends ArrayAdapter<MessagesDataObject> {

    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    ArrayList<MessagesDataObject> mItems = null;

    public ChatListAdapter(Activity ctx, int resourceId,
            ArrayList<MessagesDataObject> items) {

        super(ctx, resourceId, items);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
        mItems = items;
    }
    
    @Override
    public boolean isEnabled(int position) {
    	// TODO Auto-generated method stub
    	return false;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
    	return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatHolder holder = null;

        if (convertView == null) {
            convertView =  inflater.inflate(resource, null);
            holder = new ChatHolder();

            holder.ll_leftchatcontainer = (LinearLayout) convertView
                    .findViewById(R.id.left_chat_container);
            holder.ll_rightchatcontainer = (LinearLayout) convertView
                    .findViewById(R.id.right_chat_container);
            holder.ll_OtherMesgType = (LinearLayout) convertView
                    .findViewById(R.id.llOtherMesgType);
            holder.ivMsgType = (ImageView) convertView
                    .findViewById(R.id.ivMesgType);

            convertView.setTag(holder);
        } else {

            holder = (ChatHolder) convertView.getTag();
        }
        holder.ll_leftchatcontainer.setVisibility(View.GONE);
        holder.ll_rightchatcontainer.setVisibility(View.GONE);
        holder.ll_OtherMesgType.setVisibility(View.GONE);
        holder.ivMsgType.setVisibility(View.GONE);
        
        MessagesDataObject obj = mItems.get(position);
        
        if ((obj.getMes_type()).contains("A")
                || (obj.getMes_type()).equals("S")) {
            View v = holder.ll_OtherMesgType;
            v.setVisibility(View.VISIBLE);
            holder.ivMsgType.setVisibility(View.VISIBLE);
            holder.ivMsgType.setImageResource(R.drawable.icon_action);

            ((TextView) v.findViewById(R.id.tv_time)).setText(obj
                    .getMes_add_datetime());
            ((TextView) v.findViewById(R.id.tv_chat_text)).setText(obj
                    .getMes_text());
        } else if ((obj.getMes_type()).equals("W")) {
            View v = holder.ll_OtherMesgType;
            v.setVisibility(View.VISIBLE);
            holder.ivMsgType.setVisibility(View.VISIBLE);
            holder.ivMsgType.setImageResource(R.drawable.icon_waiting);

            ((TextView) v.findViewById(R.id.tv_time)).setText(obj
                    .getMes_add_datetime());
            ((TextView) v.findViewById(R.id.tv_chat_text)).setText(obj
                    .getMes_text());

        } else if ((obj.getMes_type()).equals("C")) {
            try {
				T4FApplication mApplication = (T4FApplication) context.getApplication();
				
				String name = "";
				
				if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN)
					name = mApplication.getLoginUser().getUserName();
				else
					name = "You";
				
				
				if (name != null && name.equals(obj.getPla_fb_name())) {
				    View v = holder.ll_rightchatcontainer;
				    v.setVisibility(View.VISIBLE);

				    ((TextView) v.findViewById(R.id.tv_right_chat_text))
				            .setText(obj.getMes_text());
				    ((TextView) v.findViewById(R.id.tv_right_chat_time))
				            .setText(obj.getMes_add_datetime());

				} else {
				    View v = holder.ll_leftchatcontainer;
				    v.setVisibility(View.VISIBLE);
				    ((TextView) v.findViewById(R.id.tv_left_chat_name)).setText(obj
				            .getPla_fb_name() + " said:");
				    ((TextView) v.findViewById(R.id.tv_left_chat_text)).setText(obj
				            .getMes_text());
				    ((TextView) v.findViewById(R.id.tv_left_chat_time)).setText(obj
				            .getMes_add_datetime());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }

        return convertView;
    }

    static class ChatHolder {
        LinearLayout ll_leftchatcontainer;
        LinearLayout ll_rightchatcontainer;
        LinearLayout ll_OtherMesgType;
        ImageView ivMsgType;
    }

}
