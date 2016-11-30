
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SaveSettingsLoginsResult;
import com.igames2go.t4f.data.SettingsLogins;
import com.igames2go.t4f.data.SettingsLoginsDataObject;
import com.igames2go.t4f.tasks.LoadSettingsLogins;
import com.igames2go.t4f.tasks.SaveSettingsLogins;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginForDeviceActivity extends Activity implements OnItemLongClickListener {

    private final String TAG = "T4F";
    private ListView listView;
    private LoginForDeviceListAdapter adapter = null;
    private ArrayList<SettingsLoginsDataObject> item = null;
    private Dialog d;

    public void onItemLongClick(final View v) {
        if (item != null) {
            ArrayList<Integer> strs = new ArrayList<Integer>();
            strs.add(R.drawable.button_delete_from_game);
            strs.add(R.drawable.button_cancel);
            d = DialogUtil.createDialog_title_buttonList(LoginForDeviceActivity.this, "Options",
                    strs, new ButtonClickListener() {
                        @Override
                        public void onButtonClick(int s) throws NullPointerException {
                        	if(s==R.drawable.button_delete_from_game){
                        		onDeleteLogin(v);
                        	}
                            d.dismiss();
                        }
                    });
            d.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_logins_for_device);

        listView = (ListView) findViewById(R.id.lv_logins_for_device);
        loadSettingsLogins();
    }

    public void save(View v) {
        if (item != null)
        {
            int index = 1;
            for (SettingsLoginsDataObject obj : item) {
                if (obj.isChanged())
                {
                    if((obj.getDevice_notify()).equals("0"))
                        saveSettingsLogins(obj.getPlaapp_id(), "1", index);
                    else
                        saveSettingsLogins(obj.getPlaapp_id(), "0", index);
                }
                index++;
            }
        }
        else
            Toast.makeText(LoginForDeviceActivity.this,
                    "List is empty",
                    Toast.LENGTH_LONG).show();

    }

    private void loadSettingsLogins() {
        LoadSettingsLogins asynTask = new LoadSettingsLogins(
                this, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handleData((SettingsLogins) obj);
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

    private void saveSettingsLogins(String plaapp_id, String device_notify, final int position) {
        String deviceId = ((T4FApplication) this.getApplication()).getDeviceID();
        SaveSettingsLogins asynTask = new SaveSettingsLogins(
                plaapp_id, device_notify, deviceId,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        if (obj != null
                                && (((SaveSettingsLoginsResult) obj)
                                        .getSaveSettingsLoginsResult())
                                        .equals("1")) {
                            Toast.makeText(LoginForDeviceActivity.this,
                                    "Logins Settings saved successfully",
                                    Toast.LENGTH_LONG).show();
                            if(position == 0)
                            {
                               loadSettingsLogins();
                            }
                            else
                            {
                                item.get(position-1).setChanged(false);
                            }
                        } else
                            Toast.makeText(LoginForDeviceActivity.this,
                                    "Unable to save Logins Settings",
                                    Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                    }

                }, getApplicationContext());
        asynTask.execute();

    }

    private void handleData(SettingsLogins obj) {
        if (obj != null) {
            item = (ArrayList<SettingsLoginsDataObject>) obj
                    .getSettingsLogins();
            if (!item.isEmpty()) {
                adapter = new LoginForDeviceListAdapter(this,
                        R.layout.layout_loginfordevicelistadapter, item);
                listView.setAdapter(adapter);
            } else {
                Log.e(TAG, "SettingsLoginsDataObject search is empty");
                adapter = new LoginForDeviceListAdapter(this,
                        R.layout.layout_loginfordevicelistadapter, null);
                listView.setAdapter(adapter);
            }
        } else {
            Log.e(TAG, "SettingsLogins Object is null");
        }
    }

    public void onDeleteLogin(final View v)
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                SettingsLoginsDataObject obj = (SettingsLoginsDataObject) v.getTag();
                saveSettingsLogins(obj.getPlaapp_id(), "-1", 0);                
            }
        });
    }

    public void onCheckBoxClicked(final View v)
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                SettingsLoginsDataObject obj = (SettingsLoginsDataObject) v.getTag();

                boolean isChanged = obj.isChanged();
                obj.setChanged(!isChanged);

            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
            long arg3) {
        return false;
    }
    public void cancel(View v){
    	finish();
    }
}
