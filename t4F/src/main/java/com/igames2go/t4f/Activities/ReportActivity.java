
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SendMsgResult;
import com.igames2go.t4f.tasks.SendTheMessage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ReportActivity extends Activity {

    private EditText etReport;

    private String mGamePlaId = null;
    private String mQuesId = null;

    public Menu menuG;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.report_question);
        Intent i = getIntent();
        mGamePlaId = i.getStringExtra("game_pla_id");
        mQuesId = i.getStringExtra("quesId");
        etReport = (EditText) findViewById(R.id.tv_report);
        String question = getIntent().getStringExtra("report") + "";
        Log.d("Questions", question);
        
        etReport.append(question);
        etReport.append(" ");
        etReport.append("\n");
        etReport.append("Add your comments below...");
        etReport.append("\n");
        etReport.setBackgroundColor(Color.WHITE);
        
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
        Log.e("Check", "ReportQues:  " + msgText);
        sendChatMessage(msgText);
    }

    private void sendChatMessage(String msgText) {
        SendTheMessage asynTask = new SendTheMessage(this, mGamePlaId, "RQ",
                msgText, mQuesId, new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        String msgResult = ((SendMsgResult) obj)
                                .getSendMsgResult();
                        if (msgResult.equals("1")) {
//                            Toast.makeText(ReportActivity.this,
//                                    "Question is Reported", Toast.LENGTH_LONG)
//                                    .show();
                            ReportActivity.this.finish();
                        } else {
                            Toast.makeText(ReportActivity.this,
                                    "Failed to report Question",
                                    Toast.LENGTH_LONG).show();
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
    public void goBack(View v){
    	finish();
    }
}
