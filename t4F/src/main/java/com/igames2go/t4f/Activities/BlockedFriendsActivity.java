
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class BlockedFriendsActivity extends Activity {

    private final String TAG = "T4F";
    private ListView listView;
    private FriendsListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_blockedfriends);

        listView = (ListView) findViewById(R.id.blockedfriends_list);

        loadBlockedFriendsList();
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    private void loadBlockedFriendsList() {
        LoadPlayers asynTask = new LoadPlayers(this, LoadPlayers.BLOCKED,
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

    private void handleData(GetPlayers blockedPlayerObj) {
        if (blockedPlayerObj != null) {
            ArrayList<PlayersDataObject> item = (ArrayList<PlayersDataObject>) blockedPlayerObj
                    .getBlocked();
            Log.e(TAG, "item size: " + item.size());
            if (!item.isEmpty()) {
                adapter = new FriendsListAdapter(this,
                        R.layout.layout_listadapter, item,
                        FriendsListAdapter.BLOCKEDFRIENDS);
                listView.setAdapter(adapter);
            } else {
                adapter = new FriendsListAdapter(this,
                        R.layout.layout_listadapter, null);
                listView.setAdapter(adapter);
                Log.e(TAG, "GetPlayerDataObject List is empty");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                alertDialogBuilder.setMessage(R.string.blockedfriendsemptylist);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
        // list.add("Add to game");

        if ((obj.getFavorite()).equals("1"))
            list.add(R.drawable.button_unfavorite);
        else
            list.add(R.drawable.button_set_as_favorite);
        list.add(R.drawable.button_unblock);
        list.add(R.drawable.button_cancel_wide);
        dialog = DialogUtil.createDialog_title_buttonList(this,
                obj.getPla_fb_name(), list, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        // if (s.equalsIgnoreCase(list.get(0))) {
                        // doAddToGame(obj);
                        // return;
                        // }
                        if (s==list.get(1)) {
                            doUnBlock(obj);
                            return;
                        }
                        if (s==list.get(0)) {
                            doFavoriteorUnFavorite(obj);
                            return;
                        }
                        if (s==list.get(2)) {
                            dialog.dismiss();
                            return;
                        }

                    }
                });
        dialog.show();

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
                        loadBlockedFriendsList();
                    }

                });
        asyncTask.execute();

    }

    protected void showAlreadyAddedDialog(final FriendsDataObject obj) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        // list.add("Delete from game");
        list.add(R.drawable.button_unblock);
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
                            doUnBlock(obj);
                            return;
                        }
                        if (s==list.get(2)) {
                            doSetAsFavorite(obj);
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
        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
//        Toast.makeText(this, "doAddToGame-" + obj.getPla_fb_name(),
//                Toast.LENGTH_LONG).show();
        // obj.setBitmap(prefetchedPictureCache.get(obj.getPla_fb_id()).getBitmap());
        ((T4FApplication) this.getApplication()).addPlayer(obj);
        this.startActivity(new Intent(BlockedFriendsActivity.this,
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

    public void doUnBlock(FriendsDataObject obj) {
        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
        String action = this.getResources().getString(R.string.action_unblock);

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
                        loadBlockedFriendsList();
                    }

                });

        asyncTask.execute();

    }

    public void doSetAsFavorite(FriendsDataObject obj) {
        // FriendsDataObject obj = (FriendsDataObject) v.getTag();
        String action = this.getResources().getString(R.string.action_favorite);

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
                        loadBlockedFriendsList();
                    }

                });
        asyncTask.execute();

    }

    public void onBack(View v)
    {
        BlockedFriendsActivity.this.finish();
    }

}
