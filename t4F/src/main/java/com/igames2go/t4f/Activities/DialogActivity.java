package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra("title");
		String image = getIntent().getStringExtra("image");
		String message = getIntent().getStringExtra("message");
		int not_entered = getIntent().getIntExtra("not_entered", 0);
		final int btn_text = getIntent().getIntExtra("btn_text", R.drawable.button_ok);
		LayoutInflater inflater = getLayoutInflater();
        setContentView(R.layout.layout_dialog);
        findViewById(R.id.divider).setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title))
            ((TextView) findViewById(android.R.id.text1)).setText(title);
        else {
            (findViewById(android.R.id.text1)).setVisibility(View.GONE);

        }

        if(not_entered == 1){
        	((ImageView) findViewById(R.id.image)).setImageResource(R.drawable.alert_not_entered);
        }else{
	        if (image != null) {
	        	ImageLoader loader = new ImageLoader(this);
	        	loader.DisplayImage(image, ((ImageView) findViewById(R.id.image)), false);
	//            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(image);
	        }else{
	        	((ImageView) findViewById(R.id.image)).setVisibility(View.GONE);
	        }
        }
        TextView tv = ((TextView) findViewById(android.R.id.text2));
        android.view.ViewGroup.LayoutParams layoutParams =  tv.getLayoutParams();
        layoutParams.width = android.view.ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//        message = "asdasdsadjhwef<br/> aiwejhfdiowefwefuh<br> aiuwefguwegfuiwegf\n waeufgiuwef weugfiuwefguw8egf aweufiuawefg8we fjugfeu  weu8fg uwefguygwef ukhgwfeuhwef uhgfeuhwef juwegfuwegfu waeufgweuifgweiu waejhugweufgweufgwe wjeugfweufguwefguwef weukfgweufgweufguwef wejhufweufgweufguwefg wejugfuwefg wefkuwe fugwe fiugwef iwefukwegfu asdasdsadjhwef aiwejhfdiowefwefuh aiuwefguwegfuiwegf waeufgiuwef weugfiuwefguw8egf aweufiuawefg8we fjugfeu  weu8fg uwefguygwef ukhgwfeuhwef uhgfeuhwef juwegfuwegfu waeufgweuifgweiu waejhugweufgweufgwe wjeugfweufguwefguwef weukfgweufgweufguwef wejhufweufgweufguwefg wejugf7uwefg wefkuwe fugwe fiugwef iwefukwegfu777";
        tv.setText(Html.fromHtml(message));
        if (btn_text != -1) {
            ((Button) findViewById(android.R.id.button2))
                    .setBackgroundResource(btn_text);
            ((Button) findViewById(android.R.id.button2))
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                        	
//                            DialogUtil.mListener.onButtonClick(btn_text);
                        	finish();
                            // TODO Auto-generated method stub

                        }
                    });
        } else {
//            view.findViewById(android.R.id.button2);
        }

	}
}
