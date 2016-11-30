
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Activities.SelectCategory.SelectCategoryActivity;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.tasks.StartTheGame;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MeetTheChallenge extends AdActivity {

    private LinearLayout container1, container2, container3;

    private ArrayList<FriendsDataObject> players;

    private ImageLoader loader;

    private T4FApplication mApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meet_the_challenge_layout);
        
        mApplication = (T4FApplication) getApplication();
        loader = new ImageLoader(this);
        container1 = (LinearLayout) findViewById(R.id.container1);
        container2 = (LinearLayout) findViewById(R.id.container2);
        container3 = (LinearLayout) findViewById(R.id.container3);
        updateView();
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
    private void updateView() {
        players = mApplication.getPlayer();
        android.view.ViewGroup.LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        if (players == null || players.size() == 0) {
            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll.setLayoutParams(params);
            ll.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((ImageView) ll.findViewById(R.id.iv_pic))
                    .setImageResource(R.drawable.icon_additionsign);
            ((TextView) ll.findViewById(R.id.title_name))
                    .setText("Add a player");
            OnClickListener clickListener = new OnClickListener() {
                public void onClick(View v) {

                    startActivity(new Intent(MeetTheChallenge.this,
                            FriendPickActivty.class));
                    finish();

                }
            };

            ll.setOnClickListener(clickListener);
            container1.addView(ll);
            /////////////////////////////////////////////////////////
            LinearLayout ll2 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll2.setLayoutParams(params);
            ((TextView) ll2.findViewById(R.id.title_name)).setText("Add another Player");
            ((TextView) ll2.findViewById(R.id.title_name)).setTextColor(Color.GRAY);
            ll2.setEnabled(false);
            container2.addView(ll2);
            LinearLayout ll3 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll3.setLayoutParams(params);
            ((TextView) ll3.findViewById(R.id.title_name))
                    .setText("Add One More Player");
            ((TextView) ll3.findViewById(R.id.title_name)).setTextColor(Color.GRAY);
            ll3.setEnabled(false);
            container3.addView(ll3);
            ///////////////////////////////////////////////////////////
        } else if (players.size() == 1) {
            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll.setLayoutParams(params);
            ll.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            FriendsDataObject friend = players.get(0);
            String str = friend.getPla_fb_name().toString();
            ((TextView) ll.findViewById(R.id.title_name)).setText(str);
            ImageView imgView = (ImageView) ll.findViewById(R.id.iv_pic);
            
            loader.displayPlayerImage(players.get(0).getPla_fb_id(), imgView, false);
            
            ll.findViewById(R.id.btn_stat).setVisibility(View.VISIBLE);
            ll.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            final ImageView statsImgView = ((ImageView) ll
                    .findViewById(R.id.btn_stat));
            OnClickListener statisticsclickListener = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(statsImgView)) {
                        Intent intent = new Intent(MeetTheChallenge.this,
                                PlayerStatActivity.class);
                        intent.putExtra("player_id", players.get(0).getPla_id());
                        startActivity(intent);
                    }
                }
            };
            statsImgView.setOnClickListener(statisticsclickListener);

            final ImageView removePlayerView = ((ImageView) ll
                    .findViewById(R.id.btn_remove));
            OnClickListener removePlayerclickListener = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(removePlayerView)) {
                        mApplication
                                .removePlayer(players.get(0));
                        Intent intent = new Intent(MeetTheChallenge.this,
                                MeetTheChallenge.class);
                        startActivity(intent);
                        MeetTheChallenge.this.finish();
                    }
                }
            };
            removePlayerView.setOnClickListener(removePlayerclickListener);
            
            
            container1.addView(ll);
            LinearLayout ll1 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll1.setLayoutParams(params);
            ll1.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll1.findViewById(R.id.title_name))
                    .setText("Add another player");
            ((ImageView) ll1.findViewById(R.id.iv_pic))
                    .setImageResource(R.drawable.icon_additionsign);

            OnClickListener clickListener = new OnClickListener() {
                public void onClick(View v) {

                    startActivity(new Intent(MeetTheChallenge.this,
                            FriendPickActivty.class));
                    finish();

                }
            };

            ll1.setOnClickListener(clickListener);
           
            container2.addView(ll1);
            ////////////////////////////////////////////////////////
            LinearLayout ll3 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll3.setLayoutParams(params);
            ((TextView) ll3.findViewById(R.id.title_name))
                    .setText("Add One More Player");
            ((TextView) ll3.findViewById(R.id.title_name)).setTextColor(Color.GRAY);
            ll3.setEnabled(false);
            container3.addView(ll3);
            
            ////////////////////////////////////////////////////////
        } else if (players.size() == 2) {
            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll.setLayoutParams(params);
            ll.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll.findViewById(R.id.title_name)).setText(players
                    .get(0).getPla_fb_name());

            ImageView imgView = (ImageView) ll.findViewById(R.id.iv_pic);
            
            if(players.get(0).getPla_fb_id().contains(".png") || players.get(0).getPla_fb_id().contains(".jpg")){
            	loader.DisplayImage(getString(R.string.url_images)
                        + players.get(0).getPla_fb_id() , imgView, false);
            }else
            	loader.DisplayImage("https://graph.facebook.com/"
                        + players.get(0).getPla_fb_id() + "/picture", imgView, false);
            	
            ll.findViewById(R.id.btn_stat).setVisibility(View.VISIBLE);
            ll.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            final ImageView statsImgView1 = ((ImageView) ll
                    .findViewById(R.id.btn_stat));
            OnClickListener statisticsclickListener1 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(statsImgView1)) {
                        Intent intent = new Intent(MeetTheChallenge.this,
                                PlayerStatActivity.class);
                        intent.putExtra("player_id", players.get(0).getPla_id());
                        startActivity(intent);
                    }
                }
            };
            statsImgView1.setOnClickListener(statisticsclickListener1);

            final ImageView removePlayerView = ((ImageView) ll
                    .findViewById(R.id.btn_remove));
            OnClickListener removePlayerclickListener = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(removePlayerView)) {
                        mApplication
                                .removePlayer(players.get(0));
                        Intent intent = new Intent(MeetTheChallenge.this,
                                MeetTheChallenge.class);
                        startActivity(intent);
                        MeetTheChallenge.this.finish();
                    }
                }
            };
            removePlayerView.setOnClickListener(removePlayerclickListener);

            container1.addView(ll);
            
            LinearLayout ll2 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll2.setLayoutParams(params);
            ll2.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll2.findViewById(R.id.title_name)).setText(players.get(
                    1).getPla_fb_name());
            ImageView imgView2 = (ImageView) ll2.findViewById(R.id.iv_pic);
            
            if(players.get(1).getPla_fb_id().contains(".png") || players.get(1).getPla_fb_id().contains(".jpg")){
            	loader.DisplayImage(getString(R.string.url_images)
                        + players.get(1).getPla_fb_id() , imgView2, false);
            }else
            	loader.DisplayImage("https://graph.facebook.com/"
                    + players.get(1).getPla_fb_id() + "/picture", imgView2, false);
            
            ll2.findViewById(R.id.btn_stat).setVisibility(View.VISIBLE);
            ll2.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            final ImageView statsImgView2 = ((ImageView) ll2
                    .findViewById(R.id.btn_stat));
            OnClickListener statisticsclickListener2 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(statsImgView2)) {
                        Intent intent = new Intent(MeetTheChallenge.this,
                                PlayerStatActivity.class);
                        intent.putExtra("player_id", players.get(1).getPla_id());
                        startActivity(intent);
                    }
                }
            };
            statsImgView2.setOnClickListener(statisticsclickListener2);

            final ImageView removePlayerView2 = ((ImageView) ll2
                    .findViewById(R.id.btn_remove));
            OnClickListener removePlayerclickListener2 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(removePlayerView2)) {
                        mApplication
                                .removePlayer(players.get(1));
                        Intent intent = new Intent(MeetTheChallenge.this,
                                MeetTheChallenge.class);
                        startActivity(intent);
                        MeetTheChallenge.this.finish();
                    }
                }
            };
            removePlayerView2.setOnClickListener(removePlayerclickListener2);
            container2.addView(ll2);
            LinearLayout ll3 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll3.setLayoutParams(params);
            ll3.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll3.findViewById(R.id.title_name))
                    .setText("Add One More Player");
            ((ImageView) ll3.findViewById(R.id.iv_pic))
                    .setImageResource(R.drawable.icon_additionsign);

            OnClickListener clickListener = new OnClickListener() {
                public void onClick(View v) {

                    startActivity(new Intent(MeetTheChallenge.this,
                            FriendPickActivty.class));
                    finish();

                }
            };

            ll3.setOnClickListener(clickListener);
            container3.addView(ll3);

        } else if (players.size() == 3) {
            LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll.setLayoutParams(params);
            ll.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll.findViewById(R.id.title_name)).setText(players
                    .get(0).getPla_fb_name());
            ImageView imgView = (ImageView) ll.findViewById(R.id.iv_pic);
            
            if(players.get(0).getPla_fb_id().contains(".png") || players.get(0).getPla_fb_id().contains(".jpg")){
            	loader.DisplayImage(getString(R.string.url_images)
                        + players.get(0).getPla_fb_id() , imgView, false);
            }else
            	loader.DisplayImage("https://graph.facebook.com/"
                    + players.get(0).getPla_fb_id() + "/picture", imgView, false);
            
            ll.findViewById(R.id.btn_stat).setVisibility(View.VISIBLE);
            ll.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            final ImageView statsImgView1 = ((ImageView) ll
                    .findViewById(R.id.btn_stat));
            OnClickListener statisticsclickListener1 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(statsImgView1)) {
                        Intent intent = new Intent(MeetTheChallenge.this,
                                PlayerStatActivity.class);
                        intent.putExtra("player_id", players.get(0).getPla_id());
                        startActivity(intent);
                    }
                }
            };
            statsImgView1.setOnClickListener(statisticsclickListener1);

            final ImageView removePlayerView1 = ((ImageView) ll
                    .findViewById(R.id.btn_remove));
            OnClickListener removePlayerclickListener1 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(removePlayerView1)) {
                        mApplication
                                .removePlayer(players.get(0));
                        Intent intent = new Intent(MeetTheChallenge.this,
                                MeetTheChallenge.class);
                        startActivity(intent);
                        MeetTheChallenge.this.finish();
                    }
                }
            };
            removePlayerView1.setOnClickListener(removePlayerclickListener1);
            container1.addView(ll);
            LinearLayout ll2 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll2.setLayoutParams(params);
            ll2.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll2.findViewById(R.id.title_name)).setText(players.get(
                    1).getPla_fb_name());
            ImageView imgView2 = (ImageView) ll2.findViewById(R.id.iv_pic);
            
            if(players.get(1).getPla_fb_id().contains(".png") || players.get(1).getPla_fb_id().contains(".jpg")){
            	loader.DisplayImage(getString(R.string.url_images)
                        + players.get(1).getPla_fb_id() , imgView2, false);
            }else
            	loader.DisplayImage("https://graph.facebook.com/"
                    + players.get(1).getPla_fb_id() + "/picture", imgView2, false);
            
            ll2.findViewById(R.id.btn_stat).setVisibility(View.VISIBLE);
            ll2.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            final ImageView statsImgView2 = ((ImageView) ll2
                    .findViewById(R.id.btn_stat));
            OnClickListener statisticsclickListener2 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(statsImgView2)) {
                        Intent intent = new Intent(MeetTheChallenge.this,
                                PlayerStatActivity.class);
                        intent.putExtra("player_id", players.get(1).getPla_id());
                        startActivity(intent);
                    }
                }
            };
            statsImgView2.setOnClickListener(statisticsclickListener2);

            final ImageView removePlayerView2 = ((ImageView) ll2
                    .findViewById(R.id.btn_remove));
            OnClickListener removePlayerclickListener2 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(removePlayerView2)) {
                        mApplication
                                .removePlayer(players.get(1));
                        Intent intent = new Intent(MeetTheChallenge.this,
                                MeetTheChallenge.class);
                        startActivity(intent);
                        MeetTheChallenge.this.finish();
                    }
                }
            };
            removePlayerView2.setOnClickListener(removePlayerclickListener2);

            container2.addView(ll2);
            LinearLayout ll3 = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.meet_the_challenge_list_row, null);
            ll3.setLayoutParams(params);
            ll3.findViewById(R.id.imageframe).setVisibility(View.VISIBLE);
            ((TextView) ll3.findViewById(R.id.title_name)).setText(players.get(
                    2).getPla_fb_name());
            ImageView imgView3 = (ImageView) ll3.findViewById(R.id.iv_pic);
            
            if(players.get(2).getPla_fb_id().contains(".png") || players.get(2).getPla_fb_id().contains(".jpg")){
            	loader.DisplayImage(getString(R.string.url_images)
                        + players.get(2).getPla_fb_id() , imgView3, false);
            }else
            	loader.DisplayImage("https://graph.facebook.com/"
                    + players.get(2).getPla_fb_id() + "/picture", imgView3, false);
            
            ll3.findViewById(R.id.btn_stat).setVisibility(View.VISIBLE);
            ll3.findViewById(R.id.btn_remove).setVisibility(View.VISIBLE);
            final ImageView statsImgView3 = ((ImageView) ll3
                    .findViewById(R.id.btn_stat));
            OnClickListener statisticsclickListener3 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(statsImgView3)) {
                        Intent intent = new Intent(MeetTheChallenge.this,
                                PlayerStatActivity.class);
                        intent.putExtra("player_id", players.get(2).getPla_id());
                        startActivity(intent);
                    }
                }
            };
            statsImgView3.setOnClickListener(statisticsclickListener3);

            final ImageView removePlayerView3 = ((ImageView) ll3
                    .findViewById(R.id.btn_remove));
            OnClickListener removePlayerclickListener3 = new OnClickListener() {
                public void onClick(View v) {
                    if (v.equals(removePlayerView3)) {
                        mApplication
                                .removePlayer(players.get(2));
                        Intent intent = new Intent(MeetTheChallenge.this,
                                MeetTheChallenge.class);
                        startActivity(intent);
                        MeetTheChallenge.this.finish();
                    }
                }
            };
            removePlayerView3.setOnClickListener(removePlayerclickListener3);

            container3.addView(ll3);

        }
    }

    Dialog d;

    public void onCancel(View v1) {
        d = DialogUtil.createDialog_title_divider_message_two_btn(this, null,
                false, getString(R.string.cancel_dialog_msg_onnewgame), R.drawable.button_no,
                R.drawable.button_yes, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        if (s == R.drawable.button_yes) {
                            mApplication.resetPlayer();
                            d.dismiss();
                            MeetTheChallenge.this.finish();
                        } else
                            d.dismiss();
                    }
                }, -1);
        d.show();

    }

    public void onHelpClick(View v) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void startGame(View v) {
    	String str = "";
    	/*
    	if(mApplication.play_mode == T4FApplication.PLAY_MODE_FB_LOGIN){
    		GraphUser user = mApplication.getFacebookUser();
    		str += user.getName()+"\n";
    	}else 
    	*/
    	if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_LOGIN){
    		str += mApplication.getLoginUser().getUserName()+"\n";
    	}

        ArrayList<String> playerIds = new ArrayList<String>();

        if (players != null || players.size() != 0) {
            for (int i = 0; i < players.size(); i++) {
                str = str + players.get(i).getPla_fb_name() + "\n";
                playerIds.add(players.get(i).getPla_id());
            }
        }

        Intent intent = new Intent(getApplicationContext(), SelectCategoryActivity.class);
        intent.putStringArrayListExtra("playerIds", playerIds);
        intent.putExtra("message", str);
        startActivity(intent);

        MeetTheChallenge.this.finish();
    }

    @Override
    public void onBackPressed() {
    	onCancel(new View(getApplicationContext()));
        return;
    }
}
