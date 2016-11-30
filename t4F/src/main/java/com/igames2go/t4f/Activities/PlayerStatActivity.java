
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.PlayerStats;
import com.igames2go.t4f.data.PlayerStatsDataObject;
import com.igames2go.t4f.data.PlayerStats_CategoriesData;
import com.igames2go.t4f.tasks.LoadPlayerStats;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.ImageLoader;
//import com.igames2go.t4f.view.CustomImageView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayerStatActivity extends Activity {

    private final String TAG = "T4F";
    private int itemCount = 0;
    private String plaId = null;
    private ImageLoader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(((T4FApplication)getApplication()).getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN) {
        	ImageView  iv= new ImageView(this);
        	iv.setBackgroundResource(R.drawable.trans);
        	setContentView(iv);
        	DialogUtil.createDialog_title_bitmap_message(this, "Player Statistics not available", null, getString(R.string.no_login_player_statistics_message), R.drawable.button_ok, new ButtonClickListener() {
				
				@Override
				public void onButtonClick(int s) throws NullPointerException {
					
					
				}
			});
        	finish();
        }
        setContentView(R.layout.layout_player_stat);
        loader = new ImageLoader(this);
        plaId = getIntent().getStringExtra("player_id");
        if (plaId != null && !(plaId.length() == 0))
            loadPlayerStatisticsData();
        else
            Toast.makeText(this, "PlayerId is null", Toast.LENGTH_LONG).show();
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }

    private void setUpViews(ArrayList<PlayerStatsDataObject> items) {
        setUpProfileRow(items);
        setUpScoreRow(items);

    }

    private void setUpScoreRow(ArrayList<PlayerStatsDataObject> items) {
        ArrayList<PlayerStats_CategoriesData> obj = (ArrayList<PlayerStats_CategoriesData>) items
                .get(0).getCategories();
        itemCount = obj.size();
        if (itemCount > 0) {
            for (int i = 0; i < itemCount; i++) {
                setUpPointRow(obj.get(i).getImagewide(),
                        new String[] {
                				String.format("%,d", obj.get(i).getCatasked()),
                                obj.get(i).getPercentright()+"%"
                        },
                        (i % 2 == 0) ? true : false);
            }
        } else {
            Log.e(TAG, "Categories List is  empty");
        }

    }

    private void setUpPointRow(String image_name, String[] values, boolean isOdd) {
        View v = getLayoutInflater().inflate(R.layout.include_pla_stats_row,
                null);
        String url = getString(R.string.base_url)+"/images/categoryv2/";
        if (isOdd) {
            v.setBackgroundColor(Color.DKGRAY);
        } else {
            v.setBackgroundColor(Color.BLACK);
        }
//        ((CustomImageView) v.findViewById(R.id.iv_cat_pic)).loadURLImage(url
//                + image_name, false);
        loader.DisplayImage(url+ image_name, ((ImageView) v.findViewById(R.id.iv_cat_pic)), false);
        int[] ids = {
                R.id.tv_scr1, R.id.tv_scr2
        };
        TextView text = null;

        for (int i = 0; i < values.length; i++) {
            text = (TextView) v.findViewById(ids[i]);
            text.setVisibility(View.VISIBLE);
            text.setText(values[i]);
        }
        ((TableLayout) findViewById(R.id.tb_scoreboard)).addView(v);
    }

    private void loadPlayerStatisticsData() {
        LoadPlayerStats asynTask = new LoadPlayerStats(this, plaId,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handleData((PlayerStats) obj);
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

    private void handleData(PlayerStats playerStatsObj) {
        if (playerStatsObj != null) {
            ArrayList<PlayerStatsDataObject> item = (ArrayList<PlayerStatsDataObject>) playerStatsObj
                    .getPlayerStatistics();
            if (!item.isEmpty()) {
                if ((item.get(0).getPlaid()).equals("0")) {
                    Log.e(TAG, "Loading Player statistics data is unsuccesfull");
                    return;
                } else {
                    setUpViews(item);

                }
            } else {
                Log.e(TAG, "PlayerStatsDataObject search is empty");
            }
        } else {
            Log.e(TAG, "PlayerStats Object is null");
        }
    }

    private void setUpProfileRow(ArrayList<PlayerStatsDataObject> items) {
        PlayerStatsDataObject obj = items.get(0);
        ImageView profile_image = null;
        TextView profile_text = null;

        findViewById(R.id.pla_profile).setVisibility(View.VISIBLE);
        profile_image = (ImageView) findViewById(R.id.iv_pic);
        profile_image.setVisibility(View.VISIBLE);
        // profile_image.setLayoutParams(new FrameLayout.LayoutParams(30, 30));
//        profile_image.loadURLImage("https://graph.facebook.com/" + obj.getFbid()
//                + "/picture", true);
        
        if(obj.getFbid().contains(".png") || obj.getFbid().contains(".jpg")){
        	loader.DisplayImage(getString(R.string.url_images)
                    + obj.getFbid() , profile_image, false);
        }else
        	loader.DisplayImage("https://graph.facebook.com/"
                + obj.getFbid() + "/picture", profile_image, false);
        
        findViewById(R.id.tv_name).setVisibility(View.GONE);
        
        //profile_text = (TextView) findViewById(R.id.tv_name);
        
        profile_text = (TextView) findViewById(R.id.tv_name1);
        
        int maxLength = 20;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        profile_text.setFilters(fArray);
//        android.view.ViewGroup.LayoutParams tvparam =  profile_text.getLayoutParams();
//        tvparam.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
//                (float) 80, getResources().getDisplayMetrics());
        profile_text.setVisibility(View.VISIBLE);
        profile_text.setText(obj.getFbname());
//        profile_text.setTextSize(10);

        ((TextView) findViewById(R.id.tv_won_played)).setText(String.format("%,d", obj.getGameswon())
                + "/" + String.format("%,d", obj.getGamesplayed()));
        
        TextView tv1 = ((TextView) findViewById(R.id.tv_scored));
        TextView tv2 = ((TextView) findViewById(R.id.tv_remaining));
        TextView tv3 = ((TextView) findViewById(R.id.tv_ranking));
        TextView tv4 = ((TextView) findViewById(R.id.tv_points));
        
        tv1.setText(String.format("%,d", obj.getLevel_points_so_far()));
        //tv2.setText(String.format("%,d", obj.getLevel_points_to_go()));
        tv3.setText(obj.getRanking());
        tv4.setText(String.format("%,d", obj.getLevel_points_so_far()));
        
        int weight1 = obj.getLevel_points_so_far();
        int weight2 = obj.getLevel_points_to_go();
        if(weight1 < 3200){
        	weight1 = 3200;
        }
        LinearLayout.LayoutParams param =  new LayoutParams(0, tv1.getLayoutParams().height, weight1);
        tv1.setLayoutParams(param);
        
        LinearLayout.LayoutParams param1 =  new LayoutParams(0, tv2.getLayoutParams().height, weight2);
        tv2.setLayoutParams(param1);
        String url = getString(R.string.url_images)
                + (obj.getAward_badge_image());
        loader.DisplayImage(url, ((ImageView) findViewById(R.id.iv_award_badge)), false);
//        ((CustomImageView) findViewById(R.id.iv_award_badge)).loadURLImage(url,
//                true);

    }

    public void onBack(View v)
    {
        PlayerStatActivity.this.finish();
    }

}
