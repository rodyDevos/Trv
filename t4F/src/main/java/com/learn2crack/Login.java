package com.learn2crack;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.igames2go.t4f.Activities.HomeScreen;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.LoginUser;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DatabaseHandler;
import com.igames2go.t4f.utils.DialogUtil;
import com.learn2crack.library.UserFunctions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    
    Button btnReturn;
    //private TextView loginErrorMsg;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";

    SharedPreferences pref;
    private T4FApplication mApplication;
    private String app_id;
    
    private static Dialog dialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.com_learn2crack_activity_login);

        pref = getSharedPreferences("user_info", MODE_PRIVATE);
		
        mApplication = (T4FApplication)getApplication();
        app_id = getResources().getString(R.string.appid);
        
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        //loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        btnReturn = (Button) findViewById(R.id.return_button);
        
       
        
        inputEmail.setText(pref.getString("user_email", ""));
        inputPassword.setText(pref.getString("user_password", ""));
        
        btnReturn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        	Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
        	startActivityForResult(myIntent, 0);
        	finish();
        }});


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Profile.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

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
            }
        });
        
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        if(db.getRowCount() == 0){
        	dialog = DialogUtil.createDialog_title_divider_message_two_btn(
        			Login.this, 
        			"New Login Method", 
					true, 
					"You now setup your own login. Tap OK and 'Sign Up' to setup a Login name, email and password.", 
					-1,
					R.drawable.button_ok,
					new ButtonClickListener() {
        				
        				@Override
        				public void onButtonClick(int s) throws NullPointerException {
        					if(s==R.drawable.button_ok)
        					{
        						Intent myIntent = new Intent(Login.this, Profile.class);
        		                startActivityForResult(myIntent, 0);
        		                //finish();
        					}
        					dialog.dismiss();
        				}
        			}, -1);
        	    	dialog.show();
        }
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
            nDialog = new ProgressDialog(Login.this);
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
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

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

            inputEmail = (EditText) findViewById(R.id.email);
            inputPassword = (EditText) findViewById(R.id.pword);
            email = inputEmail.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Login.this);
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
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        //UserFunctions logout = new UserFunctions();
                        //logout.logoutUser(getApplicationContext());
                        //db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                       /**
                        *If JSON array details are stored in SQlite it launches the User Panel.
                        **/
                        //upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        
                        
                        pDialog.dismiss();
                        finish();
                        Intent upanel = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(upanel);
                        return;
                    }else{
                        pDialog.dismiss();
                        //loginErrorMsg.setText("Incorrect username/password");
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
    public void sendEmail(View view){
    	//Intent emailIntent = new Intent(Intent.ACTION_SEND);
    	//emailIntent.setData(Uri.parse("mailto:"));
    	//emailIntent.setType("text/plain");
    	
    	Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
    	emailIntent.setType("message/rfc822");

    	
    	emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Support@Trivia4Friends.com"});
    	emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Unable to Login");
    	emailIntent.putExtra(Intent.EXTRA_TEXT   , "");
    	
    	try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
         } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Login.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
         }
    }
}