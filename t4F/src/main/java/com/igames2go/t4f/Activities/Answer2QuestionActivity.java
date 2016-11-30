
package com.igames2go.t4f.Activities;

import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.EndOfTurn;
import com.igames2go.t4f.data.QuesWithAnsDataObject;
import com.igames2go.t4f.tasks.DoEndOfTurn;
import com.igames2go.t4f.tasks.SaveRateResult;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.CustomTextView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Answer2QuestionActivity extends AdActivity {

    private static final int PLAYBYPLAY_ACTIVITY = 2;
    private static final int SCOREBOARD_ACTIVITY = 3;
    private QuesWithAnsDataObject mSelectedAns;
    private QuesWithAnsDataObject mCorrect;

    private String mGameId = null;
    private String mGamePlaId = null;
    private String mQuesId = null;

    private CallbackManager callbackManager;

    private static boolean isResigned = false;
    private Dialog dialog;
    EndOfTurn turn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        Intent i = getIntent();
        mGameId = getIntent().getStringExtra("game_id");
        mGamePlaId = getIntent().getStringExtra("gamePlaId");
        mQuesId = getIntent().getStringExtra("quesId");
        turn = (EndOfTurn) i.getSerializableExtra("EndOfTurn");
        Log.i("EndOf turn", turn.toString());

        // Wrap your existing view with the action bar.
		// your_layout refers to the resource ID of your current layout.
		
        if (i.getBooleanExtra("timeout", true)) {
        	ViewGroup ll = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_ontimeoutanswer, null);
            setContentView(ll);

//        	view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//          ll.addView(view);
            ((TextView)ll .findViewById(R.id.tv_question)).setText(i
                    .getStringExtra("question"));
            ((TextView)ll.findViewById(R.id.tv_quescatgory)).setText(i
                    .getStringExtra("category"));
            String str = getString(R.string.outoftime_nextplayerturn);
            str = str.replace("#NAME#", turn.getEndOfTurnData()
                    .getFbname_next());
            ((TextView)ll.findViewById(R.id.ontimeoutnextplayer_tv))
                    .setText(str);

        } else {
            mSelectedAns = (QuesWithAnsDataObject) i
                    .getSerializableExtra("selected_answer");
            mCorrect = (QuesWithAnsDataObject) i
                    .getSerializableExtra("correct_answer");
            int selectedID = i.getIntExtra("selected_answer_id", -1);
            int correctID = i.getIntExtra("correct_answer_id", -1);
            if (selectedID != -1 && correctID != -1) {

                if (selectedID == correctID) {
//                	Toast.makeText(getApplicationContext(), "correct", 1000).show();
                	ViewGroup ll = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_oncorrectanswer, null);
                	setContentView(ll);
//                	view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//                    ll.addView(view);
                    ((CustomTextView) ll.findViewById(R.id.tv_question))
                            .setText(i.getStringExtra("question"));
                    ((CustomTextView) ll.findViewById(R.id.tv_quescatgory))
                            .setText(i.getStringExtra("category"));
                    String str = getString(R.string.passedcategorymsg);
                    str = str.replace("#NAME#", turn.getEndOfTurnData()
                            .getFbname_next() + "");
                    ((Button) findViewById(R.id.btn_correct_option))
                            .setText(mCorrect.getText());
                    findViewById(R.id.ll_correct)
                            .setBackgroundResource(correctID);
                    ((CustomTextView) ll
                            .findViewById(R.id.tv_correct_answer_label2))
                            .setText(str);

                } else {
                	ViewGroup ll = (ViewGroup) getLayoutInflater().inflate(R.layout.layout_onwronganswer, null);

                	setContentView(ll);
//                	view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//                    ll.addView(view);
                    ((CustomTextView) ll.findViewById(R.id.tv_question))
                            .setText(i.getStringExtra("question"));
                    ((CustomTextView) ll.findViewById(R.id.tv_quescatgory))
                            .setText(i.getStringExtra("category"));
                    findViewById(R.id.ll_correct)
                            .setBackgroundResource(correctID);
                    findViewById(R.id.ll_incorrect).setBackgroundResource(
                            selectedID);

                    ((Button) findViewById(R.id.btn_correct_option))
                            .setText(mCorrect.getText());
                    ((Button) findViewById(R.id.btn_wrong_option))
                            .setText(mSelectedAns.getText());
                    ((CustomTextView) ll
                            .findViewById(R.id.tv_wrong_answer_label2))
                            .setText(turn.getEndOfTurnData().getMessage1());
                }
            }
        }
        
        int chatcount = 0;
        try {
			chatcount = Integer.parseInt(turn.getEndOfTurnData().getChat_number());
		} catch (Exception e) {
			chatcount =0;
		}
        if(chatcount > 0) {
        	try {
				TextView tv = (TextView) findViewById(R.id.tv_pbp_count);
				tv.setVisibility(View.VISIBLE);
				tv.setText(chatcount+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else
        	findViewById(R.id.tv_pbp_count).setVisibility(View.INVISIBLE);
        
        String image = turn.getEndOfTurnData().getImage1();
        if(!TextUtils.isEmpty(image)) {
        	//image = "http://www.trivia4friends.com/dev/game/images/"+image;
        	image = "http://www.trivia4friends.com/t4fapi/images/"+image;
        }
        else
        {
        	image = null;
        }
        DialogUtil.createDialog_title_bitmap_message(this, turn.getEndOfTurnData().getDidyouknow_title(), image, turn.getEndOfTurnData().getMessage2(), R.drawable.button_ok, new ButtonClickListener() {

            @Override
            public void onButtonClick(int s) throws NullPointerException {
//				d.dismiss();

            }
        });
        ImageLoader.readMap(getApplicationContext());

        if(getString(R.string.ad_enabled).equals("1"))
        {

        }else{

        }

    }

    @Override
    protected void showAd() {
        if(getString(R.string.ad_enabled).equals("1"))
        {
            Log.d("banner_image", turn.getEndOfTurnData().banner_image);

            ImageView adImageView = (ImageView) findViewById(R.id.adImageView);

            if(turn.getEndOfTurnData().banner_image.length() > 0) {

                final String banner_image = turn.getEndOfTurnData().banner_image;
                final String banner_url = turn.getEndOfTurnData().banner_url;

                Display mDisplay = this.getWindowManager().getDefaultDisplay();
                int mWidth  = mDisplay.getWidth();
                int mHeight = mDisplay.getHeight();

                Log.d("Screen Width=", String.valueOf(mWidth));

                mHeight = mWidth/320 * 50;

                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(mWidth,mHeight);
                adImageView.setLayoutParams(parms);

                ImageLoader loader = new ImageLoader(this);
                loader.displayAdImage(adImageView, banner_image);

                adImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(banner_url)));
                    }
                });

                adImageView.setVisibility(View.VISIBLE);

            }else{
                adImageView.setVisibility(View.GONE);
                super.showAd();
            }
        }else{

        }
    }


    @Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	ImageLoader.writeMap(getApplicationContext());
    	
    	super.onDestroy();
    }

    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    	try {
			Object obj= HttpManager.getResponse(getString(R.string.game_url)+"?f=getChatNumber&gamplaid="+mGamePlaId, false);
			if(obj instanceof String) {
				String str = (String)obj;
				str = str.replace("{\"chat_number\":", "");
				str = str.replace("}", "");
				
				int chatcount = Integer.parseInt(str.trim());
				if(chatcount > 0) {
		        	try {
						TextView tv = (TextView) findViewById(R.id.tv_pbp_count);
						tv.setVisibility(View.VISIBLE);
						tv.setText(chatcount+"");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		        else
		        	findViewById(R.id.tv_pbp_count).setVisibility(View.INVISIBLE);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void showScoreboard(View v) {
        showScoreBoard();
    }

    private void showScoreBoard() {
        if (mGameId == null) {
            Toast.makeText(this, "SomeThing is wrong...", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Intent intent = new Intent(getApplicationContext(),
                ScoreboardActivity.class);
        intent.putExtra("game_id", mGameId + "");
        intent.putExtra("game_pla_id", mGamePlaId + "");
        if (isResigned)
            intent.putExtra("resigned", "1");
        else
            intent.putExtra("resigned", "0");

        startActivityForResult(intent, SCOREBOARD_ACTIVITY);

    }

    public void help(View v) {

        startActivity(new Intent(this, HelpActivity.class));
    }

    public void report(View view) {
        Intent intent = new Intent(getApplicationContext(),
                ReportActivity.class);
        String str = getIntent().getStringExtra("report");
        intent.putExtra("game_pla_id", mGamePlaId + "");
        intent.putExtra("quesId", mQuesId + "");
        intent.putExtra("report", str);
        startActivity(intent);
    }

    public void showPlaybyplay(View v) {
        showPlaybyPlay();
    }

    private void showPlaybyPlay() {
        Intent intent = new Intent(this, PlaybyPlayActivity.class);
        intent.putExtra("game_id", mGameId + "");
        intent.putExtra("game_pla_id", mGamePlaId + "");
        intent.putExtra("quesId", mQuesId + "");
        if (isResigned)
            intent.putExtra("resigned", "1");
        else
            intent.putExtra("resigned", "0");

        startActivityForResult(intent, PLAYBYPLAY_ACTIVITY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);

        switch (requestCode) {
            case PLAYBYPLAY_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().containsKey("hasResigned")) {
                        String result = data.getStringExtra("hasResigned");
                        if (result.equals("1")) {
                            findViewById(R.id.btn_resign).setVisibility(View.GONE);
                            isResigned = true;
                        }
                    } else if (data.getExtras().containsKey("openScoreBoard")) {
                        String result = data.getStringExtra("openScoreBoard");
                        if (result.equals("1")) {
                            isResigned = true;
                            findViewById(R.id.btn_resign).setVisibility(View.GONE);
                            showScoreBoard();
                        } else if (result.equals("2")) {
                            showScoreBoard();
                        }
                    }
                }
                break;

            case SCOREBOARD_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (data.getExtras().containsKey("hasResigned")) {
                        String result = data.getStringExtra("hasResigned");
                        if (result.equals("1")) {
                            findViewById(R.id.btn_resign).setVisibility(View.GONE);
                            isResigned = true;
                        }
                    } else if (data.getExtras().containsKey("openPlayByPlay")) {
                        String result = data.getStringExtra("openPlayByPlay");
                        if (result.equals("1")) {
                            isResigned = true;
                            findViewById(R.id.btn_resign).setVisibility(View.GONE);
                            showPlaybyPlay();
                        } else if (result.equals("2")) {
                            showPlaybyPlay();
                        }
                    }
                }
                break;
             case 77:
             {
            	 if(turn!=null) {
            		 if(turn.getEndOfTurnData().flag2.equals("1")) {
            			 share(new Button(this));
            		 }
            		 if(turn.getEndOfTurnData().flag1.equals("1")) {
//            			 invite();
            			 ((T4FApplication)getApplication()).inviteFlag = true;
            		 }
            		 if(turn.getEndOfTurnData().flag3.equals("1")) {
//            			 invite();
            			 startActivity(new Intent(this, AddActivity.class));
            		 }
            		 if(turn.getEndOfTurnData().flag4.equals("1")) {
            			 
            			 final ArrayList<Integer> list = new ArrayList<Integer>();
         		         list.add(R.drawable.button_yes_wide);
         		         list.add(R.drawable.button_undecided_wide);
         		         list.add(R.drawable.button_notreally_wide);
         		         
            			 dialog = DialogUtil.createDialog_title_buttonList(Answer2QuestionActivity.this, getString(R.string.rate_title1), list, new ButtonClickListener() {

         		                    @Override
         		                    public void onButtonClick(int s)
         		                            throws NullPointerException {
         		                        if (s==list.get(0)) {
         		                        	
         		                        	final ArrayList<Integer> list1 = new ArrayList<Integer>();
         		           		         	list1.add(R.drawable.button_gotostore_wide);
         		           		         	list1.add(R.drawable.button_askweek_wide);
         		           		         	list1.add(R.drawable.button_askmonth_wide);
         		           		         	list1.add(R.drawable.button_never_wide);
         		           		         	d = DialogUtil.createDialog_title_buttonList(Answer2QuestionActivity.this, getString(R.string.rate_title2), list1, new ButtonClickListener() {

         		           		                    @Override
         		           		                    public void onButtonClick(int s)
         		           		                            throws NullPointerException {
         		           		                        if (s==list1.get(0)) {
         		           		                        	saveRateResult(1);
         		           		                        }
         		           		                        if (s==list1.get(1)) {
                                                            saveRateResult(2);
                                                        }
         		           		                        if (s==list1.get(2)) {
         		           		                        	saveRateResult(3);
         		           		                        }
	         		           		                    if (s==list1.get(3)) {
	         		           		                    	saveRateResult(4);
	     		           		                        }
         		           		                        
         		           		                        d.dismiss();
         		           	                            return;
         		           		                    }
         		           		                });
	         		           		        d.setCancelable(false);
	         		           		        d.setCanceledOnTouchOutside(false);
	         		           		        d.show();
         		                        }
         		                        if (s==list.get(1)) {
         		                        	saveRateResult(2);
         		                        }
         		                        if (s==list.get(2)) {
         		                        	saveRateResult(0);
         		                        }
         		                        
         		                        dialog.dismiss();
         	                            return;
         		                    }
         		                });
         		        dialog.setCancelable(false);
         		        dialog.setCanceledOnTouchOutside(false);
         		        dialog.show();
            		 }
            	 }
             }
            	 
        }
    }

	Dialog d;

    public void share(View v1) {
    	
    	if(FacebookManager.init()) {
    		d = DialogUtil.createDialog_title_divider_message_two_btn(this,
    				"Share Question", false, getString(R.string.fb_ok_to_post),
    				R.drawable.button_cancel, R.drawable.button_ok, new ButtonClickListener() {

    			@Override
    			public void onButtonClick(int s)
    					throws NullPointerException {
    				if (s == R.drawable.button_ok)
    					post();
    				d.dismiss();
    			}
    		}, -1);
    		d.show();
    	}
    	else
    	
    	{
            d = DialogUtil.createDialog_title_divider_message_two_btn(this,
                    "Share Question", false, getString(R.string.no_login_share_message),
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

    public void resign(View v) {
        if (mGamePlaId != null && !(mGamePlaId.length() == 0)) {
            d = DialogUtil.createDialog_title_divider_message_two_btn(this,
                    null, false, getString(R.string.resign_dialog_msg),
                    R.drawable.button_cancel, R.drawable.button_resign, new ButtonClickListener() {

                        @Override
                        public void onButtonClick(int s)
                                throws NullPointerException {
                            if (s == R.drawable.button_resign)
                                resignFromGame(mGamePlaId);
                            d.dismiss();
                        }
                    }, -1);
            d.show();

        } else
            Toast.makeText(this,
                    "Game Player Id is null...so cannot Resign from the game",
                    Toast.LENGTH_LONG).show();
    }

    public void showDialogonResignSuccess(String successmsg) {
        d = DialogUtil.createDialog_title_divider_message_two_btn(this, null,
                false, successmsg, -1, R.drawable.button_ok, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        d.dismiss();
                    }
                }, -1);
        d.show();
    }

    @SuppressWarnings("deprecation")
    private void post() {

        FacebookManager.fbShare(
                getString(R.string.fb_post_name_android),
                getString(R.string.fb_share_url),
                getString(R.string.fb_post_picture),
                getString(R.string.fb_post_message) + " " + getIntent().getStringExtra("question"),
                this,
                callbackManager,
                new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        DialogUtil.createDialog_title_bitmap_message1(
                                Answer2QuestionActivity.this, null, null,
                                getString(R.string.fb_complete_post), R.drawable.button_ok,
                                new ButtonClickListener() {

                                    @Override
                                    public void onButtonClick(int s)
                                            throws NullPointerException {
                                        // TODO Auto-generated method stub
//                                d.dismiss();
                                    }
                                });
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                }
        );
    }

    private void resignFromGame(String gameplaId) {
        DoEndOfTurn asynTask;
        asynTask = new DoEndOfTurn(this, gameplaId, "0", "0", "0", "0", "0",
                "0", new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @Override
                    public void onLoadingComplete(Object obj) {
                        if (obj != null) {
                            EndOfTurn endofturnObj = (EndOfTurn) obj;
                            String successmsg = endofturnObj.getEndOfTurnData()
                                    .getMessage1()
                                    + "\n"
                                    + endofturnObj.getEndOfTurnData()
                                            .getMessage2() + "\n";
                            showDialogonResignSuccess(successmsg);
                            findViewById(R.id.btn_resign).setVisibility(
                                    View.GONE);
                            isResigned = true;
                        } else
                            Toast.makeText(Answer2QuestionActivity.this,
                                    "Resign is Unsuccessfull",
                                    Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asynTask.execute();
    }

    private void saveRateResult(final int rated) {
		SaveRateResult asyncTask = new SaveRateResult(this, rated,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

					@Override
                    public void onLoadingComplete(Object obj) {
                        
                    	String result = (String)obj;
                        if(result.equalsIgnoreCase("1")){
                        	if(rated == 1){
                        		final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        		try {
                        		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        		} catch (android.content.ActivityNotFoundException anfe) {
                        		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        		}
                        	}
                        }else{
                        	DialogUtil.createDialog_title_bitmap_message(Answer2QuestionActivity.this, "Error", null, "Request Not Success", R.drawable.button_ok, new ButtonClickListener() {
                    			
                    			@Override
                    			public void onButtonClick(int s) throws NullPointerException {
//                    				d.dismiss();
                    				
                    			}
                    		});
                        }
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }
    public void goHome(View v) {
        //startActivity(new Intent(getApplicationContext(), HomeScreen.class));
    	
    	
        Answer2QuestionActivity.this.finish();
    }

}
