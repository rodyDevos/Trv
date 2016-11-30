
package com.igames2go.t4f.view;

import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class CustomImageView extends ImageView {

    public CustomImageView(Context context) {
        super(context);
        // Typeface tf = Typeface.createFromAsset(context.getAssets(),
        // "hobostd.otf");
        // this.setTypeface(tf);
        ctx = context;
        onConstructorDone();
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Typeface tf = Typeface.createFromAsset(context.getAssets(),
        // "hobostd.otf");
        // this.setTypeface(tf);
        ctx = context;
        onConstructorDone();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Typeface tf = Typeface.createFromAsset(context.getAssets(),
        // "hobostd.otf");
        // this.setTypeface(tf);
        ctx = context;
        onConstructorDone();
    }

    private void onConstructorDone() {
        mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundID = mShortPlayer.load(ctx, R.raw.click, 1);
        mShortPlayer.setOnLoadCompleteListener(new OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                    int status) {
                // TODO Auto-generated method stub

            }
        });
    }

//    public void loadURLImage(final String URL, final boolean isSrc) {
//        Runnable r = new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    InputStream is = (InputStream) HttpManager.getResponse(URL,
//                            true);
//                    
//                    
//                    if (isSrc)
//                        CustomImageView.this
//                                .setImageDrawable(new BitmapDrawable(is));
//                    else
//                        CustomImageView.this
//                                .setBackgroundDrawable(new BitmapDrawable(is));
//                } catch (NullPointerException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (ClientProtocolException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//        };
//        Thread t = new Thread(r);
//        t.start();
//    }

    @Override
    public void setOnClickListener(final android.view.View.OnClickListener l) {

        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                l.onClick(v);
//                Toast.makeText(ctx, "setOnClick", 1000).show();
                if(T4FApplication.soundSettting)	
                	mShortPlayer.play(soundID, 0.99f, 0.99f, 0, 0, 1);

            }
        };

        super.setOnClickListener(listener);
    }

    private SoundPool mShortPlayer;
    private int soundID;
    private Context ctx;
}
