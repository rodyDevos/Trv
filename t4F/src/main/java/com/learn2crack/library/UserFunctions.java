package com.learn2crack.library;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@learn2crack.com
 * Website:www.learn2crack.com
 **/

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.learn2crack.ChangeName;

import android.content.Context;
import android.util.Log;


public class UserFunctions {

    private JSONParser jsonParser;

    //URL of the PHP API
    /*
    private static String loginURL = "http://10.0.2.2/learn2crack_login_api/";
    private static String registerURL = "http://10.0.2.2/learn2crack_login_api/";
    private static String forpassURL = "http://10.0.2.2/learn2crack_login_api/";
    private static String chgpassURL = "http://10.0.2.2/learn2crack_login_api/";
	*/
    private static String loginURL = "http://www.trivia4friends.com/t4fapi/login/index.php";
    private static String registerURL = "http://www.trivia4friends.com/t4fapi/login/index.php";
    private static String forpassURL = "http://www.trivia4friends.com/t4fapi/login/index.php";
    private static String chgpassURL = "http://www.trivia4friends.com/t4fapi/login/index.php";
    private static String chgnameURL = "http://www.trivia4friends.com/t4fapi/login/index.php";
    
    private static String login_tag = "login";
    private static String register_tag = "new";
    private static String update_tag = "update";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String chgname_tag = "chgname";
    

    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password, String appId){
        // Building Parameters
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        /*
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        */
    	
        String url = String.format(loginURL+"?tag=%s&email=%s&password=%s&appid=%s",
        		login_tag, 
        		email, 
        		password,
        		appId);
        url = url.replaceAll(" ", "%20");
        Log.d("Login URL", url);
        JSONObject json = jsonParser.getJSONFromUrl(url, params);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        /*
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        */
        String url = String.format(chgpassURL+"?tag=%s&newpas=%s&email=%s",
        		chgpass_tag, 
        		newpas,
        		email
        		);
        url = url.replaceAll(" ", "%20");
        Log.d("Change Password URL", url);
        JSONObject json = jsonParser.getJSONFromUrl(url, params);
        return json;
    }

    public JSONObject chgName(String newname, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        /*
        params.add(new BasicNameValuePair("tag", chgpass_tag));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        */
        String url = String.format(chgnameURL+"?tag=%s&newname=%s&email=%s",
        		chgname_tag, 
        		newname,
        		email
        		);
        url = url.replaceAll(" ", "%20");
        Log.d("Change Name URL", url);
        JSONObject json = jsonParser.getJSONFromUrl(url, params);
        return json;
    }



    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword, String appId){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = String.format(forpassURL+"?tag=%s&forgotpassword=%s&appid=%s",
        		forpass_tag, 
        		forgotpassword,
        		appId
        		);
        Log.d("Forgot URL", url);
        url = url.replaceAll(" ", "%20");
        /*
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        */
        JSONObject json = jsonParser.getJSONFromUrl(url, params);
        return json;
    }






     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password, String appId, String fbId, String regid, String deviceid, String plaid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = String.format(registerURL+"?tag=%s&fname=%s&lname=%s&uname=%s&email=%s&password=%s&appid=%s&fbid=%s&regid=%s&deviceid=%s&plaid=%s",
        		register_tag, 
        		fname,
        		lname,
        		uname,
        		email, 
        		password,
        		appId,
        		fbId,
        		regid,
        		deviceid,
        		plaid);
        url = url.replaceAll(" ", "%20");
        Log.d("Register URL", url);
        
        JSONObject json = jsonParser.getJSONFromUrl(url,params);
        return json;
    }

    public JSONObject updateUser(String plaid, String fname, String lname, String email, String uname, String password, String appId, String fbId){
        // Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = String.format(registerURL+"?tag=%s&fname=%s&lname=%s&uname=%s&email=%s&password=%s&appid=%s&fbid=%s&plaid=%s",
        		update_tag, 
        		fname,
        		lname,
        		uname,
        		email, 
        		password,
        		appId,
        		fbId,
        		plaid);
        url = url.replaceAll(" ", "%20");
        Log.d("Register URL", url);
        
        JSONObject json = jsonParser.getJSONFromUrl(url,params);
        return json;
    }
    
    public JSONObject uploadFBPhoto(String plaid, String appId, String fbId){
        // Building Parameters
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = String.format(loginURL+"?tag=%s&appid=%s&fbid=%s&plaid=%s",
        		"fbphoto", 
        		appId,
        		fbId,
        		plaid);
        url = url.replaceAll(" ", "%20");
        Log.d("Upload FB Photo URL", url);
        
        JSONObject json = jsonParser.getJSONFromUrl(url,params);
        return json;
    }
    
    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

