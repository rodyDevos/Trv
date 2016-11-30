/*
 * 文件描述:
 * author：zbtu
 * date：2012-10-31
 */
package com.igames2go.t4f.view;

import java.util.HashMap;

import com.igames2go.t4f.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;


 
public class SideBarView extends View {
	private boolean isPressed = false;
	private static final char[] mLetter = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	/**
	 * @param context
	 */
	public SideBarView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SideBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SideBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	int textHeight;
	@Override
	public void draw(Canvas canvas) {
		Paint paint1 = new Paint();
		super.draw(canvas);
		if(isPressed)
			paint1.setColor(Color.parseColor("#55555555"));
//			canvas.drawColor(Color.parseColor("#555555"));
		else
			paint1.setColor(Color.TRANSPARENT);
//			canvas.drawColor(Color.TRANSPARENT);
		
		RectF rect = new RectF(0, 0, getWidth(), getHeight());
		canvas.drawRoundRect(rect, 13, 13, paint1);
//		mCanvas.drawColor(Color.TRANSPARENT);
		Paint paint = new Paint();
		 textHeight = getHeight() / mLetter.length;
		final int width = getWidth();
		for (int i = 0; i < mLetter.length; i++) {
			paint.setColor(Color.GRAY);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(getResources().getDimension(R.dimen.textsize_small));
			final float xPos = width / 2
					- paint.measureText(String.valueOf(mLetter[i])) / 2;

			final float yPos = textHeight * i + textHeight;
			
			canvas.drawText(String.valueOf(mLetter[i]), xPos, yPos, paint);
//			canvas.drawLine(0, yPos, getWidth(), yPos, paint); 
			paint.reset();
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		final float y = (int) event.getY();
		int idx = (int) (y / textHeight);
		if (idx >= mLetter.length) {
			idx = mLetter.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			isPressed = true;
//			mCanvas.drawColor(color, mode)
			Log.i("adsasdas", "List is null ? "+(list.getTag().getClass().toString())+"  "+String.valueOf( mLetter[idx]));
			/*if(list.getTag() instanceof HashMap)*/ {
				Log.i("adsasdas", "step1");
				HashMap<String , Integer> map = (HashMap<String, Integer>) list.getTag();
				if(map.containsKey(String.valueOf( mLetter[idx]))) {
					Log.i("adsasdas", "step2");
					int position = map.get(String.valueOf( mLetter[idx]));
					if (position == -1) {
						Log.i("adsasdas", "step3");
						return true;
					}
					Log.i("adsasdas", "step4");
					list.setSelection(position);
					Log.i("adsasdas", "Position-"+position);
				}
			}
//			Log.i("sdsdsdsdswdsd", list.findViewWithTag(String.valueOf( mLetter[idx])).getClass().getName());
//			mDialogText.setVisibility(View.VISIBLE);
//			mDialogText.setText(String.valueOf( mLetter[idx]));
//			if (sectionIndexter == null) {
//				sectionIndexter = (SectionIndexer) list.getAdapter();
//			}
//			int position = 10;//sectionIndexter.getPositionForSection(mLetter[idx]);
//			if (position == -1) {
//				return true;
//			}
//			list.setSelection(position);
		} else {
//			mDialogText.setVisibility(View.INVISIBLE);
			isPressed = false;
		}
		invalidate();
		return true;
	}

	private ListView list;
//	private SectionIndexer sectionIndexter;
//	private TextView mDialogText;

	public void setListView(ListView _list) {
		list = _list;
//		sectionIndexter = (SectionIndexer) _list.getAdapter();
	}

//	public void setTextView(TextView mDialogText) {
//		this.mDialogText = mDialogText;
//	}

}
