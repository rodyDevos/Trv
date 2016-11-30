package com.learn2crack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import com.igames2go.t4f.Activities.HomeScreen;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.Activities.LoginPage;
import com.igames2go.t4f.R;
import com.igames2go.t4f.Activities.TermActivity;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.data.LoginUser;
import com.igames2go.t4f.listener.FBLoadListener;
import com.igames2go.t4f.tasks.LoadRegNameDefaults;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.utils.ShowDialog;
import com.learn2crack.library.UserFunctions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Profile extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_ALERT_BODY = "alert_body";
    
    public static final int PICK_IMAGE_CHOOSE_EXISTING = 1;
    public static final int PICK_IMAGE_TAKE_PHOTO = 2;
    public static final int PICK_IMAGE_FACEBOOK_PICTURE = 3;
    
    public static final String PROPERTY_REG_ID = "registration_id";
    
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    EditText inputRPassword;
    
    Button btnRegister;
    TextView registerErrorMsg;
    RelativeLayout fbConnectLayout;
    TextView fbNameText;
    Button btnFBConnect;
    Button btnChangePicture;
    ImageView profileImageView;
    public int viewOption;
    ImageLoader loader;
    private Dialog dialog;

    int pickImageOption;
    
    Bitmap profileImage;
    String email,password,fname,lname,uname;
    String app_id;
    T4FApplication mApplication;
    SharedPreferences prefs;
    
    UserFunctions userFunction;
	JSONObject json = null;
	CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_learn2crack_register);

        mApplication = (T4FApplication)getApplication();
        prefs = getSharedPreferences(HomeScreen.class.getSimpleName(),
                Context.MODE_PRIVATE);

		callbackManager = CallbackManager.Factory.create();

        loader = new ImageLoader(this);
        userFunction = new UserFunctions();
        
        pickImageOption = 0;
        
        app_id = getResources().getString(R.string.appid);
        profileImage = null;
        
        inputFirstName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputUsername = (EditText) findViewById(R.id.uname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        inputRPassword = (EditText) findViewById(R.id.rpword);
        btnRegister = (Button) findViewById(R.id.register);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        btnFBConnect = (Button) findViewById(R.id.fbConnectButton);
        btnChangePicture = (Button) findViewById(R.id.changePhotoButton);
        fbConnectLayout = (RelativeLayout) findViewById(R.id.fbConnectLayout);
        fbNameText = (TextView) findViewById(R.id.fbNameText);
        
        inputFirstName.addTextChangedListener(new TextWatcher(){
            
            @Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
            	
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String firstName = inputFirstName.getText().toString();
				String lastName = inputLastName.getText().toString();
				String uname =  firstName;
				if(lastName.length() > 0)
					uname = firstName + " " + lastName;
					 
				inputUsername.setText(uname);
			}
        });
        inputLastName.addTextChangedListener(new TextWatcher(){
            
            @Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
            	
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String firstName = inputFirstName.getText().toString();
				String lastName = inputLastName.getText().toString();
				String uname =  firstName;
				if(lastName.length() > 0)
					uname = firstName + " " + lastName;
					 
				inputUsername.setText(uname);
			}
        });
        
        btnFBConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(FacebookManager.init()){
					Log.d("Facebook", "Logged In");
					
					dialog = DialogUtil.createDialog_title_divider_message_two_btn(
							Profile.this, 
							getString(R.string.logout_title), 
							true, getString(R.string.logout_messsage), 
							R.drawable.button_no, 
							R.drawable.button_yes, 
							new ButtonClickListener() {
						
								@Override
								public void onButtonClick(int s) throws NullPointerException {
									if(s==R.drawable.button_yes)
									{
										FacebookManager.fbLogout(Profile.this);
										refresh();
									}
									dialog.dismiss();
								}
							},
							-1);
					dialog.show();
					
				}else{
					Log.d("Facebook", "Logged Out");
					ArrayList<String> permissions = new ArrayList<String>();
			        permissions.add("email");
					permissions.add("public_profile");
					permissions.add("user_friends");
					FacebookManager.fbLogin(Profile.this, callbackManager, callback, permissions);

					//FacebookManager.openActiveSession(Profile.this, true, callback, permissions);
				}
			}
		});

        Button cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            	InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 

            	inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                if (  ( !inputUsername.getText().toString().equals("")) && ( !inputFirstName.getText().toString().equals("")) && ( !inputLastName.getText().toString().equals("")) && ( !inputEmail.getText().toString().equals("")))
                {
                    if ( inputUsername.getText().toString().length() > 4 ){
                    	
                    	if(viewOption == T4FApplication.PROFILE_NEW){

                    		mApplication.setPlayerID("0");
                    		if(inputPassword.getText().toString().equals("")){
                    			 showAlertDialog("Validation Failed", "One or more fields are empty");
                    		}else if(!inputPassword.getText().toString().equals(inputRPassword.getText().toString())){
                				showAlertDialog("Validation Failed", "Password does not match");
                			}else{
                				NetAsync(view);
                			}
                    	}else{
                    		if(!inputPassword.getText().toString().equals(inputRPassword.getText().toString()))
                    			showAlertDialog("Validation Failed", "Password does not match");
                			else{
                				NetAsync(view);
                			}
                    	}
                    }
                    else
                    {
                        showAlertDialog("Validation Failed", "Username should be minimum 5 characters");
                    }
                }
                else
                {
                	showAlertDialog("Validation Failed", "One or more fields are empty");
                }
            }
        });
        
        btnChangePicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final ArrayList<Integer> list = new ArrayList<Integer>();
		        list.add(R.drawable.button_choose_existing);
		        list.add(R.drawable.button_take_photo);
		        list.add(R.drawable.button_facebook_picture);
		        list.add(R.drawable.button_cancel);
		        
				dialog = DialogUtil.createDialog_photo_select(Profile.this,
		                "Change Picture", list, new ButtonClickListener() {

		                    @Override
		                    public void onButtonClick(int s)
		                            throws NullPointerException {
		                        if (s==list.get(0)) {
		                        	Intent intent = new Intent();
		                        	intent.setType("image/*");
		                        	intent.setAction(Intent.ACTION_GET_CONTENT);
		                        	startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CHOOSE_EXISTING);
		                        }
		                        if (s==list.get(1)) {
		                        	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		                        	startActivityForResult(intent, PICK_IMAGE_TAKE_PHOTO);
		                        }
		                        if (s==list.get(2)) {
		                        	if(FacebookManager.init()){
		            					loader.displayPlayerImage(FacebookManager.fbId, profileImageView, false);
		            					pickImageOption = PICK_IMAGE_FACEBOOK_PICTURE;
		            				}else{
		            					d = DialogUtil.createDialog_title_divider_message_two_btn(Profile.this,
		            		                    "", false, getString(R.string.no_login_select_picture_message),
		            		                    -1, R.drawable.button_ok, new ButtonClickListener() {

		            		                        @Override
		            		                        public void onButtonClick(int s)
		            		                                throws NullPointerException {
		            		                      
		            		                            d.dismiss();
		            		                        }
		            		                    }, -1);
		            		            d.show();
		            				}
		                        }
		                        if( s==list.get(3)) {
		                        	
		                        }
		                        dialog.dismiss();
	                            return;
		                    }
		                });
		        dialog.setCancelable(false);
		        dialog.setCanceledOnTouchOutside(false);
		        dialog.show();
			}
		});
        refresh();
    }
    
    public void termButtonPressed(View v){
    	startActivity(new Intent(Profile.this, TermActivity.class));
    }
    
    public void refresh(){
    	
    	viewOption = getIntent().getIntExtra("viewOption", T4FApplication.PROFILE_NEW);
    	
    	if(viewOption == T4FApplication.PROFILE_NEW){
    		if(FacebookManager.init()){
    			fbConnectLayout.setBackgroundResource(R.drawable.button_fb_logout);
    			fbNameText.setText(FacebookManager.fbName);
    			inputEmail.setText(FacebookManager.fbEmail);
    			
    			ShowDialog.showLoadingDialog(Profile.this, "Loading Data...");
    			LoadRegNameDefaults task = new LoadRegNameDefaults(Profile.this, FacebookManager.fbName, new LoadingListener() {
					
					@Override
					public void onLoadingComplete() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingComplete(Object obj) {
						// TODO Auto-generated method stub
						ShowDialog.removeLoadingDialog();
						JSONObject jObj = (JSONObject)obj;
						try {
							loader.displayPlayerImage(FacebookManager.fbId, profileImageView, false);
							
							inputFirstName.setText(jObj.getString("firstname"));
							inputLastName.setText(jObj.getString("lastname"));
							inputUsername.setText(jObj.getString("playername"));
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					@Override
					public void onError(Object error) {
						// TODO Auto-generated method stub
						
					}
				});
    			task.execute();
    			
    		}else{
    			fbConnectLayout.setBackgroundResource(R.drawable.button_fb_connect);
    			inputFirstName.setText("");
				inputLastName.setText("");
				inputUsername.setText("");
				inputEmail.setText("");
				fbNameText.setText("");
				loader.displayPlayerImage("", profileImageView, false);
    		}
    	}else{
    		
    		ShowDialog.removeLoadingDialog();
    		
    		if(FacebookManager.init()){
    			fbConnectLayout.setBackgroundResource(R.drawable.button_fb_logout);
    			fbNameText.setText(FacebookManager.fbName);
    			
    		}else{
    			fbConnectLayout.setBackgroundResource(R.drawable.button_fb_connect);
    			fbNameText.setText("");
    		}
    		
    		inputFirstName.setText(mApplication.getLoginUser().getFirstName());
			inputLastName.setText(mApplication.getLoginUser().getLastName());
			inputUsername.setText(mApplication.getLoginUser().getUserName());
			inputEmail.setText(mApplication.getLoginUser().getEmail());
			loader.displayPlayerImage(mApplication.getPlayerImage(), profileImageView, false);
    	}
    }
    
    public static int calculateInSampleSize(
    		 
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
     
            if (height > reqHeight || width > reqWidth) {
     
                // Calculate ratios of height and width to requested height and width
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
     
                // Choose the smallest ratio as inSampleSize value, this will guarantee
                // a final image with both dimensions larger than or equal to the
                // requested height and width.
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
     
            return inSampleSize;
        }
    
    private Bitmap getBitmapFromUri(Uri data){
        Bitmap bitmap = null;
 
        // Starting fetch image from file
        InputStream is=null;
        try {
 
            is = getContentResolver().openInputStream(data);
 
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
 
            // BitmapFactory.decodeFile(path, options);
            BitmapFactory.decodeStream(is, null, options);
 
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);
 
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
 
            is = getContentResolver().openInputStream(data);
 
            bitmap = BitmapFactory.decodeStream(is,null,options);
 
            if(bitmap==null){
                Toast.makeText(getBaseContext(), "Image is not Loaded",Toast.LENGTH_SHORT).show();
                return null;
            }
 
            is.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return bitmap;
    }
    
    Dialog d;
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode ==  PICK_IMAGE_CHOOSE_EXISTING){
		        if(resultCode == RESULT_OK){  
		            Uri selectedImage = data.getData();
		            profileImage = getBitmapFromUri(selectedImage);
		            if(profileImage != null){
		            	profileImageView.setImageBitmap(profileImage);
		            }
		            
		            pickImageOption = PICK_IMAGE_CHOOSE_EXISTING;
		            /*else{
			            String[] filePathColumn = {MediaStore.Images.Media.DATA};
			
			            Cursor cursor = getContentResolver().query(
			                               selectedImage, filePathColumn, null, null, null);
			            cursor.moveToFirst();
			
			            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			            String filePath = cursor.getString(columnIndex);
			            Log.d("File Path", filePath);
			            cursor.close();
			
			            profileImage = BitmapFactory.decodeFile(filePath);
			            profileImageView.setImageBitmap(profileImage);
			            pickImageOption = PICK_IMAGE_CHOOSE_EXISTING;
		            }
		            */
		        }
		}else if(requestCode == PICK_IMAGE_TAKE_PHOTO){
			Bundle extras = data.getExtras();
			profileImage = (Bitmap) extras.get("data");
	        profileImageView.setImageBitmap(profileImage);
	        pickImageOption = PICK_IMAGE_TAKE_PHOTO;
		}else if(requestCode == PICK_IMAGE_FACEBOOK_PICTURE){
		}else{
			//Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
	}
	private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
		@Override
		public void onSuccess(LoginResult loginResult) {
			ShowDialog.showLoadingDialog(Profile.this, "Loading Facebook Info...");
			FacebookManager.getPersonalInfo(fbListener);
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onError(FacebookException error) {

		}
	};

	/*
	private Session.StatusCallback callback = new Session.StatusCallback() {
    	@Override
	    public void call(Session session, SessionState state, Exception exception) {
	    	if (session.isOpened()) {
	    		ShowDialog.showLoadingDialog(Profile.this, "Loading Facebook Info...");
	    		FacebookManager.getPersonalInfo(session, fbListener);
	    	}else{
	    		
	    	}
	    }
    };
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("Facebook", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("Facebook", "Logged out...");
        }
    }
    */

    /**
     * Async Task to check whether internet connection is working
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(Profile.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
            nDialog.setCanceledOnTouchOutside(false);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
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
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                //registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog pDialog;
        int success = 0;
        int errorCode = 0;
    	String errorMsg = "";
    	String message = "";
    	String pla_fb_id = "";
    	String pla_id = "";
    	String registrationId = prefs.getString(PROPERTY_REG_ID, "");
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inputUsername = (EditText) findViewById(R.id.uname);
            inputPassword = (EditText) findViewById(R.id.pword);
            fname = inputFirstName.getText().toString();
            lname = inputLastName.getText().toString();
            email = inputEmail.getText().toString();
            uname= inputUsername.getText().toString();
            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(Profile.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
		protected JSONObject doInBackground(String... args) {
        	
        	String plaid = mApplication.getPlayerID();
        	Log.d("plaid", plaid);
        	if(viewOption == T4FApplication.PROFILE_NEW){
        		json = userFunction.registerUser(fname, lname, email, uname, password, app_id, FacebookManager.fbId, registrationId, "", plaid);
        		
        		try {
        			success = Integer.parseInt(json.getString(KEY_SUCCESS));
        			if(success == 1){
		        		JSONObject json_user = json.getJSONObject("user");
		                LoginUser user = new LoginUser();
		                user.setUserId(json_user.getString(KEY_UID));
		                
		                mApplication.setLoginUser(user);
		                
		                plaid = json_user.getString(KEY_UID);
        			}
        		}catch(JSONException e){
        			e.printStackTrace();
        		}
                
        	}else{
        		json = userFunction.updateUser(plaid, fname, lname, email, uname, password, app_id, FacebookManager.fbId);
        		//json = userFunction.updaterUser(mApplication.getLoginUser().getUserId(), fname, lname, email, uname, password, app_id, FacebookManager.fbId);
        	}

        	if((!plaid.isEmpty()) && pickImageOption > 0){
        		if(pickImageOption == PICK_IMAGE_FACEBOOK_PICTURE){
        			json = userFunction.uploadFBPhoto(plaid, app_id, FacebookManager.fbId);
        			Log.d("FB Photo Upload", json.toString());
        		}else{
        			uploadPhoto(plaid);
        		}
        	}
        	
            return json;
        }
       @Override
        protected void onPostExecute(JSONObject json) {
            
    	   pDialog.dismiss();
    	   try {
    		   	success = Integer.parseInt(json.getString(KEY_SUCCESS));
	        	errorCode = Integer.parseInt(json.getString(KEY_ERROR));
	        	
                if (json.getString(KEY_SUCCESS) != null) {
                    //registerErrorMsg.setText("");
                    
                    if(success == 1){
                    	pickImageOption = 0;
                    	if(viewOption == T4FApplication.PROFILE_NEW){
                    		message = "You can now login to play " + getResources().getString(R.string.app_name) + " using the email of " + email + " and the password of \"" + password + "\"";
                    		dialog = DialogUtil.createDialog_title_divider_message_two_btn(
                    				Profile.this, 
                    				"You Have Signed Up", 
                    				true, 
                    				message, 
                    				-1, 
                    				R.drawable.button_ok, 
                    				new ButtonClickListener() {
                    			
                        				@Override
                        				public void onButtonClick(int s) throws NullPointerException {
                        					dialog.dismiss();
                        					
                        					SharedPreferences pref = getSharedPreferences("user_info", MODE_PRIVATE);
                        					SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("user_email", email);
                                    		editor.putString("user_password", password);
                                    		editor.commit();
                                    		
                        					Intent activity = new Intent(Profile.this, LoginPage.class);
                	                        startActivity(activity);
                	                        finish();
                        				}
                    				},
                    				-1);
                        	dialog.show();
	                        
                    	}else{
                    		mApplication.refresh = true;
                    		finish();
                    	}
                    }else{  
                    	errorMsg = json.getString(KEY_ERROR_MSG);
                    	if(errorCode == -1){
    	        			
    	        			pDialog.dismiss();
    	        			
    	        			message = json.getString(KEY_ALERT_BODY);
    	    	        	pla_fb_id = json.getString("pla_fb_id");
    	    	        	pla_id = json.getString("pla_id");
    	    	        	
                			dialog = DialogUtil.createDialog_title_divider_message_with_image_two_btn(
                					Profile.this, 
                					errorMsg, 
                					true, 
                					message, 
                					pla_fb_id,
                					R.drawable.button_no, 
                					R.drawable.button_yes, 
                					new ButtonClickListener() {
                				
    	                				@Override
    	                				public void onButtonClick(int s) throws NullPointerException {
    	                					if(s==R.drawable.button_yes)
    	                					{
    	                						//json = userFunction.registerUser(fname, lname, email, uname, password, app_id, FacebookManager.fbId, registrationId, "", pla_id);
    	                						mApplication.setPlayerID(pla_id);
    	                					}else{
    	                						//json = userFunction.registerUser(fname, lname, email, uname, password, app_id, FacebookManager.fbId, registrationId, "", "-1");
    	                						mApplication.setPlayerID("-1");
    	                					}
    	                					dialog.dismiss();
    	                					new ProcessRegister().execute();
    	                					//pDialog.show();
    	                				}
                					},
                					-1);
                	    	dialog.show();
    	        		}else{
    	        			showAlertDialog("Registeration Failed", errorMsg);
    	        		}
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error occured in registration", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
       	}
    }
    
    public void NetAsync(View view){
            new NetCheck().execute();
    }
    
    FBLoadListener fbListener = new FBLoadListener() {
    	public void onFBLogin() {};
    	public void onFBLogout() {
    		refresh();
    	};
    	public void onFBDone() {
    		refresh();
    	};
    };
    
    @SuppressWarnings("deprecation")
	public void uploadPhoto(String plaid) {
    	try {
    		if(profileImage != null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                profileImage.compress(CompressFormat.JPEG, 75, bos);
                byte[] data = bos.toByteArray();
                HttpClient httpClient = new DefaultHttpClient();
                String uploadUrl = getResources().getString(R.string.upload_url);
                HttpPost postRequest = new HttpPost(uploadUrl);
                ByteArrayBody bab = new ByteArrayBody(data, uname+".jpg");
                
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("userfile", bab);
                reqEntity.addPart("plaid", new StringBody(plaid));
                reqEntity.addPart("appid", new StringBody(app_id));
                postRequest.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
     
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                System.out.println("Response: " + s);
    		}
    		
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }	
    
    public void showAlertDialog(String title, String message){
    	dialog = DialogUtil.createDialog_title_divider_message_two_btn(
				Profile.this, 
				title, 
				true, 
				message, 
				-1, 
				R.drawable.button_ok, 
				new ButtonClickListener() {
			
    				@Override
    				public void onButtonClick(int s) throws NullPointerException {
    					dialog.dismiss();
    				}
				},
				-1);
    	dialog.show();
    }
    
    public void cancel(View v){
    	finish();
    }
}


