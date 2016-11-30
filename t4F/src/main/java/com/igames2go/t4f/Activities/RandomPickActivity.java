
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.data.GetPlayers;
import com.igames2go.t4f.data.PlayersDataObject;
import com.igames2go.t4f.tasks.LoadPlayers;
import com.igames2go.t4f.tasks.TagThePlayer;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.CustomButton;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RandomPickActivity extends Activity {

    private final String TAG = "T4F";

    private TextView tv_rdmpickinfo;

    private String randomPlayerId = null;

    private ImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_random_pick);
        loader = new ImageLoader(this);
        tv_rdmpickinfo = (TextView) findViewById(R.id.tv_rdm_pick_info);

        ((CustomButton) findViewById(R.id.question_mark)).setVisibility(View.GONE);

        getRandomPlayer();
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    private void getRandomPlayer() {
        LoadPlayers asynTask = new LoadPlayers(this, LoadPlayers.RANDOM,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handleData((GetPlayers) obj);
                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                    }

                });
        asynTask.execute();

    }

    private void handleData(GetPlayers randomPlayerObj) {
        if (randomPlayerObj != null) {
            ArrayList<PlayersDataObject> item = (ArrayList<PlayersDataObject>) randomPlayerObj
                    .getRandom();
            Log.e(TAG, "item size: " + item.size());
            if (!item.isEmpty()) {
                randomPlayerId = item.get(0).getPla_id();
                String rdmPickInfo = null;
                rdmPickInfo = item.get(0).getPla_fb_name() + "\n"
                        + item.get(0).getPla_location();
                tv_rdmpickinfo.setText(rdmPickInfo);
                FriendsDataObject obj = item.get(0)
                        .creatFriendDataObject();
                ((Button) findViewById(R.id.optionButton)).setTag(obj);
                final String pla_fb_id = item.get(0).getPla_fb_id();
                final ImageView pic = (ImageView) findViewById(R.id.iv_pic);
                
                if(pla_fb_id.contains(".png") || pla_fb_id.contains(".jpg")){
                	loader.DisplayImage(getString(R.string.url_images)
                            + pla_fb_id , pic, false);
                }else
                	loader.DisplayImage("https://graph.facebook.com/"
                        + pla_fb_id + "/picture", pic, false);
                
                
                if(item.get(0).getFavorite().equalsIgnoreCase("1")) {
                	findViewById(R.id.iv_fav).setVisibility(View.VISIBLE);
                }
                else
                	findViewById(R.id.iv_fav).setVisibility(View.INVISIBLE);
                if(TextUtils.isEmpty(item.get(0).getAward_badge_image())==false) {
                	((ImageView)findViewById(R.id.iv_award)).setVisibility(View.VISIBLE);
                	loader.DisplayImage(
                			getString(R.string.url_images) + item.get(0).getAward_badge_image(),
                			((ImageView)findViewById(R.id.iv_award)), false);
                }
                else
                	((ImageView)findViewById(R.id.iv_award)).setVisibility(View.INVISIBLE);
                
            } else {
                Log.e(TAG, "Random search is empty");
            }

        } else {
            Log.e(TAG, "GetPlayers Object is null");
        }

    }

    private Dialog dialog;

    public void onOptionButtonClick(final View v) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                FriendsDataObject obj = (FriendsDataObject) v.getTag();
                T4FApplication application = (T4FApplication) getApplication();
                if (application.isAlreadyAdded(obj)) {
                    showAlreadyAddedDialog(obj);
                } else {
                    showNotAddedDialog(obj);
                }

            }
        });

    }

    protected void showNotAddedDialog(final FriendsDataObject obj) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.button_add_to_game);

        if ((obj.getBlocked()).equals("1"))
            list.add(R.drawable.button_unblock);
        else
            list.add(R.drawable.button_set_as_blocked);

        if ((obj.getFavorite()).equals("1"))
            list.add(R.drawable.button_unfavorite);
        else
            list.add(R.drawable.button_set_as_favorite);

        list.add(R.drawable.button_cancel);
        dialog = DialogUtil.createDialog_title_buttonList(this,
                obj.getPla_fb_name(), list, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        if (s==list.get(0)) {
                            doAddToGame(obj);
                            return;
                        }
                        if (s==list.get(1)) {
                            doBlockorUnblock(obj);
                            return;
                        }
                        if (s==list.get(2)) {
                            doFavoriteorUnFavorite(obj);
                            return;
                        }
                        if (s==list.get(3)) {
                            dialog.dismiss();
                            return;
                        }

                    }
                });
        dialog.show();

    }

    protected void showAlreadyAddedDialog(final FriendsDataObject obj) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.button_delete_from_game);

        if ((obj.getBlocked()).equals("1"))
            list.add(R.drawable.button_unblock);
        else
            list.add(R.drawable.button_set_as_blocked);

        if ((obj.getFavorite()).equals("1"))
            list.add(R.drawable.button_unfavorite);
        else
            list.add(R.drawable.button_set_as_favorite);

        list.add(R.drawable.button_cancel);
        dialog = DialogUtil.createDialog_title_buttonList(this,
                obj.getPla_fb_name(), list, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        if (s==list.get(0)) {
                            removeFromGame(obj);
                            return;
                        }
                        if (s==list.get(1)) {
                            doBlockorUnblock(obj);
                            return;
                        }
                        if (s==list.get(2)) {
                            doFavoriteorUnFavorite(obj);
                            return;
                        }
                        if (s==list.get(3)) {
                            dialog.dismiss();
                            return;
                        }

                    }
                });
        dialog.show();
    }

    public void doAddToGame(FriendsDataObject obj) {
        dialog.dismiss();
        // Toast.makeText(this, "doAddToGame-"+obj.getPla_fb_name(),
        // Toast.LENGTH_LONG).show();
        ((T4FApplication) this.getApplication()).addPlayer(obj);
        this.startActivity(new Intent(RandomPickActivity.this,
                MeetTheChallenge.class));
        this.finish();
    }

    public void removeFromGame(FriendsDataObject obj) {
        // Toast.makeText(this, "remove-"+obj.getPla_fb_name(),
        // Toast.LENGTH_LONG).show();
        ((T4FApplication) this.getApplication()).removePlayer(obj);
        dialog.dismiss();
    }

    public void doBlockorUnblock(FriendsDataObject obj) {
        String action = null;

        if ((obj.getBlocked()).equals("1"))
            action = this.getResources().getString(R.string.action_unblock);
        else
            action = this.getResources().getString(R.string.action_block);

        TagThePlayer asyncTask = new TagThePlayer(this, obj.getPla_fb_id(),
                obj.getPla_fb_name(), obj.getPla_id(), action, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {

                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                        dialog.dismiss();

                    }

                });

        asyncTask.execute();

    }

    public void doFavoriteorUnFavorite(FriendsDataObject obj) {
        String action = null;

        if ((obj.getFavorite()).equals("1"))
            action = this.getResources().getString(R.string.action_unfavorite);
        else
            action = this.getResources().getString(R.string.action_favorite);

        TagThePlayer asyncTask = new TagThePlayer(this, obj.getPla_fb_id(),
                obj.getPla_fb_name(), obj.getPla_id(), action, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {

                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                        dialog.dismiss();

                    }

                });
        asyncTask.execute();

    }

    public void getRandom(View v) {
        getRandomPlayer();
    }

    public void showStat(View v) {
        if ((randomPlayerId != null) && !(randomPlayerId.length() == 0)) {
            Intent intent = new Intent(RandomPickActivity.this,
                    PlayerStatActivity.class);
            intent.putExtra("player_id", randomPlayerId);
            startActivity(intent);
        } else
            Log.e(TAG, "Pla id is null or empty");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FriendPickActivty.class));
        RandomPickActivity.this.finish();

    }

    public void onBack(View v)
    {
        startActivity(new Intent(this, FriendPickActivty.class));
        RandomPickActivity.this.finish();
    }

    public void onRandom(View v)
    {
        startActivity(new Intent(this, RandomPickActivity.class));
        RandomPickActivity.this.finish();
    }

    public void onSearch(View v)
    {
        startActivity(new Intent(this, FindAFriendActivity.class));
        RandomPickActivity.this.finish();
    }

    public void onFavFriend(View v)
    {
        startActivity(new Intent(this, FavoriteFriendsActivity.class));
        RandomPickActivity.this.finish();
    }

    public void onFBFriend(View v)
    {
        startActivity(new Intent(this, FacebookFriendsActivity.class));
        RandomPickActivity.this.finish();
    }

}
