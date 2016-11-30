
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.data.PlayersDataObject;
import com.igames2go.t4f.data.SearchLimit;
import com.igames2go.t4f.data.SearchPlayers;
import com.igames2go.t4f.tasks.CheckSearchLimit;
import com.igames2go.t4f.tasks.LoadSearchPlayers;
import com.igames2go.t4f.tasks.TagThePlayer;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.CustomButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class FindAFriendActivity extends Activity {

    private final String TAG = "T4F";
    private ListView listView;
    private FriendsListAdapter adapter = null;
    private EditText eText;
    private static int searchLimit = 3;
    private static int indicatorValue = 1;
    private Button searchbtn;
    private final static int STATE_SEARCH = 0;
    private final static int STATE_MORE = 1;
    private final static int STATE_NEW = 2;
    private static int state = STATE_SEARCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_findafriend);

        listView = (ListView) findViewById(R.id.searchresult_list);
        eText = (EditText) findViewById(R.id.searchtext);
        searchbtn = (Button) findViewById(R.id.searchfriendbtn);

        ((CustomButton) findViewById(R.id.search_friends)).setVisibility(View.GONE);

        getSearchLimit();

        indicatorValue = 1;
        state = STATE_SEARCH;
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    private void getSearchLimit() {
        CheckSearchLimit asyncTask = new CheckSearchLimit(
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        SearchLimit searchLimitObj = (SearchLimit) obj;
                        if (searchLimitObj != null) {
                            searchLimit = Integer.parseInt(searchLimitObj
                                    .getSearchLimit());
                            Log.e(TAG, "searchLimit:= " + searchLimit);
                        }

                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                    }

                }, getApplicationContext());

        asyncTask.execute();

    }

    public void showSearchResult(View v) {
        // Toast.makeText(getApplicationContext(), "getSearchResult",
        // 1000).show();
        String searchString = null;
        String setIndicator = "1";

        if (state == STATE_NEW) {
            searchbtn.setBackgroundResource(R.drawable.icon_search);
            indicatorValue = 1;
            state = STATE_SEARCH;
            eText.setText("");
            adapter = new FriendsListAdapter(this, R.layout.layout_listadapter,
                    null);
            listView.setAdapter(adapter);
        } else {
            if (eText != null) {
                searchString = eText.getText().toString();
                Log.e(TAG, "search str :" + searchString);
            }

            if (searchString != null && !(searchString.length() == 0)) {
                if (indicatorValue <= searchLimit) {
                    setIndicator = Integer.toString(indicatorValue);
                    searchString = searchString.toString().replaceAll("\n", "");
                    getSearchResult(searchString, setIndicator);

                }
                else {
                    Log.e(TAG, "indicator value issue");
                }
            } else {
                Log.e(TAG, "search String is empty");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                alertDialogBuilder.setTitle(R.string.searchstringempty);
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }

    private void getSearchResult(String searchString, String setIndicator) {

        LoadSearchPlayers asyncTask = new LoadSearchPlayers(this, searchString,
                setIndicator, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        Log.e(TAG, "loading complete");
                        int result = handleData((SearchPlayers) obj);

                        if (result > searchLimit) {
                            ++indicatorValue;
                            searchbtn
                                    .setBackgroundResource(R.drawable.icon_more);
                            state = STATE_MORE;
                        } else if (state == STATE_MORE) {
                            searchbtn
                                    .setBackgroundResource(R.drawable.icon_new);
                            state = STATE_NEW;
                        }
                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                    }

                });

        asyncTask.execute();

    }

    private int handleData(SearchPlayers searchPlayersobj) {

        if (searchPlayersobj != null) {
            ArrayList<PlayersDataObject> item = (ArrayList<PlayersDataObject>) searchPlayersobj
                    .getSearchPlayers();
            if (!item.isEmpty()) {
                Log.e(TAG, "SearchPlayers List not empty");
                adapter = new FriendsListAdapter(this,
                        R.layout.layout_listadapter, item, FriendsListAdapter.SEARCHFRIENDS);
                listView.setAdapter(adapter);
                return item.size();
            } else {
                Log.e(TAG, "SearchPlayers List is empty");
                adapter = new FriendsListAdapter(this,
                        R.layout.layout_listadapter, item);
                listView.setAdapter(adapter);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                alertDialogBuilder.setTitle(R.string.searchresultempty);
                alertDialogBuilder.setMessage(R.string.searchresultemptymsg);
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
            Log.e(TAG, "GetSearchPlayers Object is null");
        }

        return 0;

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

    public void doAddToGame(FriendsDataObject obj) {
        dialog.dismiss();
//        Toast.makeText(this, "doAddToGame-" + obj.getPla_fb_name(),
//                Toast.LENGTH_LONG).show();
        ((T4FApplication) this.getApplication()).addPlayer(obj);
        this.startActivity(new Intent(FindAFriendActivity.this,
                MeetTheChallenge.class));
        this.finish();
    }

    public void removeFromGame(FriendsDataObject obj) {
//        Toast.makeText(this, "remove-" + obj.getPla_fb_name(),
//                Toast.LENGTH_LONG).show();
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
                        showSearchResult(null);
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
                        showSearchResult(null);
                    }

                });
        asyncTask.execute();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, FriendPickActivty.class));
        FindAFriendActivity.this.finish();
    }

    public void onBack(View v)
    {
        startActivity(new Intent(this, FriendPickActivty.class));
        FindAFriendActivity.this.finish();
    }

    public void onRandom(View v)
    {
        startActivity(new Intent(this, RandomPickActivity.class));
        FindAFriendActivity.this.finish();
    }

    public void onSearch(View v)
    {
        startActivity(new Intent(this, FindAFriendActivity.class));
        FindAFriendActivity.this.finish();
    }

    public void onFavFriend(View v)
    {
        startActivity(new Intent(this, FavoriteFriendsActivity.class));
        FindAFriendActivity.this.finish();
    }

    public void onFBFriend(View v)
    {
        startActivity(new Intent(this, FacebookFriendsActivity.class));
        FindAFriendActivity.this.finish();
    }

}
