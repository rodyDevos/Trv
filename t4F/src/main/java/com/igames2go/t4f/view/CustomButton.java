
package com.igames2go.t4f.view;

import com.igames2go.t4f.R;
import com.igames2go.t4f.Application.T4FApplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CustomButton extends Button {

    private Context ctx;

    public CustomButton(Context context) {
        super(context);
        ctx = context;
        onConstructorDone();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        onConstructorDone();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ctx = context;
        onConstructorDone();
    }

    private void onConstructorDone() {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), "hobostd.otf");
        this.setTypeface(tf);
        setTextColor(Color.BLACK);
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

    @Override
    public void setOnClickListener(final android.view.View.OnClickListener l) {

        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                l.onClick(v);
                if(T4FApplication.soundSettting)
                	mShortPlayer.play(soundID, 0.99f, 0.99f, 1, 0, 1);

            }
        };

        super.setOnClickListener(listener);
    }

    private SoundPool mShortPlayer;
    private int soundID;
}
