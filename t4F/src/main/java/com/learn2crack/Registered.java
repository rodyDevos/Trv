package com.learn2crack;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.LoginUser;
import com.learn2crack.library.DatabaseHandler;

import java.util.HashMap;

import org.json.JSONObject;

public class Registered extends Activity {
	
	T4FApplication mApplication;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_learn2crack_registered);

        mApplication = (T4FApplication)getApplication();
        LoginUser user = mApplication.getLoginUser();

        /**
         * Displays the registration details in Text view
         **/

        final TextView fname = (TextView)findViewById(R.id.fname);
        final TextView lname = (TextView)findViewById(R.id.lname);
        final TextView uname = (TextView)findViewById(R.id.uname);
        final TextView email = (TextView)findViewById(R.id.email);
        final TextView created_at = (TextView)findViewById(R.id.regat);
        fname.setText(user.getFirstName());
        lname.setText(user.getLastName());
        uname.setText(user.getUserName());
        email.setText(user.getEmail());
        created_at.setText(user.getCreatedDate());

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	 mApplication.setLoginUser(null);
                Intent myIntent = new Intent(view.getContext(), Login.class);
                startActivityForResult(myIntent, 0);
                finish();
            }

        });


    }}
