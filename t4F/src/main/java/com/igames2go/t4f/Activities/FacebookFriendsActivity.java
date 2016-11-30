
package com.igames2go.t4f.Activities;

import com.facebook.AccessToken;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FacebookFriendsDataObject;
import com.igames2go.t4f.data.FacebookFriendsList;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.listener.FBLoadListener;
import com.igames2go.t4f.tasks.TagThePlayer;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.CustomButton;
import com.igames2go.t4f.view.SideBarView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class FacebookFriendsActivity extends FragmentActivity {

    private final String TAG = "T4F";
    private ListView listView;
    private FacebookFriendsListAdapter adapter = null;
    private SideBarView sidebar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_friend);
        //String access_token = Session.getActiveSession().getAccessToken();
        //url = "https://graph.facebook.com/fql?q=SELECT%20uid,%20name%20FROM%20user%20WHERE%20uid%20IN%20(SELECT%20uid2%20FROM%20friend%20WHERE%20uid1%20=%20me())%20ORDER%20BY%20name&access_token="
        //        + access_token;

        listView = (ListView) findViewById(R.id.fbfriends_list);
        sidebar = (SideBarView) findViewById(R.id.letterSideBarView);
        ((CustomButton) findViewById(R.id.fb_friends)).setVisibility(View.GONE);

        if(AccessToken.getCurrentAccessToken() != null)
        	loadFacebookFriends();
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }
    FBLoadListener fbListener = new FBLoadListener() {
    	public void onFBLogin() {};
    	public void onFBLogout() {
    		
    	};
    	public void onFBDone() {
    		handleData((FacebookFriendsList) FacebookManager.fbFriends);
    	};
    };
    private void loadFacebookFriends() {
    	FacebookManager.getFriends(fbListener);
    }

    ArrayList<FacebookFriendsDataObject> items = new ArrayList<FacebookFriendsDataObject>();
    public  HashMap<String, Integer> map = new HashMap<String, Integer>();
    private void handleData(FacebookFriendsList fbFriendsObj) {

        if (fbFriendsObj != null) {
            items = (ArrayList<FacebookFriendsDataObject>) fbFriendsObj
                    .getFacebookFriends();
            
            if (!items.isEmpty()) {
            	FacebookFriendsDataObject obj = null;
            	for(int i=0 ; i<items.size(); i++) {
            		obj = items.get(i);
            		if(!map.containsKey(""+obj.getName().charAt(0))){
            			map.put(""+obj.getName().charAt(0), i);
            		}
            	}
                adapter = new FacebookFriendsListAdapter(this,
                        R.layout.layout_facebooklistadapter, items);
                listView.setAdapter(adapter);

            } else {
                Log.e(TAG, "FacebookFriendsDataObject List is empty");
                adapter = new FacebookFriendsListAdapter(this,
                        R.layout.layout_facebooklistadapter, null);
                listView.setAdapter(adapter);
            }
            // listView.setOnItemClickListener(itemClickedListener );
            listView.setTag(map);
            sidebar.setListView(listView);
        } else {
            Log.e(TAG, "FacebookFriendsList Object is null");
        }
    }

    private Dialog dialog;

    public void onOptionButtonClick(final View v) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                int position = (Integer) v
                        .getTag(R.id.btn_addtogame);
                FacebookFriendsDataObject obj1 = adapter.items.get(position);
               final  FriendsDataObject obj = obj1.creatFriendDataObject();
                T4FApplication application = (T4FApplication) getApplication();

                if (obj != null && obj.getPla_id() != null)
                {
                    if ((obj.getPla_id()).equals("0"))
                    {
                    	final ArrayList<Integer> list = new ArrayList<Integer>();
                        list.add(R.drawable.button_invite_to_app );
                        list.add(R.drawable.button_cancel_wide);
                        dialog = DialogUtil.createDialog_title_buttonList(FacebookFriendsActivity.this,
                                obj.getPla_fb_name(), list, new ButtonClickListener() {

                                    @Override
                                    public void onButtonClick(int s)
                                            throws NullPointerException {
                                    	dialog.dismiss();
                                        if (s == list.get(0)) {
                                        	 DialogUtil.createDialog_title_bitmap_message(FacebookFriendsActivity.this, getString(R.string.invite_dialog_msg), null, "Facebook invite sent to "+obj.getPla_fb_name(), R.drawable.button_ok,new ButtonClickListener() {
												
												@Override
												public void onButtonClick(int s) throws NullPointerException {
//													dialog.dismiss();
													
												}
											} );
//                                        	dialog.show();
                                        }
                                            
                                            

                                    }
                                });
                        dialog.show();

                    }
                    else
                    {
                        if (application.isAlreadyAdded(obj)) {
                            showAlreadyAddedDialog(obj);
                        } else {
                            showNotAddedDialog(obj);
                        }
                    }
                }
            }
        });

    }

    protected void showNotAddedDialog(final FriendsDataObject obj) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(R.drawable.button_add_to_game );
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
                        if (s == list.get(0)) {
                            doAddToGame(obj);
                            return;
                        }
                        if (s == list.get(1)) {
                            doBlockorUnblock(obj);
                            return;
                        }
                        if (s == list.get(2)) {
                            doFavoriteorUnFavorite(obj);
                            return;
                        }
                        if (s == list.get(3)) {
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
                        if (s == list.get(0)) {
                            removeFromGame(obj);
                            return;
                        }
                        if (s == list.get(1)) {
                            doBlockorUnblock(obj);
                            return;
                        }
                        if (s == list.get(2)) {
                            doFavoriteorUnFavorite(obj);
                            return;
                        }
                        if (s == list.get(3)) {
                            dialog.dismiss();
                            return;
                        }

                    }
                });
        dialog.show();
    }

    public void doAddToGame(FriendsDataObject obj) {
        dialog.dismiss();

        ((T4FApplication) this.getApplication()).addPlayer(obj);
        this.startActivity(new Intent(FacebookFriendsActivity.this,
                MeetTheChallenge.class));
        this.finish();
    }

    public void removeFromGame(FriendsDataObject obj) {

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
                        loadFacebookFriends();
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
                        loadFacebookFriends();
                    }

                });
        asyncTask.execute();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FriendPickActivty.class));
        FacebookFriendsActivity.this.finish();
    }

    public void onBack(View v)
    {
        startActivity(new Intent(this, FriendPickActivty.class));
        FacebookFriendsActivity.this.finish();
    }

    public void onRandom(View v)
    {
        startActivity(new Intent(this, RandomPickActivity.class));
        FacebookFriendsActivity.this.finish();
    }

    public void onSearch(View v)
    {
        startActivity(new Intent(this, FindAFriendActivity.class));
        FacebookFriendsActivity.this.finish();
    }

    public void onFavFriend(View v)
    {
        startActivity(new Intent(this, FavoriteFriendsActivity.class));
        FacebookFriendsActivity.this.finish();
    }

    public void onFBFriend(View v)
    {
        startActivity(new Intent(this, FacebookFriendsActivity.class));
        FacebookFriendsActivity.this.finish();
    }
}
