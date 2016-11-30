
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SendMsgResult;
import com.igames2go.t4f.tasks.SendTheMessage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ChatActivity extends Activity {

    private final String TAG = "T4F";
    private EditText etReport;
    private TextView tv;

    private String mGameId = null;
    private String mGamePlaId = null;
    private String mQuesId = null;
    private String mMsgType = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.chat);
        Intent i = getIntent();
        mGameId = i.getStringExtra("gameId");
        mGamePlaId = i.getStringExtra("gamePlaId");
        mQuesId = i.getStringExtra("quesId");
        mMsgType = i.getStringExtra("msgType");

        etReport = (EditText) findViewById(R.id.tv_report);
        etReport.setBackgroundColor(Color.WHITE);
        tv = (TextView) findViewById(R.id.title);

        if (mMsgType.equals("C"))
            tv.setText("190 Remaining");

        etReport.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                String str = etReport.getText() + "";
                tv.setText((190 - str.length()) + " Remaining");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void send(View v) {
        String msgText = etReport.getText().toString().replaceAll("\n", "");
        try {
            URLEncoder.encode(msgText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sendChatMessage(msgText);
    }

    private void sendChatMessage(String msgText) {
        SendTheMessage asynTask = new SendTheMessage(this, mGamePlaId,
                mMsgType, msgText, mQuesId, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        String msgResult = ((SendMsgResult) obj)
                                .getSendMsgResult();
                        if (msgResult.equals("1")) {
                            Toast.makeText(ChatActivity.this, "Message Sent",
                                    Toast.LENGTH_LONG).show();
                            Intent i = getIntent();
                            setResult(RESULT_OK, i);
                            ChatActivity.this.finish();
                        } else {
                            Toast.makeText(ChatActivity.this,
                                    "Message Sent Failed", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(Object error) {

                    }

                    @Override
                    public void onLoadingComplete() {

                    }

                });
        asynTask.execute();

    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        setResult(RESULT_CANCELED, i);
        ChatActivity.this.finish();
    }
    public void cancel(View v){
		finish();
	}
}
