/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igames2go.t4f.Activities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.igames2go.t4f.Activities.SelectCategory.SelectCategoryActivity;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.LoginUser;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DatabaseHandler;
import com.igames2go.t4f.utils.DialogUtil;
import com.learn2crack.PasswordReset;
import com.learn2crack.Profile;
import com.learn2crack.library.UserFunctions;

public class LoginPage extends FragmentActivity implements OnClickListener {

    //private static final List<String> PERMISSIONS = Arrays.asList("publish_actions","email");
	//private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private Dialog dialog;
    private static final Location SEATTLE_LOCATION = new Location("") {
        {
            setLatitude(47.6097);
            setLongitude(-122.3331);
        }
    };

    private T4FApplication mApplication;
    private String app_id;
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";

    SharedPreferences pref;
    ProgressDialog pDialog;
    ImageButton btnLogin;
    ImageButton btnSignUp;
    ImageButton btnForgot;
    ImageButton btnNoLoginPlay;
    ImageButton btnHelp;
    ImageButton btnHowToPlay;

    EditText inputEmail;
    EditText inputPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = ((T4FApplication)getApplication());
        mApplication.setPlay_mode(T4FApplication.PLAY_MODE_LOGIN);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.igames2go.t4f", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:====================",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        setContentView(R.layout.main);

        pref = getSharedPreferences("user_info", MODE_PRIVATE);

        mApplication = (T4FApplication)getApplication();
        app_id = getResources().getString(R.string.appid);

        inputEmail = (EditText) findViewById(R.id.emailText);
        inputPassword = (EditText) findViewById(R.id.passwordText);
        btnLogin = (ImageButton) findViewById(R.id.loginButton);
        btnSignUp = (ImageButton) findViewById(R.id.signupButton);
        btnForgot = (ImageButton)findViewById(R.id.forgotButton);
        btnNoLoginPlay = (ImageButton)findViewById(R.id.noLoginPlayButton);
        btnHowToPlay = (ImageButton)findViewById(R.id.howToPlayButton);
        btnHelp = (ImageButton)findViewById(R.id.helpButton);
        //loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);

        inputEmail.setText(pref.getString("user_email", ""));
        inputPassword.setText(pref.getString("user_password", ""));

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnNoLoginPlay.setOnClickListener(this);
        btnHowToPlay.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
    }

    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(LoginPage.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.setCanceledOnTouchOutside(false);
            nDialog.show();
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
        **/
        @Override
        protected Boolean doInBackground(String... args){

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    Log.d("response code", urlc.getResponseCode()+"");
                    //if (urlc.getResponseCode() == 200) {
                        return true;
                    //}
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //return false;
            return true;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                //loginErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    /**
     * Async Task to get and send data to My Sql database through JSON respone.
     **/
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
        String email,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(LoginPage.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Logging in ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email, password, app_id);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
            	Log.d("Login Data", json.toString());
               if (json.getString(KEY_SUCCESS) != null) {

                    String res = json.getString(KEY_SUCCESS);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Data");
                        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        LoginUser user = new LoginUser();
                        user.setUserId(json_user.getString(KEY_UID));

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("user_email", email);
                		editor.putString("user_password", password);
                		editor.commit();

                        mApplication.setLoginUser(user);
                        //mApplication.setPlayerImage(json_user.getString(KEY_UID));

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    	db.storeLoginPlayerId(Integer.parseInt(user.getUserId()));

                        pDialog.dismiss();

                        Intent upanel = new Intent(LoginPage.this, HomeScreen.class);
                        startActivity(upanel);
                        return;
                    }else{
                        pDialog.dismiss();
                        String errorMsg = json.getString("error_msg");

                        DialogUtil.createDialog_title_bitmap_message1(LoginPage.this, "Invalid Login", null, errorMsg, R.drawable.button_ok,  new ButtonClickListener() {

                            @Override
                            public void onButtonClick(int s)
                                    throws NullPointerException {
                            }
                        });
                    }
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
    }


    public void NetAsync(View view){
        new NetCheck().execute();
    }

    @Override
	public void onClick(View view) {

		switch(view.getId()){
			case R.id.signupButton:
				startActivity(new Intent(LoginPage.this, Profile.class));
				break;
			case R.id.loginButton:
				mApplication.setPlay_mode(T4FApplication.PLAY_MODE_LOGIN);
				if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                    NetAsync(view);
                }
                else if ( ( !inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
				break;
			case R.id.forgotButton:
	        	startActivity(new Intent(LoginPage.this, PasswordReset.class));
				break;
			case R.id.noLoginPlayButton:
				mApplication.setPlay_mode(T4FApplication.PLAY_MODE_NO_LOGIN);
	            startActivity(new Intent(LoginPage.this, HomeScreen.class));
                //startActivity(new Intent(LoginPage.this, SelectCategoryActivity.class));
				break;
			case R.id.howToPlayButton:
				Intent i = new Intent(LoginPage.this, HelpActivity.class);
				i.setAction("howtoplay");
				startActivity(i);
				break;
			case R.id.helpButton:
				startActivity(new Intent(LoginPage.this, SupportActivity.class));
				break;
			default:
				break;
		}
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.d("LoginPage", "Start");
		//((T4FApplication)getApplication()).play_mode = T4FApplication.PLAY_MODE_LOGIN;
	}

	@Override
    protected void onSaveInstanceState(Bundle outState) {
     super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
     super.onRestoreInstanceState(savedInstanceState);
    }
}
