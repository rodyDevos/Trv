
package com.igames2go.t4f.utils;

import com.igames2go.t4f.R;
import com.igames2go.t4f.view.CustomProfileView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler = new Handler();// handler to display images in UI thread
    Context mContext;
    public ImageLoader(Context context) {
    	mContext = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
//        readMap();
    }

    final int stub_id = R.drawable.trans;

    protected class StreamDrawable extends Drawable {
		private static final boolean USE_VIGNETTE = false;

		private final float mCornerRadius;
		private final RectF mRect = new RectF();
		private final BitmapShader mBitmapShader;
		private final Paint mPaint; private Paint paintBorder;
		private final int mMargin;
		private Bitmap bmp;
		StreamDrawable(Bitmap bitmap, float cornerRadius, int margin) {
//			int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
//                    (float) mContext.getResources().getDimension(R.dimen.profile_height), mContext.getResources().getDisplayMetrics());
			int value = (int) mContext.getResources().getDimension(R.dimen.profile_height);
			mCornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                    (float) 10, mContext.getResources().getDisplayMetrics());
			bmp= Bitmap.createScaledBitmap(bitmap, value, value, false);
			mBitmapShader = new BitmapShader(bmp,
					BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
			
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setShader(mBitmapShader);
			
			mMargin = 0;
		}
		private int borderWidth = 4;
	    private void setup()
	    {
	        // init paint

	        paintBorder = new Paint();
	        paintBorder.setAntiAlias(true);
	        
	        paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
	    }

	    @Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			//mRect.set(mMargin, mMargin, bounds.width() - mMargin, bounds.height() - mMargin);

			if (USE_VIGNETTE) {
				RadialGradient vignette = new RadialGradient(
						mRect.centerX(), mRect.centerY() * 1.0f / 0.7f, mRect.centerX() * 1.3f,
						new int[] { 0, 0, 0x7f000000 }, new float[] { 0.0f, 0.7f, 1.0f },
						BitmapShader.TileMode.CLAMP);
	
				Matrix oval = new Matrix();
				oval.setScale(1.0f, 0.7f);
				vignette.setLocalMatrix(oval);
	
				mPaint.setShader(
						new ComposeShader(mBitmapShader, vignette, PorterDuff.Mode.SRC_OVER));
			}
		}

		@Override
		public void draw(Canvas canvas) {
			mRect.set(0,0,bmp.getWidth() , bmp.getHeight());
			mPaint.setColor(Color.BLACK); 
			canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, mPaint);
			Paint paint = new Paint();
			RectF rect =  new RectF(0,0,bmp.getWidth() , bmp.getHeight());
			paint.setStrokeWidth(2);
			paint.setColor(Color.GRAY); 
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRoundRect(rect, mCornerRadius*1.25f, mCornerRadius*1.25f, paint);
		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			mPaint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			mPaint.setColorFilter(cf);
		}		
	}

    public void displayAdImage(ImageView iv, String url){
        new DownloadImageTask(iv).execute(url);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = mContext.getString(R.string.url_images) + urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void displayPlayerImage(String playerImageName, ImageView imageView, boolean isBackground)
    {
    	String url="";
    	if(playerImageName.length() > 0){
	    	if(playerImageName.contains(".png") || playerImageName.contains(".jpg"))
	    		url = mContext.getString(R.string.url_images)+playerImageName;
	    	else
	    		url = "https://graph.facebook.com/" + playerImageName + "/picture?type=large";
    	}else{
    		url = mContext.getString(R.string.url_images)+"fake_you.png";
    	}
    	url = url.replaceAll(" ", "%20");
    	DisplayImage(url, imageView, isBackground);
    }
    public void DisplayImage(String url, ImageView imageView, boolean isBackground)
    {
    	imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        
        if (bitmap != null)
        {
        	if(imageView instanceof CustomProfileView)
        	{
        		if(isBackground)
        			imageView.setBackgroundDrawable(new StreamDrawable(bitmap, 1, 1));
        		else
        			imageView.setImageDrawable(new StreamDrawable(bitmap, 1, 1));
        	}
        	else {
        		if(isBackground)
        			imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        		else
        			imageView.setImageBitmap(bitmap);
        	}
        }
        else
        {
            queuePhoto(url, imageView, isBackground);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView, boolean isBackground)
    {
        PhotoToLoad p = new PhotoToLoad(url, imageView, isBackground);
        executorService.submit(new PhotosLoader(p));
    }
    static HashMap<String, Long> map = null;
    
    public static synchronized void writeMap(Context context) {
    	FileOutputStream fStream;
		try {
			fStream = context.openFileOutput("saved-map", Context.MODE_PRIVATE);
			ObjectOutputStream oStream = new ObjectOutputStream(fStream);

		    oStream.writeObject(map);        
		    oStream.flush();
		    oStream.close();

		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
    }
    public static synchronized void readMap(Context context) {
        FileInputStream fis;
		try {
			fis = context.openFileInput("saved-map");
	        ObjectInputStream ois = new ObjectInputStream(fis);

	        map = (HashMap<String, Long>) ois.readObject();

	        ois.close();
		} catch (Exception e) {
			
			e.printStackTrace();
			
			map = new HashMap<String, Long>();
//			writeMap();
			
		} 
    }
    private Bitmap getBitmap(String url)
    {
        File f = fileCache.getFile(url);
        // from SD cache
        Bitmap b = decodeFile(f);
        if (b != null) {
        	try {
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
	            conn.setConnectTimeout(5000);
	            conn.setReadTimeout(5000);
	            Log.i("Headers", "***********************start*************************");
	            for(String str : conn.getHeaderFields().keySet()) {
	            	Log.i(str, conn.getHeaderField(str));
	            }
	            Log.i("Headers", "***********************End***************************");
	            long lastModifiedHeader = conn.getHeaderFieldDate("Last-Modified", System.currentTimeMillis());
//	            readMap();
				if(map != null){
					if(map.containsKey(f.getName())) {
						long lastModifiedFile = map.get(f.getName());
						if(lastModifiedHeader == lastModifiedFile) {
							Log.i("ImageLoader", "Caching in action- using local copy");
							return b;
						}
					}
					else
						Log.i("ImageLoader", "Map key doesnot exist");
				
				}
				else
					Log.i("ImageLoader", "map is null");
				
			} catch (Exception e) {
				e.printStackTrace();
				return b;
			} 
        }
        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            Log.i("Headers", "***********************start*************************");
            for(String str : conn.getHeaderFields().keySet()) {
            	Log.i(str, conn.getHeaderField(str));
            }
            Log.i("Headers", "***********************End***************************");
            long lastModifiedHeader = conn.getHeaderFieldDate("Last-Modified", System.currentTimeMillis());
			if(map == null)
			{
//				readMap();
			}
			if(map == null)
				map = new HashMap<String, Long>();
			
			map.put(f.getName(), lastModifiedHeader);
//			writeMap();
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public boolean isBackground;
        public PhotoToLoad(String u, ImageView i, boolean isBackground) {
            url = u;
            imageView = i;
            this.isBackground = isBackground;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if (imageViewReused(photoToLoad))
                    return;
                Bitmap bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if (imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run()
        {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
            	if(photoToLoad.imageView instanceof CustomProfileView) {
            		if(photoToLoad.isBackground)
            			photoToLoad.imageView.setBackgroundDrawable(new StreamDrawable(bitmap, 1, 1));
            		else
            			photoToLoad.imageView.setImageDrawable(new StreamDrawable(bitmap, 1, 1));
            	}
            	else {
            		if(photoToLoad.isBackground)
            			photoToLoad.imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            		else
            			photoToLoad.imageView.setImageBitmap(bitmap);
            	}
            }
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
