
package com.igames2go.t4f.Activities;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.EndOfTurn;
import com.igames2go.t4f.data.QuesWithAns;
import com.igames2go.t4f.data.QuesWithAnsDataObject;
import com.igames2go.t4f.data.SurpriseCategory;
import com.igames2go.t4f.tasks.DoEndOfTurn;
import com.igames2go.t4f.tasks.LoadLifeline;
import com.igames2go.t4f.tasks.LoadQuestion;
import com.igames2go.t4f.tasks.LoadSurpriseCategory;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {

    private static final String ANSWERRIGHT = "answerright";

    private final String TAG = "T4F";
    private TextView tv_question;
    private TextView tv_category;
    private Button btn_optA;
    private Button btn_optB;
    private Button btn_optC;
    private Button btn_optD;
    private String mGameID;
    //private String mGameStatus;
    private String mCategoryID;
    private String mCategoryName;
    private String mGameplaId;
    private String mSurprise;
    private ProgressBar pg;
    //private Boolean isSolo;
    
    private QuesWithAns quesansObj = null;
    private int total_time = 120;
    //private int allowed_time = 30;
    //private final int TIME_INTERVAL_UNIT = 30;
    // lp 08/02/2014
    private int allowed_time = 20;
    private final int TIME_INTERVAL_UNIT = 20;

    // private int maxTime = ALLOWED_TIME;
    int timeRemaning = allowed_time;
    private SoundPool mShortPlayer;
    private int soundRightID;
    private int soundWrongID;
    private int soundOutOfTimeID;
    private boolean question_fetched = false;
    private boolean lifeline_fetched = false;
    // private int totalTime=30;
    private OnLoadCompleteListener mListener = new OnLoadCompleteListener() {

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
           Log.d("SoundPool", status+"");
           
        }
    };

    private int lifeline = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_questionscreen);

        tv_question = (TextView) findViewById(R.id.tv_question);
        tv_category = (TextView) findViewById(R.id.tv_quescatgory);
        btn_optA = (Button) findViewById(R.id.btn_optionA);
        btn_optB = (Button) findViewById(R.id.btn_optionB);
        btn_optC = (Button) findViewById(R.id.btn_optionC);
        btn_optD = (Button) findViewById(R.id.btn_optionD);
        pg = (ProgressBar) findViewById(R.id.progress_timer);
        mGameID = getIntent().getStringExtra("game_id");
        //mGameStatus = getIntent().getStringExtra("game_status");
        mCategoryID = getIntent().getStringExtra("category_id");
        mCategoryName = getIntent().getStringExtra("category_name") + "";
        mGameplaId = getIntent().getStringExtra("game_pla_id");
        mSurprise = getIntent().getStringExtra("isSurprise");
        lifeline = getIntent().getIntExtra("lifeline", 3);
        mShortPlayer = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundRightID = mShortPlayer.load(this, R.raw.answer_right, 1);
        soundWrongID = mShortPlayer.load(this, R.raw.answer_wrong, 1);
        soundOutOfTimeID = mShortPlayer.load(this, R.raw.answer_outoftime, 1);
        mShortPlayer.setOnLoadCompleteListener(mListener);
        setMaxTimeForGame();
        if (mSurprise.equals("0")) {
            loadQuestion(mCategoryID);
        } else if (mSurprise.equals("1")) {
            loadQuestionWithSurpriseCategory();
        } else {
            Toast.makeText(getApplicationContext(), "Some Thing is Wrong....",
                    1000).show();
        }

        /*
        if(mGameStatus.equalsIgnoreCase("1SOLO"))
        	isSolo = true;
        else
        	isSolo = false;
        */
        
        timer = new NewCountDownTimer(total_time * 1000, 1000);
        pg.setMax(allowed_time);
        ((Button) findViewById(R.id.btn_lifeline)).setText("" + (total_time - allowed_time)
                / TIME_INTERVAL_UNIT);
        ((TextView) findViewById(R.id.tv_timerbox)).setText(timeRemaning
                + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int originallifeline = getIntent().getIntExtra("lifeline", 3);
        if (originallifeline > lifeline) {
            setLifeline(lifeline - originallifeline);
        }
    }

    private void setLifeline(int increment) {
        LoadLifeline loadLifeline = new LoadLifeline(mGameplaId, increment + "",
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {
                        
                    }

                    @Override
                    public void onLoadingComplete(Object obj) {
                       
                    }

                    @Override
                    public void onError(Object error) {
                        
                    }
                }, getApplicationContext());
        loadLifeline.execute();

    }

    public void useLifeLine(View v) {
        if (allowed_time >= total_time) {
            allowed_time = total_time;
            lifeline = 0;
            return;

        }
        if (lifeline > 0)
            lifeline--;
        allowed_time += TIME_INTERVAL_UNIT;
        ((Button) findViewById(R.id.btn_lifeline)).setText(lifeline + "");
    }

    private NewCountDownTimer timer;

    private void setMaxTimeForGame() {
        total_time = TIME_INTERVAL_UNIT * (lifeline + 1);
    }

    private boolean timedout = false;

    private class NewCountDownTimer extends CountDownTimer {

        public NewCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            
        }

        @Override
        public void onFinish() {
            if (timedout == false) {
                pg.setProgress(pg.getMax());
                if(T4FApplication.soundSettting)
                	mShortPlayer.play(soundOutOfTimeID, 0.99f, 0.99f, 0, 0, 1);
                onTimeOut();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("onTick", "" + millisUntilFinished + " " + (total_time - allowed_time) * 1000);
            if (timedout) {
                return;
            }
            if (millisUntilFinished <= (total_time - allowed_time) * 1000) {
                this.cancel();
                timer.cancel();
                timedout = true;
                pg.setProgress(pg.getMax());
                if(T4FApplication.soundSettting)
                	mShortPlayer.play(soundOutOfTimeID, 0.99f, 0.99f, 0, 0, 1);
                onTimeOut();
                timer = null;
                return;
            }
            pg.setMax(allowed_time);
            pg.setProgress(pg.getProgress() + 1);
            timeRemaning = (int) ((millisUntilFinished - (total_time - allowed_time) * 1000) / 1000);
            ((TextView) findViewById(R.id.tv_timerbox)).setText(timeRemaning
                    + "");

        }

    }

    private void loadQuestion(String categoryID) {
        LoadQuestion asynTask = new LoadQuestion(this, mGameID, categoryID,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @Override
                    public void onLoadingComplete(Object obj) {

                        quesansObj = (QuesWithAns) obj;
                        handleData(quesansObj);
                        timer.start();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asynTask.execute();
    }

    private void loadQuestionWithSurpriseCategory() {
        LoadSurpriseCategory asynTask = new LoadSurpriseCategory(mGameID,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @Override
                    public void onLoadingComplete(Object obj) {
                        SurpriseCategory surpriseCatObj = (SurpriseCategory) obj;
                        if (surpriseCatObj != null)
                        { loadQuestion(surpriseCatObj
                                    .getSurpriseCategoryData().getCatid());
                        	mCategoryName = surpriseCatObj.getSurpriseCategoryData().getCatnameshort();
                        	mCategoryID = surpriseCatObj.getSurpriseCategoryData().getCatid();
                        }
                        else
                            Log.e(TAG, "SurpriseCategory object is null");
                    }

                    @Override
                    public void onError(Object error) {

                    }
                }, getApplicationContext());

        asynTask.execute();
    }

    int correctid = -1;
    
    private void handleData(QuesWithAns obj) {
        if (obj != null) {
            if ((obj.getQuestion() != null)
                    && !((obj.getQuestion().getQueid()).equals("0"))) {
                tv_question.setText(obj.getQuestion().getText());
                tv_category.setText(mCategoryName);
                btn_optA.setText(obj.getAnswer1().getText());
                btn_optB.setText(obj.getAnswer2().getText());
                btn_optC.setText(obj.getAnswer3().getText());
                btn_optD.setText(obj.getAnswer4().getText());
                btn_optA.setGravity(Gravity.CENTER);
                if (obj.getAnswer1().getType().equalsIgnoreCase(ANSWERRIGHT)) {
                    correctid = R.drawable.button_a;
                }
                if (obj.getAnswer2().getType().equalsIgnoreCase(ANSWERRIGHT)) {
                    correctid = R.drawable.button_b;
                }
                if (obj.getAnswer3().getType().equalsIgnoreCase(ANSWERRIGHT)) {
                    correctid = R.drawable.button_c;
                    ;
                }
                if (obj.getAnswer4().getType().equalsIgnoreCase(ANSWERRIGHT)) {
                    correctid = R.drawable.button_d;
                    ;
                }

                btn_optA.setTag(R.id.ad, obj.getAnswer1());
                btn_optB.setTag(R.id.ad, obj.getAnswer2());
                btn_optC.setTag(R.id.ad, obj.getAnswer3());
                btn_optD.setTag(R.id.ad, obj.getAnswer4());

                btn_optA.setTag(R.drawable.button_a);
                btn_optB.setTag(R.drawable.button_b);
                btn_optC.setTag(R.drawable.button_c);
                btn_optD.setTag(R.drawable.button_d);

            } else {
                Log.e(TAG, "either obj.getQuestion is null or question id is 0");
                Toast.makeText(this, "Error in loading question",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Log.e(TAG, "QuesWithAns obj is null");
            Toast.makeText(this, "Error in loading question", Toast.LENGTH_LONG)
                    .show();
            finish();
        }
    }

    QuesWithAnsDataObject answeredObj;
    QuesWithAnsDataObject correctObj;

    public void optionSelected(final View v) {
        // Toast.makeText(this, "optionAselected", Toast.LENGTH_SHORT).show();
        if (quesansObj != null) {
            answeredObj = (QuesWithAnsDataObject) v.getTag(R.id.ad);

            if (answeredObj != null) {
                if(timer != null)
                	timer.cancel();
                // Toast.makeText(this,answeredObj.getType() ,
                // Toast.LENGTH_SHORT).show();
                DoEndOfTurn asynTask;
                int answer = 1;
                if (answeredObj.getType().equals(ANSWERRIGHT)) {
                    correctObj = answeredObj;
                    if(T4FApplication.soundSettting)
                    	mShortPlayer.play(soundRightID, 0.99f, 0.99f, 1, 0, 1);

                } else {
                    answer = 0;
                    correctObj = getCorrectAnswerDataObject();
                    if(T4FApplication.soundSettting)
                    	mShortPlayer.play(soundWrongID, 0.99f, 0.99f, 1, 0, 1);
                }
                asynTask = new DoEndOfTurn(this, mGameplaId, mCategoryID,
                        mSurprise, answeredObj.getQueid(), answer + "", ""
                                + (allowed_time - timeRemaning), allowed_time + "", 
                        new LoadingListener() {

                            @Override
                            public void onLoadingComplete() {

                            }

                            @Override
                            public void onLoadingComplete(Object obj) {
                            	if(obj == null)
                            		return;
                                final EndOfTurn turn = (EndOfTurn) obj;
//                                Log.i(TAG, "" + turn.toString());
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(
                                                getApplicationContext(),
                                                Answer2QuestionActivity.class);
                                        intent.putExtra("question",
                                                tv_question.getText() + "");
                                        intent.putExtra("category",
                                                tv_category.getText() + "");
                                        intent.putExtra("timeout", false);
                                        intent.putExtra("selected_answer",
                                                answeredObj);
                                        intent.putExtra("selected_answer_id",
                                                (Integer) v.getTag());
                                        intent.putExtra("correct_answer",
                                                correctObj);
                                        intent.putExtra("correct_answer_id",
                                                correctid);
                                        intent.putExtra("EndOfTurn", turn);
                                        intent.putExtra("game_id", mGameID);
                                        intent.putExtra("report",
                                                getReportString());
                                        intent.putExtra("gamePlaId", mGameplaId);
                                        intent.putExtra("quesId",
                                                answeredObj.getQueid());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(Object error) {

                            }
                        });

                asynTask.execute();
            }
        }
    }

    public void onTimeOut() {
        // Toast.makeText(this, "optionAselected", Toast.LENGTH_SHORT).show();
        if (quesansObj != null) {
            // answeredObj = (QuesWithAnsDataObject) v.getTag();

            // if( answeredObj != null )
            {
                timer.cancel();
                // Toast.makeText(this,answeredObj.getType() ,
                // Toast.LENGTH_SHORT).show();
                DoEndOfTurn asynTask;
                asynTask = new DoEndOfTurn(this, mGameplaId, mCategoryID,
                        mSurprise, quesansObj.getQuestion().getQueid(), "0", ""
                                + (allowed_time), allowed_time + "", 
                        new LoadingListener() {

                            @Override
                            public void onLoadingComplete() {

                            }

                            @Override
                            public void onLoadingComplete(Object obj) {
                                final EndOfTurn turn = (EndOfTurn) obj;
                                Log.i(TAG, "" + turn.toString());
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(
                                                getApplicationContext(),
                                                Answer2QuestionActivity.class);
                                        intent.putExtra("question",
                                                tv_question.getText() + "");
                                        intent.putExtra("category",
                                                tv_category.getText() + "");
                                        intent.putExtra("timeout", true);
                                        
                                        intent.putExtra("EndOfTurn", turn);
                                        intent.putExtra("game_id", mGameID);
                                        intent.putExtra("report",
                                                getReportString());
                                        intent.putExtra("gamePlaId", mGameplaId);
                                        intent.putExtra("quesId", quesansObj
                                                .getQuestion().getQueid());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                            @Override
                            public void onError(Object error) {

                            }
                        });

                asynTask.execute();
            }
        }
    }

    private QuesWithAnsDataObject getCorrectAnswerDataObject() {
        if (quesansObj != null) {
            if (quesansObj.getAnswer1().getType().equals(ANSWERRIGHT))
                return quesansObj.getAnswer1();
            if (quesansObj.getAnswer2().getType().equals(ANSWERRIGHT))
                return quesansObj.getAnswer2();
            if (quesansObj.getAnswer3().getType().equals(ANSWERRIGHT))
                return quesansObj.getAnswer3();
            if (quesansObj.getAnswer4().getType().equals(ANSWERRIGHT))
                return quesansObj.getAnswer4();

        }
        return null;

    }

    private String getReportString() {
        String str = "";

        str = tv_question.getText() + "\n";
        str = str + "A-" + btn_optA.getText() + (correctid == R.drawable.button_a?" *":"")+"\n";
        str = str + "B-" + btn_optB.getText() + (correctid == R.drawable.button_b?" *":"")+"\n";
        str = str + "C-" + btn_optC.getText() + (correctid == R.drawable.button_c?" *":"")+"\n";
        str = str + "D-" + btn_optD.getText() + (correctid == R.drawable.button_d?" *":"")+"\n";

        return str;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
        	return true;
        }
	    return super.onKeyDown(keyCode, event);
    }
}
