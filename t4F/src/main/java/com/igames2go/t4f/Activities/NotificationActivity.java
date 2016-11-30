
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SaveSettingsNotificationsResult;
import com.igames2go.t4f.data.SettingsNotifications;
import com.igames2go.t4f.data.SettingsNotificationsDataObject;
import com.igames2go.t4f.tasks.LoadSettingsNotifications;
import com.igames2go.t4f.tasks.SaveSettingsNotifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationActivity extends Activity {

    private final String TAG = "T4F";

    private CheckBox cb_notification;
    private CheckBox cb_game;
    private CheckBox cb_yourturn;
    private CheckBox cb_any1turn;
    private CheckBox cb_chatmsg;
    private LinearLayout ll_notificationtypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notification);
        initView();

        loadSettingsNotification();
    }

    private void initView() {
        cb_notification = (CheckBox) findViewById(R.id.cb_notification);
        cb_game = (CheckBox) findViewById(R.id.cb_game);
        cb_yourturn = (CheckBox) findViewById(R.id.cb_yourturn);
        cb_any1turn = (CheckBox) findViewById(R.id.cb_any1turn);
        cb_chatmsg = (CheckBox) findViewById(R.id.cb_chatmsg);
        ll_notificationtypes = (LinearLayout) findViewById(R.id.ll_typesofnotification);

        cb_notification.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    ll_notificationtypes.setVisibility(View.VISIBLE);
                } else
                    ll_notificationtypes.setVisibility(View.GONE);
            }
        });
    }

    public void loginForDevice(View v) {
        startActivity(new Intent(getApplicationContext(),
                LoginForDeviceActivity.class));
    }

    public void save(View v) {

        String notification = checkboxIsChecked(cb_notification);
        String game_start = checkboxIsChecked(cb_game);
        String your_turn = checkboxIsChecked(cb_yourturn);
        String anyones_turn = checkboxIsChecked(cb_any1turn);
        String chat = checkboxIsChecked(cb_chatmsg);

        saveSettingsNotification(notification, game_start, your_turn,
                anyones_turn, chat);
    }

    private String checkboxIsChecked(View v) {
        String result = "0";

        if (((CheckBox) v).isChecked()) {
            result = "1";
        }

        return result;
    }

    private void loadSettingsNotification() {
        LoadSettingsNotifications asynTask = new LoadSettingsNotifications(
                this, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handleData((SettingsNotifications) obj);
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

    private void saveSettingsNotification(String notification,
            String game_start, String your_turn, String anyones_turn,
            String chat) {
        SaveSettingsNotifications asynTask = new SaveSettingsNotifications(
                this, notification, game_start, your_turn, anyones_turn, chat,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        if (obj != null
                                && (((SaveSettingsNotificationsResult) obj)
                                        .getSaveSettingsNotificationsResult())
                                        .equals("1")) {
                            Toast.makeText(NotificationActivity.this,
                                    "Settings saved successfully",
                                    Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(NotificationActivity.this,
                                    "Unable to save Settings",
                                    Toast.LENGTH_LONG).show();
                        
                        finish();
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

    private void handleData(SettingsNotifications obj) {
        if (obj != null) {
            ArrayList<SettingsNotificationsDataObject> item = (ArrayList<SettingsNotificationsDataObject>) obj
                    .getSettingsNotification();
            if (!item.isEmpty()) {
                SettingsNotificationsDataObject itemobj = item.get(0);
                if ((itemobj.getNotification()).equals("0")) {
                    ll_notificationtypes.setVisibility(View.GONE);
                    cb_notification.setChecked(false);
                } else if ((itemobj.getNotification()).equals("1")) {
                    ll_notificationtypes.setVisibility(View.VISIBLE);
                    cb_notification.setChecked(true);

                }

                if ((itemobj.getGame_start()).equals("1"))
                    cb_game.setChecked(true);
                else
                    cb_game.setChecked(false);

                if ((itemobj.getYour_turn()).equals("1"))
                    cb_yourturn.setChecked(true);
                else
                    cb_yourturn.setChecked(false);

                if ((itemobj.getAnyones_turn()).equals("1"))
                    cb_any1turn.setChecked(true);
                else
                    cb_any1turn.setChecked(false);

                if ((itemobj.getChat()).equals("1"))
                    cb_chatmsg.setChecked(true);
                else
                    cb_chatmsg.setChecked(false);

            } else {
                Log.e(TAG, "SettingsNotificationsDataObject search is empty");
            }
        } else {
            Log.e(TAG, "SettingsNotifications Object is null");
        }
    }
    public void goBack(View v){
    	
    }
}
