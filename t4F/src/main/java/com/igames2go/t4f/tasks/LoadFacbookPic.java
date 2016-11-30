
package com.igames2go.t4f.tasks;

import com.igames2go.t4f.data.FriendsDataObject;
import com.igames2go.t4f.utils.HttpManager;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class LoadFacbookPic extends AsyncTask<Void, Void, Void> {

    private FriendsDataObject pla_data_obj = null;
    private ImageView imageview;
    private Context context;
    private ImageButton option;

    public LoadFacbookPic(Context context, FriendsDataObject pla_data_obj,
            ImageView imageview, ImageButton opt) {
        this.pla_data_obj = pla_data_obj;
        this.imageview = imageview;
        this.context = context;
        option = opt;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

    @Override
    protected Void doInBackground(Void... params) {
        String pla_fb_id = pla_data_obj.getPla_fb_id();

        if (imageview != null && !TextUtils.isEmpty(pla_fb_id)) {

            try {
                final InputStream is = (InputStream) HttpManager.getResponse(
                        "https://graph.facebook.com/" + pla_fb_id + "/picture",
                        true);

                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        imageview.setImageBitmap(bmp);
                        //pla_data_obj.setBitmap(bmp);
                        option.setTag(pla_data_obj);
                    }
                });

            } catch (NullPointerException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        return null;
    }

}
