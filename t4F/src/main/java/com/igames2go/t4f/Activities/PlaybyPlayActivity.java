
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.R;
import com.igames2go.t4f.data.EndOfTurn;
import com.igames2go.t4f.data.Messages;
import com.igames2go.t4f.data.MessagesDataObject;
import com.igames2go.t4f.tasks.DoEndOfTurn;
import com.igames2go.t4f.tasks.LoadMessages;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.view.CustomButton;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlaybyPlayActivity extends AdActivity {

    private final String TAG = "T4F";

    private static final int CHAT_ACTIVITY = 1;

    private static boolean isResigned = false;

    private String mGameId = null;
    private String mGamePlaId = null;
    private String mQuesId = null;

    private ListView lv = null;

    ChatListAdapter adapter = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_playbyplay);
        Intent i = getIntent();
        mGameId = i.getStringExtra("game_id");
        mGamePlaId = i.getStringExtra("game_pla_id");
        mQuesId = i.getStringExtra("quesId");

        isResigned = false;
        if ((i.getStringExtra("resigned")).equals("1")) {
            findViewById(R.id.btn_resign).setVisibility(View.GONE);
            isResigned = true;
        }

        lv = (ListView) findViewById(R.id.lv_chat);

        findViewById(R.id.play_by_play).setVisibility(View.GONE);
        
        CustomButton backButton = (CustomButton)findViewById(R.id.go_back);
        backButton.setBackgroundResource(R.drawable.icon_back_transparent);
        //backButton.setBackground(getResources().getDrawable(R.drawable.icon_back_transparent));
        
        loadChatMessages();
    }

    private void loadChatMessages() {
        LoadMessages asynTask = new LoadMessages(this, mGameId, mGamePlaId,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete(Object obj) {
                        handleData((Messages) obj);
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

    private void handleData(Messages messagesObj) {
        if (messagesObj != null) {
            ArrayList<MessagesDataObject> item = (ArrayList<MessagesDataObject>) messagesObj
                    .getGameMessages();
            if (!item.isEmpty()) {
                adapter = new ChatListAdapter(this, R.layout.layout_chat_a_s,
                        item);
                lv.setAdapter(adapter);
                scrollMyListViewToBottom();
                
            } else {
                Log.e(TAG, "MessagesDataObject search is empty");
            }
        } else {
            Log.e(TAG, "Messages Object is null");
        }
    }

    private void scrollMyListViewToBottom() {
        lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv.setSelection(lv.getCount() - 1);
            }
        });
    }
    
    Dialog d;

    public void resign(View v) {
        if (mGamePlaId != null && !(mGamePlaId.length() == 0)) {
            d = DialogUtil.createDialog_title_divider_message_two_btn(this,
                    null, false, getString(R.string.resign_dialog_msg),
                    R.drawable.button_cancel, R.drawable.button_resign, new ButtonClickListener() {

                        @Override
                        public void onButtonClick(int s)
                                throws NullPointerException {
                            if (s == R.drawable.button_resign)
                                resignFromGame(mGamePlaId);
                            d.dismiss();
                        }
                    }, -1);
            d.show();

        } else
            Toast.makeText(this,
                    "Game Player Id is null...so cannot Resign from the game",
                    Toast.LENGTH_LONG).show();
    }

    public void createMessage(View v) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("gameId", mGameId + "");
        intent.putExtra("gamePlaId", mGamePlaId + "");
        intent.putExtra("quesId", mQuesId + "");
        intent.putExtra("msgType", "C");
        startActivityForResult(intent, CHAT_ACTIVITY);

    }

    public void showScoreboard(View v) {
        Intent i = getIntent();
        if (isResigned)
            i.putExtra("openScoreBoard", "1");
        else
            i.putExtra("openScoreBoard", "2");

        setResult(RESULT_OK, i);
        PlaybyPlayActivity.this.finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case CHAT_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    loadChatMessages();
                }
                break;
        }
    }

    public void showDialogonResignSuccess(String successmsg) {
        d = DialogUtil.createDialog_title_divider_message_two_btn(this, null,
                false, successmsg, -1, R.drawable.button_ok, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        d.dismiss();
                    }
                }, -1);
        d.show();
    }

    private void resignFromGame(String gameplaId) {
        DoEndOfTurn asynTask;
        asynTask = new DoEndOfTurn(this, gameplaId, "0", "0", "0", "0", "0",
                "0", new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @Override
                    public void onLoadingComplete(Object obj) {
                        if (obj != null) {
                            EndOfTurn endofturnObj = (EndOfTurn) obj;
                            String successmsg = endofturnObj.getEndOfTurnData()
                                    .getMessage1()
                                    + "\n"
                                    + endofturnObj.getEndOfTurnData()
                                            .getMessage2() + "\n";
                            showDialogonResignSuccess(successmsg);
                            findViewById(R.id.btn_resign).setVisibility(
                                    View.GONE);
                            isResigned = true;
                        } else
                            Toast.makeText(PlaybyPlayActivity.this,
                                    "Resign is Unsuccessfull",
                                    Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asynTask.execute();
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        if (isResigned) {
            i.putExtra("hasResigned", "1");
            setResult(RESULT_OK, i);
        } else {
            i.putExtra("hasResigned", "0");
            setResult(RESULT_CANCELED, i);
        }

        PlaybyPlayActivity.this.finish();
    }

    public void goHome(View v) {
        //startActivity(new Intent(getApplicationContext(), HomeScreen.class));
        PlaybyPlayActivity.this.finish();
    }
}
