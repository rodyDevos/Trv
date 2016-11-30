package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.GamesDataObject;
import com.igames2go.t4f.tasks.DeclineGameTask;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GameAlertDialogActivity extends Activity {
	GamesDataObject obj;
	int type;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		setResult(resultCode, data);
		finish();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type = getIntent().getIntExtra("type", 1);
		String title = getIntent().getStringExtra("title");
		String image = getIntent().getStringExtra("image");
		String message = getIntent().getStringExtra("message");
		final int btn_text = getIntent().getIntExtra("btn_text", -1);
		final int btn_text2 = getIntent().getIntExtra("btn_text2",-1);
		obj= (GamesDataObject) getIntent().getSerializableExtra("GamesDataObject");
		LayoutInflater inflater = getLayoutInflater();
        setContentView(R.layout.layout_alert_dialog);
        findViewById(R.id.divider).setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title))
            ((TextView) findViewById(android.R.id.text1)).setText(title);
        else {
            (findViewById(android.R.id.text1)).setVisibility(View.GONE);

        }

        if (image != null) {
        	ImageLoader loader = new ImageLoader(this);
        	loader.DisplayImage(image, ((ImageView) findViewById(R.id.image)), false);
//            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(image);
        }
        TextView tv = ((TextView) findViewById(android.R.id.text2));
        android.view.ViewGroup.LayoutParams layoutParams =  tv.getLayoutParams();
        layoutParams.width = android.view.ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//        message = "asdasdsadjhwef<br/> aiwejhfdiowefwefuh<br> aiuwefguwegfuiwegf\n waeufgiuwef weugfiuwefguw8egf aweufiuawefg8we fjugfeu  weu8fg uwefguygwef ukhgwfeuhwef uhgfeuhwef juwegfuwegfu waeufgweuifgweiu waejhugweufgweufgwe wjeugfweufguwefguwef weukfgweufgweufguwef wejhufweufgweufguwefg wejugfuwefg wefkuwe fugwe fiugwef iwefukwegfu asdasdsadjhwef aiwejhfdiowefwefuh aiuwefguwegfuiwegf waeufgiuwef weugfiuwefguw8egf aweufiuawefg8we fjugfeu  weu8fg uwefguygwef ukhgwfeuhwef uhgfeuhwef juwegfuwegfu waeufgweuifgweiu waejhugweufgweufgwe wjeugfweufguwefguwef weukfgweufgweufguwef wejhufweufgweufguwefg wejugf7uwefg wefkuwe fugwe fiugwef iwefukwegfu777";
        tv.setText(Html.fromHtml(message));
        if (btn_text != -1) {
            ((ImageView) findViewById(android.R.id.button2))
                    .setImageResource(btn_text);
            ((ImageView) findViewById(android.R.id.button2))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                        	
//                            DialogUtil.mListener.onButtonClick(btn_text);
                        	
//                        	GamesDataObject obj = (GamesDataObject) v.getTag();
                        	if(type == 1) {
                        		finish();
                        		Intent intent = new Intent(getApplicationContext(),
                                        GameOptionsActivity.class);
                                intent.putExtra("game_id", obj.getGam_id() + "");
                                intent.putExtra("gameplaid", obj.getGampla_id() + "");
                                intent.putExtra("lifeline", obj.getGampla_lifelines() + "");
                                startActivity(intent);
                        	}
                        	if(type == 2) {
                        		Intent intent = new Intent(getApplicationContext(),
                        				ScoreboardActivity.class);
                        		intent.putExtra("game_id", obj.getGam_id() + "");
                        		intent.putExtra("game_pla_id", obj.getGampla_id() + "");
                        		if ((obj.getGampla_status()).equals(HomeScreen.GAME_PS_DROPPED)
                        				|| (obj.getGampla_status()).equals(HomeScreen.GAME_ENDED) || (obj.getGampla_status()).equals(HomeScreen.GAME_PS_WON))
                        			intent.putExtra("resigned", "1");
                        		else
                        			intent.putExtra("resigned", "0");

                        		startActivityForResult(intent, type);
                        	}
                        }
                    });
        } else {
        	((ImageView) findViewById(android.R.id.button2)).setVisibility(View.GONE);
        }
        if (btn_text2 != -1) {
            ((ImageView) findViewById(android.R.id.button1))
                    .setImageResource(btn_text2);
            ((ImageView) findViewById(android.R.id.button1))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                        	
//                            DialogUtil.mListener.onButtonClick(btn_text);
                        	DeclineGameTask task = new DeclineGameTask(GameAlertDialogActivity.this, obj.getGam_id(), true, new LoadingListener() {
								
								@Override
								public void onLoadingComplete() {
									// TODO Auto-generated method stub
									finish();
								}
								
								@Override
								public void onLoadingComplete(Object obj) {
									((T4FApplication)getApplication()).refresh = true;
									finish();
								}
								
								@Override
								public void onError(Object error) {
									// TODO Auto-generated method stub
									finish();
								}
							});
                        	task.execute();
                            // TODO Auto-generated method stub

                        }
                    });
        } else {
        	((ImageView) findViewById(android.R.id.button1)).setVisibility(View.GONE);
        }

	}
	
}
