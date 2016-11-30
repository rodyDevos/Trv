
package com.igames2go.t4f.utils;

import com.igames2go.t4f.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ShowDialog {

    private static final String TAG = ShowDialog.class.getSimpleName();
    private static Dialog mDialog = null;

    public static void showLoadingDialog(Activity context, String message) {

        try {
        	removeLoadingDialog();
        	
            mDialog = new Dialog(context);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(getContentView(context, message));
            mDialog.setTitle(null);
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "--- Tried showing a dialog: Dialog spat out errors");
            Log.d(TAG, "--- Tolerating exception");
            Log.e(TAG, e.getMessage(), e);
        }

    }

    public static void removeLoadingDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            Log.d(TAG, "--- Tried removing a dialog: tolerating exception");
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static LinearLayout getContentView(Context context, String message) {

        LinearLayout layout = new LinearLayout(context);

        LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.CENTER;

        params.setMargins(10, 10, 10, 10);

        layout.setLayoutParams(params);

        layout.setOrientation(LinearLayout.VERTICAL);

        ProgressBar bar = new ProgressBar(context, null,
                android.R.attr.progressBarStyle);
        layout.addView(bar, params);

        TextView textView = new TextView(context);
        textView.setText(message);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5, 5, 5, 5);

        LayoutParams textParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
        textParams.setMargins(2, 2, 2, 2);
        layout.addView(textView, textParams);

        return layout;
    }

    public static void showAlertDialog(final Context context, final String msg,
            DialogInterface.OnClickListener listener) {
        AlertDialog mAlertDialog = null;
        try {
            if (context instanceof Activity) {
                TextView tv = new TextView(context);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(msg);
                tv.setTextSize(17);
                tv.setTextColor(Color.WHITE);
                mAlertDialog = new AlertDialog.Builder(context)
                        .setCancelable(false).setView(tv)
                        .setPositiveButton("OK", listener)
                        .setNegativeButton("Cancel", null).create();
                mAlertDialog.show();
            } else {
                Log.d(TAG, "--- Caller did not pass activity instance");
                Log.d(TAG, "--- Will not display alert dialog");
            }
        } catch (Exception e) {
            Log.d(TAG,
                    "--- Tried showing a dialog: AlertDialog spat out errors");
            // Log.d(TAG,"--- Tolerating the exception");
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void showAlertDialog(final Context context, final String msg) {
        AlertDialog mAlertDialog = null;
        try {
            if (context instanceof Activity) {
                TextView tv = new TextView(context);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(msg);
                tv.setTextSize(17);
                tv.setTextColor(Color.WHITE);
                mAlertDialog = new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setView(tv)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int whichButton) {

                                    }
                                }).create();
                mAlertDialog.setContentView(R.layout.alert_layout);
                // ((TextView)mAlertDialog.getWindow().findViewById(android.R.id.title)).setBackgroundColor(context.getResources().getColor(R.color.color1));
                mAlertDialog.show();
            } else {
                Log.d(TAG, "--- Caller did not pass activity instance");
                Log.d(TAG, "--- Will not display alert dialog");
            }
        } catch (Exception e) {
            Log.d(TAG,
                    "--- Tried showing a dialog: AlertDialog spat out errors");
            // Log.d(TAG,"--- Tolerating the exception");
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static Dialog createDialog(Context context) {
        mDialog = new Dialog(context);

        return mDialog;
    }

    public static void showAlertDialog(final Context context,
            final Spanned msg, View.OnClickListener listener,
            String buttonText, final String title) {

        try {
            // ScrollView s=new ScrollView(context);
            // s.setBackgroundColor(Color.WHITE);
            // TextView tv=new TextView(context);
            // // tv.setGravity(Gravity.CENTER_HORIZONTAL);
            //
            // tv.setPadding(15, 5,15,5);
            // tv.setText(msg);
            // tv.setAutoLinkMask(Linkify.WEB_URLS);
            // tv.setTextSize(17);
            //
            // tv.setTextColor(Color.BLACK);
            // s.addView(tv);

            if (mDialog == null)
                mDialog = new Dialog(context);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.alert_layout);
            // mDialog.setTitle(title);
            ((TextView) mDialog.findViewById(R.id.message)).setText(msg);
            ((TextView) mDialog.findViewById(R.id.title)).setText(Html
                    .fromHtml(title));
            Button btn1 = (Button) mDialog.findViewById(R.id.button1);
            btn1.setText(buttonText);
            btn1.setVisibility(View.VISIBLE);
            btn1.setOnClickListener(listener);

            // mDialog.setCancelable(false);

            mDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "--- Tried showing a dialog: Dialog spat out errors");
            Log.d(TAG, "--- Tolerating exception");
            Log.e(TAG, e.getMessage(), e);
        }

        /*
         * AlertDialog mAlertDialog = null; try { if (context instanceof
         * Activity) { ScrollView s=new ScrollView(context);
         * s.setBackgroundColor(Color.WHITE); TextView tv=new TextView(context);
         * // tv.setGravity(Gravity.CENTER_HORIZONTAL); tv.setPadding(5, 5,5,5);
         * tv.setText(Html.fromHtml(msg)); tv.setAutoLinkMask(Linkify.WEB_URLS);
         * tv.setTextSize(17); tv.setTextColor(Color.BLACK); s.addView(tv);
         * mAlertDialog = new AlertDialog.Builder(context).create();
         * mAlertDialog.setContentView(R.layout.alert_layout);
         * mAlertDialog.setCancelable( false); mAlertDialog.setView(s);
         * mAlertDialog.setButton("OK", new DialogInterface.OnClickListener() {
         * public void onClick(DialogInterface dialog, int whichButton) { } });
         * mAlertDialog.setTitle(Html.fromHtml(title)); mAlertDialog.show();
         * }else{ Log.d(TAG,"--- Caller did not pass activity instance");
         * Log.d(TAG,"--- Will not display alert dialog"); } } catch (Exception
         * e) {
         * Log.d(TAG,"--- Tried showing a dialog: AlertDialog spat out errors");
         * // Log.d(TAG,"--- Tolerating the exception");
         * Log.e(TAG,e.getMessage(),e); }
         */
    }

    public static void showAlertDialogLink(final Context context,
            final Spanned msg, View.OnClickListener listener,
            String buttonText, final String title, final String link) {
        try {
            // ScrollView s=new ScrollView(context);
            // s.setBackgroundColor(Color.WHITE);
            // TextView tv=new TextView(context);
            // // tv.setGravity(Gravity.CENTER_HORIZONTAL);
            //
            // tv.setPadding(15, 5,15,5);
            // tv.setText(msg);
            // tv.setAutoLinkMask(Linkify.WEB_URLS);
            // tv.setTextSize(17);
            //
            // tv.setTextColor(Color.BLACK);
            // s.addView(tv);

            if (mDialog == null)
                mDialog = new Dialog(context);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.alert_layout);
            // mDialog.setTitle(title);
            ((TextView) mDialog.findViewById(R.id.message)).setText(msg);
            ((TextView) mDialog.findViewById(R.id.title)).setText(Html
                    .fromHtml(title));
            Button btn1 = (Button) mDialog.findViewById(R.id.button1);
            btn1.setText(buttonText);
            btn1.setVisibility(View.VISIBLE);
            btn1.setOnClickListener(listener);
            Button btn2 = (Button) mDialog.findViewById(R.id.button2);
            btn2.setVisibility(View.VISIBLE);
            btn2.setText("More");
            // btn2.setVisibility(View.VISIBLE);
            btn2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(link));
                    context.startActivity(browserIntent);
                }
            });
            // mDialog.setCancelable(false);

            mDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "--- Tried showing a dialog: Dialog spat out errors");
            Log.d(TAG, "--- Tolerating exception");
            Log.e(TAG, e.getMessage(), e);
        }

        /*
         * AlertDialog mAlertDialog = null; try { if (context instanceof
         * Activity) { ScrollView s=new ScrollView(context);
         * s.setBackgroundColor(Color.WHITE); TextView tv=new TextView(context);
         * // tv.setGravity(Gravity.CENTER_HORIZONTAL); tv.setPadding(5, 5,5,5);
         * tv.setText(Html.fromHtml(msg)); tv.setAutoLinkMask(Linkify.WEB_URLS);
         * tv.setTextSize(17); tv.setTextColor(Color.BLACK); s.addView(tv);
         * mAlertDialog = new AlertDialog.Builder(context).create();
         * mAlertDialog.setContentView(R.layout.alert_layout);
         * mAlertDialog.setCancelable( false); mAlertDialog.setView(s);
         * mAlertDialog.setButton("OK", new DialogInterface.OnClickListener() {
         * public void onClick(DialogInterface dialog, int whichButton) { } });
         * mAlertDialog.setTitle(Html.fromHtml(title)); mAlertDialog.show();
         * }else{ Log.d(TAG,"--- Caller did not pass activity instance");
         * Log.d(TAG,"--- Will not display alert dialog"); } } catch (Exception
         * e) {
         * Log.d(TAG,"--- Tried showing a dialog: AlertDialog spat out errors");
         * // Log.d(TAG,"--- Tolerating the exception");
         * Log.e(TAG,e.getMessage(),e); }
         */
    }

}
