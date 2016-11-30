
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.Categories;
import com.igames2go.t4f.data.CategoriesDataObject;
import com.igames2go.t4f.data.GameStatistics;
import com.igames2go.t4f.data.GameStatisticsDataObject;
import com.igames2go.t4f.tasks.LoadCategories;
import com.igames2go.t4f.tasks.LoadGameStatistics;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.HttpManager;
import com.igames2go.t4f.utils.ImageLoader;
//import com.igames2go.t4f.view.ImageView;
import com.igames2go.t4f.view.CustomTextView;

import org.apache.http.client.ClientProtocolException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GameOptionsActivity extends AdActivity {

    private final String TAG = "T4F";
    private View dialoglayout;
    private TextView dailogcategory_tv;
    private String gameID;
    private String gameplaid;
    //private String gameStatus;
    private boolean isSurprise = false;
    private boolean isNewType = true;
    private int lifeline = 3;
    private ImageLoader loader;
    private int width = 0;
    private int height = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gameoptions);
        gameID = getIntent().getStringExtra("game_id");
        gameplaid = getIntent().getStringExtra("gameplaid");
        //gameStatus = getIntent().getStringExtra("game_status");
        try {
            lifeline = Integer.parseInt(getIntent().getStringExtra("lifeline"));
        } catch (NumberFormatException e) {
            lifeline = 3;
        }
        loadCategory();
        loader = new ImageLoader(this);
        loadPlayers();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
        width = (int) (displaymetrics.widthPixels*.45);
        height = (int) (width/1.79);
        ImageLoader.readMap(getApplicationContext());

    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ImageLoader.writeMap(getApplicationContext());
    }


    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
        	isSurprise = false;
            askQuestion((CategoriesDataObject) v.getTag(), true);
        }
    };

    private OnClickListener listenerSuprise = new OnClickListener() {

        @Override
        public void onClick(View v) {
        	isSurprise = true;
            askQuestion((CategoriesDataObject) v.getTag(), true);
        }
    };

    private OnClickListener listener1 = new OnClickListener() {

        @Override
        public void onClick(View v) {
            askQuestion((CategoriesDataObject) v.getTag(), false);
        }
    };

    private OnClickListener emptylistener = new OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    Dialog dialog;

    private void askQuestion(final CategoriesDataObject obj,
            boolean showSelectAgain) {
        dialog = DialogUtil.createDialog_Ready_to_go(this,
                obj.getCatnamelong(), showSelectAgain,
                new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                String surpriseStr = null;

                                if (isSurprise)
                                    surpriseStr = "1";
                                else
                                    surpriseStr = "0";

                                Intent intent = new Intent(
                                        getApplicationContext(),
                                        QuestionActivity.class);
                                intent.putExtra("game_id", gameID);
                                //intent.putExtra("game_status", gameStatus);
                                intent.putExtra("category_id", obj.getCatid());
                                intent.putExtra("category_name",
                                        obj.getCatnamelong());
                                intent.putExtra("game_pla_id", gameplaid);
                                intent.putExtra("isSurprise", surpriseStr);
                                intent.putExtra("lifeline", lifeline);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();

                            }
                        });
                    }
                });
        dialog.show();

    }

    private void loadCategory() {
        LoadCategories asyncTask = new LoadCategories(this, gameID,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handleData((Categories) obj);
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }

    private void loadPlayers() {
        LoadGameStatistics asyncTask = new LoadGameStatistics(this, gameID, false,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handlePlayersData((GameStatistics) obj);
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }

    private void handlePlayersData(GameStatistics gamestats) {
        if (gamestats != null) {
            List<GameStatisticsDataObject> playersstats = gamestats
                    .getGameStatistics();
            if (playersstats != null && !playersstats.isEmpty()) {
                if (playersstats.get(0).getPlaid() != "0") {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.players_container);
                    for (GameStatisticsDataObject obj : playersstats) {

                        LinearLayout ll1 = (LinearLayout) getLayoutInflater()
                                .inflate(R.layout.layout_players, null);
                        ll1.findViewById(R.id.frame1).setVisibility(
                                View.VISIBLE);
                        ll.addView(ll1);
                        View view = new View(getApplicationContext());
                        view.setLayoutParams(new LinearLayout.LayoutParams(5,
                                1));
                        ll.addView(view);

                        ImageView player_image = (ImageView) ll1
                                .findViewById(R.id.iv_pic);
                        CustomTextView player_name = (CustomTextView) ll1
                                .findViewById(R.id.tv_name);
                        player_name.setVisibility(View.VISIBLE);
                        player_name.setTextColor(getResources().getColor(R.color.nav_bar_title_color));
                        CustomTextView player_game_points = (CustomTextView) ll1
                                .findViewById(R.id.player_game_points);
                        ImageView ribbon = (ImageView) ll1
                                .findViewById(R.id.img_ribbon);
                        
                        loader.displayPlayerImage(obj.getFbid(), player_image, false);
                        
                        player_name.setText(obj.getShortname());
                        player_game_points.setText(obj.getGampla_point_tot());

                        if ((obj.getPlace()).equals("1"))
                            ribbon.setBackgroundResource(R.drawable.ribbon_blue);
                        else if ((obj.getPlace()).equals("2"))
                            ribbon.setBackgroundResource(R.drawable.ribbon_red);
                        else if (obj.getPlace().equals("3"))
                            ribbon.setBackgroundResource(R.drawable.ribbon_yellow);
                        
                        if((obj.getStatus()).equals("ENDED")) {
                        	((ImageView) ll1
                                    .findViewById(R.id.player_no_more)).setVisibility(View.VISIBLE);
                        	((ImageView) ll1
                                    .findViewById(R.id.player_no_more)).setBackgroundResource(R.drawable.icon_no_more_play);
                    		
                    	}
                    	else if((obj.getStatus()).equals("DROPPED")) {
                    		((ImageView) ll1
                                    .findViewById(R.id.player_no_more)).setVisibility(View.VISIBLE);
                    		((ImageView) ll1
                                    .findViewById(R.id.player_no_more)).setBackgroundResource(R.drawable.icon_resigned);
                    	}
                        ((ImageView) ll1
                                .findViewById(R.id.player_no_more)).bringToFront();
                        
                    }
                } else {
                    Log.e(TAG, "Loading Game statistics is unsuccessful");
                }
            } else {
                Log.e(TAG, "GameStatisticsDataObject  is null or empty");
            }
        } else {
            Log.e(TAG, "gamestats obj is null");
        }
    }

    private void handleData(Categories cat) {
        if (cat != null) {
            if (isNewCategory(cat)) {
                ViewGroup ll = (ViewGroup) findViewById(R.id.option_container);
                ViewGroup view = (ViewGroup) getLayoutInflater().inflate(
                        R.layout.layout_gameoptions_apppart, null);
                ViewGroup.LayoutParams	param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
                param.height = ViewGroup.LayoutParams.FILL_PARENT;
                param.width = ViewGroup.LayoutParams.FILL_PARENT;
                
                view.setLayoutParams(param);
                ll.addView(view);
                FrameLayout btn_category1;
                FrameLayout btn_category2;
                FrameLayout btn_category3;
                FrameLayout btn_category4;
                FrameLayout btn_category5;
                FrameLayout btn_categorysurprise;
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(width, height);
                btn_category1 = (FrameLayout) findViewById(R.id.category_1);
                btn_category2 = (FrameLayout) findViewById(R.id.category_2);
                btn_category3 = (FrameLayout) findViewById(R.id.category_3);
                btn_category4 = (FrameLayout) findViewById(R.id.category_4);
                btn_category5 = (FrameLayout) findViewById(R.id.category_5);
                btn_categorysurprise = (FrameLayout) findViewById(R.id.category_6);
                
                btn_category1.setOnClickListener(listener);
                btn_category2.setOnClickListener(listener);
                btn_category3.setOnClickListener(listener);
                btn_category4.setOnClickListener(listener);
                btn_category5.setOnClickListener(listener);
                btn_categorysurprise.setOnClickListener(listenerSuprise);
                
                btn_category1.setLayoutParams(param1);
                btn_category2.setLayoutParams(param1);
                btn_category3.setLayoutParams(param1);
                btn_category4.setLayoutParams(param1);
                btn_category5.setLayoutParams(param1);
                btn_categorysurprise.setLayoutParams(param1);
                isSurprise = false;

                if (cat.getCategoryPosition1().getCatid() != null) {
                    // setCategoryImage(cat.getCategoryPosition1().getCatimage(),
                    // (ImageView) btn_category1.findViewById(R.id.main_image));
                	
                	loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                            + cat.getCategoryPosition1().getCatimage(),((ImageView) btn_category1
                            .findViewById(R.id.main_image)), true);
//                    ((ImageView) btn_category1
//                            .findViewById(R.id.main_image)).loadURLImage(
//                            getString(R.string.base_url)+"/images/categoryv2/"
//                                    + cat.getCategoryPosition1().getCatimage(),
//                            true);
                    btn_category1.setTag(cat.getCategoryPosition1());
                    ((CustomTextView) btn_category1.findViewById(R.id.tv_count))
                            .setText(cat.getCategoryPosition1()
                                    .getCat_point_value());
                    if ((cat.getCategoryPosition1().getCatx()).equals("1")) {
                        ((ImageView) btn_category1
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category1.setOnClickListener(emptylistener);

                    }
                    if ((cat.getCategoryPosition1().getCatsurprise())
                            .equals("1")) {
                        ((ImageView) btn_category1
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        ((ImageView) btn_categorysurprise
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category1.setOnClickListener(emptylistener);
                        btn_categorysurprise.setOnClickListener(emptylistener);
                    }
                } else
                    return;

                if (cat.getCategoryPosition2().getCatid() != null) {
                    // setCategoryImage(cat.getCategoryPosition2().getCatimage(),
                    // btn_category2);
                	loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                          + cat.getCategoryPosition2().getCatimage(),((ImageView) btn_category2
                                .findViewById(R.id.main_image)), true);
//                    ((ImageView) btn_category2
//                            .findViewById(R.id.main_image)).loadURLImage(
//                            getString(R.string.base_url)+"/images/categoryv2/"
//                                    + cat.getCategoryPosition2().getCatimage(),
//                            true);
                    btn_category2.setTag(cat.getCategoryPosition2());
                    ((CustomTextView) btn_category2.findViewById(R.id.tv_count))
                            .setText(cat.getCategoryPosition2()
                                    .getCat_point_value());
                    if ((cat.getCategoryPosition2().getCatx()).equals("1")) {
                        ((ImageView) btn_category2
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category2.setOnClickListener(emptylistener);
                    }

                    if ((cat.getCategoryPosition2().getCatsurprise())
                            .equals("1")) {
                        ((ImageView) btn_category2
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        ((ImageView) btn_categorysurprise
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category2.setOnClickListener(emptylistener);
                        btn_categorysurprise.setOnClickListener(emptylistener);
                    }
                }

                if (cat.getCategoryPosition3().getCatid() != null) {
                    // setCategoryImage(cat.getCategoryPosition3().getCatimage(),
                    // btn_category3);
                	loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                            + cat.getCategoryPosition3().getCatimage(),((ImageView) btn_category3
                                  .findViewById(R.id.main_image)), true);
//                    ((ImageView) btn_category3
//                            .findViewById(R.id.main_image)).loadURLImage(
//                            getString(R.string.base_url)+"/images/categoryv2/"
//                                    + cat.getCategoryPosition3().getCatimage(),
//                            true);
                    btn_category3.setTag(cat.getCategoryPosition3());
                    ((CustomTextView) btn_category3.findViewById(R.id.tv_count))
                            .setText(cat.getCategoryPosition3()
                                    .getCat_point_value());
                    if ((cat.getCategoryPosition3().getCatx()).equals("1")) {
                        ((ImageView) btn_category3
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category3.setOnClickListener(emptylistener);
                    }
                    if ((cat.getCategoryPosition3().getCatsurprise())
                            .equals("1")) {
                        ((ImageView) btn_category3
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        ((ImageView) btn_categorysurprise
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category3.setOnClickListener(emptylistener);
                        btn_categorysurprise.setOnClickListener(emptylistener);
                    }
                }

                if (cat.getCategoryPosition4().getCatid() != null) {
                    // setCategoryImage(cat.getCategoryPosition4().getCatimage(),
                    // btn_category4);
                	loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                            + cat.getCategoryPosition4().getCatimage(),((ImageView) btn_category4
                                  .findViewById(R.id.main_image)), true);
//                    ((ImageView) btn_category4
//                            .findViewById(R.id.main_image)).loadURLImage(
//                            getString(R.string.base_url)+"/images/categoryv2/"
//                                    + cat.getCategoryPosition4().getCatimage(),
//                            true);
                    btn_category4.setTag(cat.getCategoryPosition4());
                    ((CustomTextView) btn_category4.findViewById(R.id.tv_count))
                            .setText(cat.getCategoryPosition4()
                                    .getCat_point_value());
                    if ((cat.getCategoryPosition4().getCatx()).equals("1")) {
                        ((ImageView) btn_category4
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category4.setOnClickListener(emptylistener);
                    }
                    if ((cat.getCategoryPosition4().getCatsurprise())
                            .equals("1")) {
                        ((ImageView) btn_category4
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        ((ImageView) btn_categorysurprise
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category4.setOnClickListener(emptylistener);
                        btn_categorysurprise.setOnClickListener(emptylistener);
                    }
                }

                if (cat.getCategoryPosition5().getCatid() != null) {
                    // setCategoryImage(cat.getCategoryPosition5().getCatimage(),
                    // btn_category5);
                	loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                            + cat.getCategoryPosition5().getCatimage(),((ImageView) btn_category5
                                  .findViewById(R.id.main_image)), true);
//                    ((ImageView) btn_category5
//                            .findViewById(R.id.main_image)).loadURLImage(
//                            getString(R.string.base_url)+"/images/categoryv2/"
//                                    + cat.getCategoryPosition5().getCatimage(),
//                            true);
                    btn_category5.setTag(cat.getCategoryPosition5());
                    ((CustomTextView) btn_category5.findViewById(R.id.tv_count))
                            .setText(cat.getCategoryPosition5()
                                    .getCat_point_value());
                    if ((cat.getCategoryPosition5().getCatx()).equals("1")) {
                        ((ImageView) btn_category5
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category5.setOnClickListener(emptylistener);
                    }
                    if ((cat.getCategoryPosition5().getCatsurprise())
                            .equals("1")) {
                        ((ImageView) btn_category5
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        ((ImageView) btn_categorysurprise
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_category5.setOnClickListener(emptylistener);
                        btn_categorysurprise.setOnClickListener(emptylistener);
                    }
                }

                if (cat.getCategoryPosition6().getCatid() != null) {
                    // setCategoryImage(cat.getCategoryPosition6().getCatimage(),
                    // btn_categorysurprise);

                    loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                            + cat.getCategoryPosition6().getCatimage(),((ImageView) btn_categorysurprise
                                  .findViewById(R.id.main_image)), true);
//                    ((ImageView) btn_categorysurprise
//                            .findViewById(R.id.main_image)).loadURLImage(
//                            getString(R.string.base_url)+"/images/categoryv2/"
//                                    + cat.getCategoryPosition6().getCatimage(),
//                            true);
                    btn_categorysurprise.setTag(cat.getCategoryPosition6());
                    ((CustomTextView) btn_categorysurprise
                            .findViewById(R.id.tv_count)).setVisibility(View.INVISIBLE);
                    
                    ((CustomTextView) btn_categorysurprise
                            .findViewById(R.id.tv_count)).setText(cat
                            .getCategoryPosition6().getCat_point_value());
                    if ((cat.getCategoryPosition6().getCatx()).equals("1")) {
                        ((ImageView) btn_categorysurprise
                                .findViewById(R.id.main_image_not_available))
                                .setVisibility(View.VISIBLE);
                        btn_categorysurprise.setOnClickListener(emptylistener);
                    }
                }

            } else {
                ViewGroup ll = (ViewGroup) findViewById(R.id.option_container);
                ViewGroup view =(ViewGroup) getLayoutInflater().inflate(
                        R.layout.passed_gameoption, null);
                view.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                ll.addView(view);
                FrameLayout btn_category0;
                btn_category0 = (FrameLayout) findViewById(R.id.category_0);
                btn_category0.setOnClickListener(listener1);
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(width, height);
                btn_category0.setLayoutParams(param1);
                loader.DisplayImage(getString(R.string.base_url)+"/images/categoryv2/"
                        + cat.getCategoryPosition0().getCatimage(),((ImageView) btn_category0
                              .findViewById(R.id.main_image)), true);
//                ((ImageView) btn_category0.findViewById(R.id.main_image))
//                        .loadURLImage(
//                                getString(R.string.base_url)+"/images/categoryv2/"
//                                        + cat.getCategoryPosition0()
//                                                .getCatimage(), true);
                btn_category0.setTag(cat.getCategoryPosition0());
                ((CustomTextView) btn_category0.findViewById(R.id.tv_count))
                        .setText(cat.getCategoryPosition0()
                                .getCat_point_value());
            }
        } else {
            Log.e(TAG, "categories data object is null");
            Toast.makeText(this, "Error in loading Categories",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void onQuestionMarkClick(View v) {
        startActivity(new Intent(this, HelpActivity.class));
    }
    private boolean isNewCategory(Categories cat) {
        if (cat.getCategoryPosition0().getCatnamelong().trim().length() != 0) {
            return false;
        }
        return true;
    }

    private void showCustomDialogBox(int category) {
        LayoutInflater inflater = getLayoutInflater();
        dialoglayout = inflater.inflate(R.layout.dialoglayout_categoryselect,
                (ViewGroup) getCurrentFocus());
        dailogcategory_tv = (TextView) dialoglayout
                .findViewById(R.id.dialog_category_tv);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialoglayout);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (dialoglayout != null)
            ((ViewGroup) dialoglayout.getParent()).removeView(dialoglayout);
    }

    private void setCategoryImage(final String catimage, final ImageView btn) {
        new Runnable() {

            @Override
            public void run() {
                try {

                    InputStream inputstream = (InputStream) HttpManager
                            .getResponse(
                                    getString(R.string.url_images)
                                            + catimage, true);
                    btn.setImageDrawable(new BitmapDrawable(inputstream));

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.run();
    }

    public void goBack(View v) {
        if (dialoglayout != null)
            ((ViewGroup) dialoglayout.getParent()).removeView(dialoglayout);

        GameOptionsActivity.this.finish();

    }

}
