
package com.igames2go.t4f.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "hobostd.otf");
        this.setTypeface(tf);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "hobostd.otf");
        this.setTypeface(tf);
    }

    public CustomTextView(Context context) {
        super(context);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "hobostd.otf");
        this.setTypeface(tf);
    }

}
