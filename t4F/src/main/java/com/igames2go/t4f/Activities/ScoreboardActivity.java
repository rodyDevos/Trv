
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.EndOfTurn;
import com.igames2go.t4f.data.GameStatistics;
import com.igames2go.t4f.data.GameStatisticsDataObject;
import com.igames2go.t4f.tasks.DoEndOfTurn;
import com.igames2go.t4f.tasks.LoadGameStatistics;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ImageLoader;
import com.igames2go.t4f.view.CustomButton;
//import com.igames2go.t4f.view.CustomImageView;
import com.igames2go.t4f.view.CustomTextView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

public class ScoreboardActivity extends AdActivity {

    private final String TAG = "T4F";
    private int itemCount = 0;
    private String gameId = null;
    private String mGamePlaId = null;

    private GameStatistics gameStatsObject = null;
    private static boolean isResigned = false;

    public static boolean isScreenGameBoard = false;
    ImageLoader loader;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scoreboard);
        loader = new ImageLoader(this);
        Intent i = getIntent();
        gameId = i.getStringExtra("game_id");
        mGamePlaId = i.getStringExtra("game_pla_id");

        findViewById(R.id.btn_scoreboard).setVisibility(View.GONE);

        isResigned = false;
        if ((i.getStringExtra("resigned")).equals("1")) {
            findViewById(R.id.btn_resign).setVisibility(View.GONE);
            isResigned = true;
        }

        if (gameId != null && !(gameId.length() == 0))
            loadGameStatisticsData();
        else return;
