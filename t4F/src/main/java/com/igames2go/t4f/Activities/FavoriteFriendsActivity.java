
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
//import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteFriendsActivity extends Activity implements
        OnItemClickListener {

    private final String TAG = "T4F";
    private ListView listView;
    private FriendsListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_favfriends);
        listView = (ListView) findViewById(R.id.friends_list);
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(this);

        ((CustomButton) findViewById(R.id.favorite_friends)).setVisibility(View.GONE);

        loadFavoriteFriendsList();
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    private void loadFavoriteFriendsList() {
        LoadPlayers asynTask = new LoadPlayers(this, LoadPlayers.FAVORITES,
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

    private ArrayList<PlayersDataObject> items = new ArrayList<PlayersDataObject>();

    private void handleData(GetPlayers favPlayerObj) {

        if (favPlayerObj != null) {
            items = (ArrayList<PlayersDataObject>) favPlayerObj
                    .getFavorites();
            if (!items.isEmpty()) {
                adapter = new FriendsListAdapter(this,
                        R.layout.layout_listadapter, items,
                        FriendsListAdapter.FAVORITEFRIENDS);
                listView.setAdapter(adapter);

            } else {
                Log.e(TAG, "GetPlayerDataObject List is empty");
                adapter = new FriendsListAdapter(this,
                        R.layout.layout_listadapter, null);
                listView.setAdapter(adapter);
            }
            // listView.setOnItemClickListener(itemClickedListener );
        } else {
            Log.e(TAG, "GetPlayers Object is null");
        }

    }

    private Dialog dialog;

    public void onOptionButtonClick(final View v) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try {
                    int position = (Integer) v
                            .getTag(R.id.btn_addtogame);
                    FriendsDataObject obj = adapter.items.get(position).creatFriendDataObject();
                    T4FApplication application = (T4FApplication) getApplication();
                    if (application.isAlreadyAdded(obj)) {
                        showAlreadyAddedDialog(obj);
                    } else {
                        showNotAddedDialog(obj);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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

        list.add(R.drawable.button_cancel_wide);
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
        list.add(R.drawable.button_cancel_wide);
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
                        // loadFacebookFriends(url);
                        loadFavoriteFriendsList();
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
                        // loadFacebookFriends(url);
                        loadFavoriteFriendsList();
                    }

                });
        asyncTask.execute();

    }

    public void doAddToGame(FriendsDataObject obj) {
        dialog.dismiss();
        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
        // Toast.makeText(this, "doAddToGame-"+obj.getPla_fb_name(),
        // Toast.LENGTH_LONG).show();
        // obj.setBitmap(prefetchedPictureCache.get(obj.getPla_fb_id()).getBitmap());
        ((T4FApplication) this.getApplication()).addPlayer(obj);
        this.startActivity(new Intent(FavoriteFriendsActivity.this,
                MeetTheChallenge.class));
        this.finish();
    }

    public void removeFromGame(FriendsDataObject obj) {

        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
//        Toast.makeText(this, "remove-" + obj.getPla_fb_name(),
//                Toast.LENGTH_LONG).show();
        // obj.setBitmap(prefetchedPictureCache.get(obj.getPla_fb_id()).getBitmap());
        ((T4FApplication) this.getApplication()).removePlayer(obj);
        // this.startActivity(new Intent(FavoriteFriendsActivity.this,
        // MeetTheChallenge.class));
        // this.finish();
        dialog.dismiss();
    }

    public void doSetAsBlocked(FriendsDataObject obj) {
        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
        String action = this.getResources().getString(R.string.action_block);

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

                        // ((ViewGroup) dialoglayout.getParent())
                        // .removeView(dialoglayout);
                        //
                        // if (alertDialog != null) {
                        // alertDialog.dismiss();
                        // }
                        dialog.dismiss();
                        loadFavoriteFriendsList();
                    }

                });

        asyncTask.execute();

    }

    public void doUnfavorite(FriendsDataObject obj) {
        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
        String action = this.getResources().getString(
                R.string.action_unfavorite);

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

                        // ((ViewGroup) dialoglayout.getParent())
                        // .removeView(dialoglayout);
                        //
                        // if (alertDialog != null) {
                        // alertDialog.dismiss();
                        // }
                        dialog.dismiss();
                        loadFavoriteFriendsList();
                    }

                });
        asyncTask.execute();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        Toast.makeText(getApplicationContext(), "Item clicked", 1000).show();
        onOptionButtonClick(arg1);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FriendPickActivty.class));
        FavoriteFriendsActivity.this.finish();
    }

    // public void doCancelDialog(View v) {
    // ((ViewGroup) dialoglayout.getParent()).removeView(dialoglayout);
    // if (alertDialog != null) {
    // alertDialog.dismiss();
    // }
    // }

    public void onBack(View v)
    {
        startActivity(new Intent(this, FriendPickActivty.class));
        FavoriteFriendsActivity.this.finish();
    }

    public void onRandom(View v)
    {
        startActivity(new Intent(this, RandomPickActivity.class));
        FavoriteFriendsActivity.this.finish();
    }

    public void onSearch(View v)
    {
        startActivity(new Intent(this, FindAFriendActivity.class));
        FavoriteFriendsActivity.this.finish();
    }

    public void onFavFriend(View v)
    {
        startActivity(new Intent(this, FavoriteFriendsActivity.class));
        FavoriteFriendsActivity.this.finish();
    }

    public void onFBFriend(View v)
    {
        startActivity(new Intent(this, FacebookFriendsActivity.class));
        FavoriteFriendsActivity.this.finish();
    }

}
