/*
 * 
 */

package com.igames2go.t4f.utils;

import com.igames2go.t4f.Activities.DialogActivity;
import com.igames2go.t4f.Activities.GameAlertDialogActivity;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.GamesDataObject;
import com.igames2go.t4f.view.CustomButton;
import com.igames2go.t4f.view.CustomImageView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogUtil {

    /**
     * Creates the dialog_title_divider_message_two_btn. The method can be use
     * for different dialog scenario Page 11,49 - pass valid and positive values
     * in all parameters Page 30,76 - showDialog = false and neg_button = null
     * or empty. Every thing else valid. Page 47,48 - showDialog = false message
     * null or empty. similarly 81,99,100
     * 
     * @param context the context
     * @param title the title
     * @param showDivider TODO
     * @param message the message
     * @param neg_button the neg_button
     * @param pos_button the pos_button
     * @param lines TODO
     * @param negativeBtnListener the negative btn listener
     * @param positiveBtnListener the positive btn listener
     * @return the dialog
     */
    public static Dialog createDialog_title_divider_message_two_btn(
            Activity context, String title, boolean showDivider,
            String message, final int neg_button, final int pos_button,
            final ButtonClickListener btnListener, int lines) {

        // dialog.setContentView(R.layout.dialog_title_divider_message_two_btn);
        final View view = context.getLayoutInflater().inflate(
                R.layout.dialog_title_divider_message_two_btn, null);

        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        else {
            (view.findViewById(android.R.id.text1)).setVisibility(View.GONE);

        }
        if (showDivider == false) {
            view.findViewById(R.id.divider).setVisibility(View.GONE);
        }
        ((TextView) view.findViewById(android.R.id.text2)).setText(message);
        if(lines >0) {
        	((TextView) view.findViewById(android.R.id.text2)).setMinLines(lines);
        }
        if (pos_button != -1)
        {
            ((Button) view.findViewById(android.R.id.button2))
                    .setBackgroundResource(pos_button);
            ((Button) view.findViewById(android.R.id.button2))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            btnListener.onButtonClick(pos_button);

                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            view.findViewById(android.R.id.button2).setVisibility(View.GONE);
        }

        if (neg_button != -1) {
            ((Button) view.findViewById(android.R.id.button1))
                    .setBackgroundResource(neg_button);
            ((Button) view.findViewById(android.R.id.button1))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            btnListener.onButtonClick(neg_button);

                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            view.findViewById(android.R.id.button1).setVisibility(View.GONE);
        }

        CustomDialog dialog = new CustomDialog(context, view);
        return dialog;
    }

    /**
     * Use this method to create login select dialog.
     * 
     **/
    public static Dialog createDialog_title_divider_message_with_image_two_btn(
            Activity context, String title, boolean showDivider,
            String message, String imageName, final int neg_button, final int pos_button,
            final ButtonClickListener btnListener, int lines) {

        // dialog.setContentView(R.layout.dialog_title_divider_message_two_btn);
    	
    	ImageLoader loader = new ImageLoader(context);
    	
        final View view = context.getLayoutInflater().inflate(
                R.layout.dialog_title_divider_message_with_image_two_btn, null);

        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        else {
            (view.findViewById(android.R.id.text1)).setVisibility(View.GONE);
        }
        
        ImageView v = (ImageView)view.findViewById(R.id.image1);
        loader.displayPlayerImage(imageName, v, false);
        
        if (showDivider == false) {
            view.findViewById(R.id.divider).setVisibility(View.GONE);
        }
        ((TextView) view.findViewById(android.R.id.text2)).setText(message);
        if(lines >0) {
        	((TextView) view.findViewById(android.R.id.text2)).setMinLines(lines);
        }
        if (pos_button != -1)
        {
            ((Button) view.findViewById(android.R.id.button2))
                    .setBackgroundResource(pos_button);
            ((Button) view.findViewById(android.R.id.button2))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            btnListener.onButtonClick(pos_button);

                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            view.findViewById(android.R.id.button2).setVisibility(View.GONE);
        }

        if (neg_button != -1) {
            ((Button) view.findViewById(android.R.id.button1))
                    .setBackgroundResource(neg_button);
            ((Button) view.findViewById(android.R.id.button1))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            btnListener.onButtonClick(neg_button);

                            // TODO Auto-generated method stub

                        }
                    });
        } else {
            view.findViewById(android.R.id.button1).setVisibility(View.GONE);
        }

        CustomDialog dialog = new CustomDialog(context, view);
        return dialog;
    }
    
    /**
     * Use this method to create login select dialog.
     * 
     * @param context
     * @param title
     * @param strings
     * @param listener
     * @return
     */
    public static Dialog createDialog_photo_select(Activity context,
            String title, ArrayList<Integer> strings,
            final ButtonClickListener listener) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_title_buttonlist,
                null);
        view.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        else {
            (view.findViewById(android.R.id.text1)).setVisibility(View.GONE);

        }
        LinearLayout container = (LinearLayout) view
                .findViewById(R.id.container);
        if (container != null && strings != null) {
            Button btn;
            View v;
            for (final Integer string : strings) {
                btn = (Button) inflater.inflate(
                        R.layout.inflatable_dialog_button, null);
                btn.setBackgroundResource(string);
                v = new View(context);
                v.setLayoutParams(new LinearLayout.LayoutParams(1, 20));
                btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onButtonClick(string);

                    }
                });
                container.addView(v);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                container.addView(btn);
            }
        }

        CustomDialog dialog = new CustomDialog(context, view);
        return dialog;

    }
    
    /**
     * Use this method to create Button list dialog as in page 10.
     * 
     * @param context
     * @param title
     * @param strings
     * @param listener
     * @return
     */
    public static Dialog createDialog_title_buttonList(Activity context,
            String title, ArrayList<Integer> strings,
            final ButtonClickListener listener) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_title_buttonlist,
                null);
        //view.findViewById(R.id.divider).setVisibility(View.GONE);
        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        else {
            (view.findViewById(android.R.id.text1)).setVisibility(View.GONE);

        }
        LinearLayout container = (LinearLayout) view
                .findViewById(R.id.container);
        if (container != null && strings != null) {
            Button btn;
            View v;
            for (final Integer string : strings) {
                btn = (Button) inflater.inflate(
                        R.layout.inflatable_dialog_button, null);
                btn.setBackgroundResource(string);
                v = new View(context);
                v.setLayoutParams(new LinearLayout.LayoutParams(1, 20));
                btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onButtonClick(string);

                    }
                });
                container.addView(v);
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                container.addView(btn);
            }
        }

        CustomDialog dialog = new CustomDialog(context, view);
        return dialog;

    }

    public static ButtonClickListener mListener;
    /*
     * Use to create "Did you know dialog" page number 76. Pass bitmap parameter
     * null, in case you don't want the image.
     */
    /**
     * @param context
     * @param title
     * @param image
     * @param message
     * @param btn_text
     * @param listener
     * @return
     */
    public static void createDialog_title_bitmap_message(Activity context,
            String title, String image, String message, final int btn_text,
            final ButtonClickListener listener) {
    	Intent intent = new Intent(context,DialogActivity.class);
    	intent.putExtra("title", title);
    	if(image != null)
    		intent.putExtra("image", image);
    	intent.putExtra("message", message);
    	intent.putExtra("btn_text", btn_text);
    	mListener = listener;
    	context.startActivityForResult(intent, 77);
    }
    
    public static void createDialog_not_entered(Activity context,
            String title, String image, String message, final int btn_text,
            final ButtonClickListener listener) {
    	Intent intent = new Intent(context,DialogActivity.class);
    	intent.putExtra("title", title);
    	if(image != null)
    		intent.putExtra("image", image);
    	intent.putExtra("message", message);
    	intent.putExtra("btn_text", btn_text);
    	intent.putExtra("not_entered", 1);
    	mListener = listener;
    	context.startActivityForResult(intent, 77);
    }
    
    public static void createDialog_title_bitmap_message1(Activity context,
            String title, String image, String message, final int btn_text,
            final ButtonClickListener listener) {
    	Intent intent = new Intent(context,DialogActivity.class);
    	intent.putExtra("title", title);
    	if(image != null)
    		intent.putExtra("image", image);
    	intent.putExtra("message", message);
    	intent.putExtra("btn_text", btn_text);
    	mListener = listener;
    	context.startActivity(intent);
    	
    }
    
    public static void createDialog_title_bitmap_message_2_button(Activity context,
            String title, String image, String message, 
            final int btn_text,final int btn_text2,GamesDataObject obj,int type,
            final ButtonClickListener listener) {
    	Intent intent = new Intent(context,GameAlertDialogActivity.class);
    	intent.putExtra("title", title);
    	if(image != null)
    		intent.putExtra("image", image);
    	intent.putExtra("message", message);
    	intent.putExtra("btn_text", btn_text);
    	intent.putExtra("btn_text2", btn_text2);
    	intent.putExtra("GamesDataObject", obj);
    	intent.putExtra("type", type);
    	mListener = listener;
    	context.startActivityForResult(intent, type);
    	
    }

    /**
     * Use this to create invite dialog. Page 34.
     * 
     * @param context
     * @param title
     * @param listener
     * @return
     */
    public static Dialog createDialog_invite(Activity context, String title,
            final ButtonClickListener listener) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_title_buttonlist,
                null);
        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        else {
            (view.findViewById(android.R.id.text1)).setVisibility(View.GONE);
        }

        LinearLayout container = (LinearLayout) view
                .findViewById(R.id.container);

        CustomImageView invite = new CustomImageView(context);
        android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                190, 46);
        params.setMargins(0, 20, 0, 40);
        invite.setLayoutParams(params);
        invite.setBackgroundResource(R.drawable.button_invite_to_app);
        invite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onButtonClick(R.drawable.button_invite_to_app);

            }
        });
        container.addView(invite);

        CustomButton cancel = new CustomButton(context);

        cancel.setLayoutParams(new LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
        cancel.setBackgroundResource(R.drawable.button_cancel);
        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        container.addView(cancel);
        dialog = new CustomDialog(context, view);
        return dialog;
    }

    public static Dialog createDialog_Ready_to_go(Activity context,
            String title, boolean showSelectAgain,
            final ButtonClickListener listener) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_ready_to_go, null);
        view.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
        if (showSelectAgain) {
            view.findViewById(R.id.btn_sel_again).setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_sel_again).setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                        }
                    });
        }
        view.findViewById(R.id.btn_go).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listener.onButtonClick(R.id.btn_go);

                    }
                });
        dialog = new CustomDialog(context, view);
        return dialog;
    }

    static CustomDialog dialog;
}