//            Toast.makeText(this, "GameId is null", Toast.LENGTH_LONG).show();
        ImageLoader.readMap(getApplicationContext());

        CustomButton backButton = (CustomButton)findViewById(R.id.go_back); 
        backButton.setBackgroundResource(R.drawable.icon_back_transparent);
        //backButton.setBackground(getResources().getDrawable(R.drawable.icon_back_transparent));
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    
    private void openPlayerStat(String playerId) {
    	Intent intent = new Intent(this, PlayerStatActivity.class);
        intent.putExtra("player_id", playerId);
        startActivity(intent);
    }
    
    private void setUpViews(ArrayList<GameStatisticsDataObject> items) {
        setUpProfileRow(items);
        setUpScoreRow(items);
        ArrayList<String> totalScores = new ArrayList<String>();
        for (int i = 0; i < itemCount; i++) {
            totalScores.add(items.get(i).getGampla_point_tot());
        }
        setUpPointRow(R.id.tr_points, null, totalScores, false);
    }

    private void loadGameStatisticsData() {
        LoadGameStatistics asynTask = new LoadGameStatistics(this, gameId, true,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        gameStatsObject = (GameStatistics) obj;
                        handleData(gameStatsObject);
                        showGameboard(null);
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

    private void handleData(GameStatistics gameStatsObj) {
        if (gameStatsObj != null) {
            ArrayList<GameStatisticsDataObject> item = (ArrayList<GameStatisticsDataObject>) gameStatsObj
                    .getGameStatistics();
            if (!item.isEmpty()) {
                if ((item.get(0).getPlaid()).equals("0")) {
                    Log.e(TAG, "Loading Game statistics data is unsuccesfull");
                    return;
                } else {
                    itemCount = item.size();
                    setUpViews((ArrayList<GameStatisticsDataObject>) (gameStatsObj
                            .getGameStatistics()));
                }
            } else {
                Log.e(TAG, "GameStatsDataObject search is empty");
            }
        } else {
            Log.e(TAG, "GameStatistics Object is null");
        }
    }

    private void setUpScoreRow(ArrayList<GameStatisticsDataObject> items) {
        if (!((items.get(0).getCategories()).isEmpty())) {
            ArrayList<String> dataCat_1 = new ArrayList<String>();
            ArrayList<String> dataCat_2 = new ArrayList<String>();
            ArrayList<String> dataCat_3 = new ArrayList<String>();
            ArrayList<String> dataCat_4 = new ArrayList<String>();
            ArrayList<String> dataCat_5 = new ArrayList<String>();
            ArrayList<String> dataCat_6 = new ArrayList<String>();

            for (int i = 0; i < itemCount; i++) {
                dataCat_1.add(items.get(i).getCategories().get(0)
                        .getCat_earned_points());
                dataCat_2.add(items.get(i).getCategories().get(1)
                        .getCat_earned_points());
                dataCat_3.add(items.get(i).getCategories().get(2)
                        .getCat_earned_points());
                dataCat_4.add(items.get(i).getCategories().get(3)
                        .getCat_earned_points());
                dataCat_5.add(items.get(i).getCategories().get(4)
                        .getCat_earned_points());
            }

            setUpPointRow(R.id.tr_cat1, items.get(0).getCategories().get(0)
                    .getImagewide(), dataCat_1, true);
            setUpPointRow(R.id.tr_cat2, items.get(0).getCategories().get(1)
                    .getImagewide(), dataCat_2, false);
            setUpPointRow(R.id.tr_cat3, items.get(0).getCategories().get(2)
                    .getImagewide(), dataCat_3, true);
            setUpPointRow(R.id.tr_cat4, items.get(0).getCategories().get(3)
                    .getImagewide(), dataCat_4, false);
            setUpPointRow(R.id.tr_cat5, items.get(0).getCategories().get(4)
                    .getImagewide(), dataCat_5, true);
            setUpPointRow(R.id.tr_cat6, items.get(0).getCategories().get(5)
                    .getImagewide(), dataCat_6, false);
        } else {
            Log.e(TAG, "Something is wrong in Loading GamesStats");
        }

    }

    private void setUpPointRow(int id, String image_name,
            ArrayList<String> values, boolean isOdd) {
        View v = findViewById(id);

        String url = getString(R.string.base_url)+"/images/categoryv2/";
        if (id == R.id.tr_points) {
            v.findViewById(R.id.iv_cat_pic).setVisibility(View.GONE);
            ((TextView) v.findViewById(R.id.tv_text)).setText("Total Points:");
            //v.setBackgroundResource(R.drawable.i_scoreboard_box_middle_red);
        } else {
            if (isOdd) {
                v.setBackgroundColor(Color.DKGRAY);
            } else {
                v.setBackgroundColor(Color.BLACK);
            }
            // ((TextView)v.findViewById(R.id.tv_text)).setText(cat_name);
//            ((CustomImageView) v.findViewById(R.id.iv_cat_pic)).loadURLImage(
//                    url + image_name, true);
            loader.DisplayImage(url + image_name, ((ImageView) v.findViewById(R.id.iv_cat_pic)), false);
        }
        int[] ids = {
                R.id.tv_scr1, R.id.tv_scr2, R.id.tv_scr3, R.id.tv_scr4
        };
        TextView text = null;
        for (int j = 0; j < itemCount; j++) {
            v.findViewById(ids[j]).setVisibility(View.GONE);
        }
        
        if (id != R.id.tr_cat6) {
            for (int i = 0; i < values.size(); i++) {
                text = (TextView) v.findViewById(ids[i]);
                if(TextUtils.isEmpty(values.get(i))== false)
                	text.setVisibility(View.VISIBLE);
                text.setTextColor(Color.WHITE);
                text.setText(values.get(i));
            }
        }
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

    
    private void setUpProfileRow(ArrayList<GameStatisticsDataObject> items) {
        int[] ids = {
                R.id.profile1, R.id.profile2, R.id.profile3,
                R.id.profile4
        };
        ImageView profile_image = null;
        CustomTextView profile_text = null;
        View v = null;
        for (int j = 0; j < 4; j++) {
            findViewById(ids[j]).setVisibility(View.GONE);
        }
        for (int i = 0; i < itemCount; i++) {
        	if(items.get(i).getGamplaid().equals(mGamePlaId)) {
        		int chatcount = 0;
                try {
        			chatcount = Integer.parseInt(items.get(i).getChat_number());
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
        	}
            v = findViewById(ids[i]);
            v.setVisibility(View.VISIBLE);
            profile_image = (ImageView) v.findViewById(R.id.iv_pic);
            profile_image.setVisibility(View.VISIBLE);
            profile_image.setTag(items.get(i).getPlaid());
            profile_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (v.getTag() !=null) {
						openPlayerStat(v.getTag()+"");
					}
					
				}
			});
            
            if(items.get(i).getFbid().contains(".png") || items.get(i).getFbid().contains(".jpg")){
            	loader.DisplayImage(getString(R.string.url_images)
                        + items.get(i).getFbid() , profile_image, false);
            }else
            	loader.DisplayImage("https://graph.facebook.com/"
                    + items.get(i).getFbid() + "/picture", profile_image, false);
            
            profile_text = (CustomTextView) v.findViewById(R.id.tv_name);
            profile_text.setVisibility(View.VISIBLE);
            profile_text.setText(items.get(i).getShortname());
            profile_text.setTextColor(Color.WHITE);
            if ((items.get(i).getStatus()).equals("ENDED")
                    ) {
                ((ImageView) v.findViewById(R.id.player_no_more))
                        .setVisibility(View.VISIBLE);
                ((ImageView) v.findViewById(R.id.player_no_more)).setBackgroundResource(R.drawable.icon_no_more_play);
                ((ImageView) v.findViewById(R.id.player_no_more)).bringToFront();
            }
            else if ((items.get(i).getStatus()).equals("DROPPED")) {
                ((ImageView) v.findViewById(R.id.player_no_more))
                        .setVisibility(View.VISIBLE);
                ((ImageView) v.findViewById(R.id.player_no_more)).setBackgroundResource(R.drawable.icon_resigned);
                ((ImageView) v.findViewById(R.id.player_no_more)).bringToFront();
            }

            ImageView ribbon = (ImageView) v
                    .findViewById(R.id.iv_ribbon);

            if ((items.get(i).getPlace()).equals("1"))
                ribbon.setBackgroundResource(R.drawable.ribbon_blue);
            else if ((items.get(i).getPlace()).equals("2"))
                ribbon.setBackgroundResource(R.drawable.ribbon_red);
            else if (items.get(i).getPlace().equals("3"))
                ribbon.setBackgroundResource(R.drawable.ribbon_yellow);

            if ((items.get(i).getAward_badge_image()) != null
                    && !((items.get(i).getAward_badge_image()).length() == 0)) {
                String url = getString(R.string.url_images)
                        + (items.get(i).getAward_badge_image());
//                ((CustomImageView) v.findViewById(R.id.iv_award)).loadURLImage(
//                        url, true);
                loader.DisplayImage(url, ((ImageView) v.findViewById(R.id.iv_award)), false);
            }
        }
    }

    private void setUpScoreRowforGameBoard(
            ArrayList<GameStatisticsDataObject> items) {
        ArrayList<String> dataCat_1 = new ArrayList<String>();
        ArrayList<String> dataCat_2 = new ArrayList<String>();
        ArrayList<String> dataCat_3 = new ArrayList<String>();
        ArrayList<String> dataCat_4 = new ArrayList<String>();
        ArrayList<String> dataCat_5 = new ArrayList<String>();
        ArrayList<String> dataCat_6 = new ArrayList<String>();

        for (int i = 0; i < itemCount; i++) {
            dataCat_1.add((items.get(i).getCategories().get(0)
                    .getCat_point_value())
                    + ":"
                    + (items.get(i).getCategories().get(0).getUnavailable()));
            dataCat_2.add((items.get(i).getCategories().get(1)
                    .getCat_point_value())
                    + ":"
                    + (items.get(i).getCategories().get(1).getUnavailable()));
            dataCat_3.add((items.get(i).getCategories().get(2)
                    .getCat_point_value())
                    + ":"
                    + (items.get(i).getCategories().get(2).getUnavailable()));
            dataCat_4.add((items.get(i).getCategories().get(3)
                    .getCat_point_value())
                    + ":"
                    + (items.get(i).getCategories().get(3).getUnavailable()));
            dataCat_5.add((items.get(i).getCategories().get(4)
                    .getCat_point_value())
                    + ":"
                    + (items.get(i).getCategories().get(4).getUnavailable()));
            dataCat_6.add(items.get(i).getCategories().get(5).getUnavailable());
        }

        setUpPointRowforGameBoard(R.id.tr_cat1, dataCat_1, false);
        setUpPointRowforGameBoard(R.id.tr_cat2, dataCat_2, false);
        setUpPointRowforGameBoard(R.id.tr_cat3, dataCat_3, false);
        setUpPointRowforGameBoard(R.id.tr_cat4, dataCat_4, false);
        setUpPointRowforGameBoard(R.id.tr_cat5, dataCat_5, false);
        setUpPointRowforGameBoard(R.id.tr_cat6, dataCat_6, true);

    }

    private void setUpPointRowforGameBoard(int id, ArrayList<String> values,
            boolean isCategorySurprise) {
        View v = findViewById(id);

        int[] ids = {
                R.id.tv_scr1, R.id.tv_scr2, R.id.tv_scr3, R.id.tv_scr4
        };
        TextView text = null;
        for (int j = 0; j < itemCount; j++) {
            v.findViewById(ids[j]).setVisibility(View.GONE);
        }
        for (int i = 0; i < values.size(); i++) {
            String str = values.get(i);
            if (!isCategorySurprise) {
                text = (TextView) v.findViewById(ids[i]);
                text.setVisibility(View.VISIBLE);
                text.setText(str.substring(0, (str.length() - 2)));
                if ((str.charAt(str.length() - 1)) == '1') {
                    text.setTextColor(getResources().getColor(R.color.red));
                }
            } else {
            	ImageView imgView = (ImageView) v
                        .findViewById(ids[i]);
                if (str.equals("1")) {
                    imgView.setVisibility(View.VISIBLE);
                    imgView.setImageResource(R.drawable.scoreboard_redx);
                }
                imgView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setUpScoreRowforScoreBoard(
            ArrayList<GameStatisticsDataObject> items) {
        ArrayList<String> dataCat_1 = new ArrayList<String>();
        ArrayList<String> dataCat_2 = new ArrayList<String>();
        ArrayList<String> dataCat_3 = new ArrayList<String>();
        ArrayList<String> dataCat_4 = new ArrayList<String>();
        ArrayList<String> dataCat_5 = new ArrayList<String>();

        for (int i = 0; i < itemCount; i++) {
            dataCat_1.add(items.get(i).getCategories().get(0)
                    .getCat_earned_points());
            dataCat_2.add(items.get(i).getCategories().get(1)
                    .getCat_earned_points());
            dataCat_3.add(items.get(i).getCategories().get(2)
                    .getCat_earned_points());
            dataCat_4.add(items.get(i).getCategories().get(3)
                    .getCat_earned_points());
            dataCat_5.add(items.get(i).getCategories().get(4)
                    .getCat_earned_points());
        }

        setUpPointRowforScoreBoard(R.id.tr_cat1, dataCat_1);
        setUpPointRowforScoreBoard(R.id.tr_cat2, dataCat_2);
        setUpPointRowforScoreBoard(R.id.tr_cat3, dataCat_3);
        setUpPointRowforScoreBoard(R.id.tr_cat4, dataCat_4);
        setUpPointRowforScoreBoard(R.id.tr_cat5, dataCat_5);

    }

    private void setUpPointRowforScoreBoard(int id, ArrayList<String> values) {
        View v = findViewById(id);

        int[] ids = {
                R.id.tv_scr1, R.id.tv_scr2, R.id.tv_scr3, R.id.tv_scr4
        };
        TextView text = null;
        for (int j = 0; j < itemCount; j++) {
            v.findViewById(ids[j]).setVisibility(View.GONE);
        }

        for (int i = 0; i < values.size(); i++) {
            text = (TextView) v.findViewById(ids[i]);
            text.setVisibility(View.VISIBLE);
            text.setText(values.get(i));
            text.setTextColor(getResources().getColor(R.color.white));
        }

    }

    public void showGameboard(View v) {
        if ((gameStatsObject != null)
                && !((gameStatsObject.getGameStatistics()).isEmpty())) {

        	findViewById(R.id.tr_points).setVisibility(View.VISIBLE);
            findViewById(R.id.tr_cat6).setVisibility(View.VISIBLE);
            setUpScoreRowforGameBoard((ArrayList<GameStatisticsDataObject>) gameStatsObject
                    .getGameStatistics());
        } else {
//            Toast.makeText(ScoreboardActivity.this, "Something is wrong...",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    Dialog d;

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
                            Toast.makeText(ScoreboardActivity.this,
                                    "Resign is Unsuccessfull",
                                    Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asynTask.execute();
    }

    public void showPlaybyplay(View v) {
        showPlaybyPlay();
    }

    private void showPlaybyPlay() {
        Intent i = getIntent();
        if (isResigned)
            i.putExtra("openPlayByPlay", "1");
        else
            i.putExtra("openPlayByPlay", "2");

        setResult(RESULT_OK, i);
        ScoreboardActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        if (isResigned) {
            i.putExtra("hasResigned", "1");
            setResult(RESULT_OK, i);
        } else {
            i.putExtra("hasResigned", "0");
            setResult(RESULT_CANCELED, i);
        }

        ScoreboardActivity.this.finish();
    }
    public void goHome(View v) {
        //startActivity(new Intent(getApplicationContext(), HomeScreen.class));
        ScoreboardActivity.this.finish();
    }
}
