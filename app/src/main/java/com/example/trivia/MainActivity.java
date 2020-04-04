package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.animation.Animator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import com.example.trivia.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv, no,score,hsc;
    private Button tru, fal;
    private ImageButton pre, next;
    private CardView cv;
    private int curr = 0;
    private int s=0,hs=0;
    private Prefs prefs;
    ArrayList<Question> questionList;
    private static  final  String MESS="msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv2);
        no = findViewById(R.id.no);
        pre = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        tru = findViewById(R.id.tru);
        fal = findViewById(R.id.fal);
        cv = findViewById(R.id.cv);
        score=findViewById(R.id.scores);
        hsc=findViewById(R.id.highscores);
        prefs = new Prefs(MainActivity.this);

        tru.setOnClickListener(this);
        fal.setOnClickListener(this);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);

        hs=prefs.getHighScore();
        hsc.setText("High: "+hs);
        curr=prefs.getCurr();

        questionList = new QuestionBank().getQuestionArrayList(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                Log.d("Mains", "kjiuih" + questionArrayList);
                tv.setText(questionArrayList.get(curr).getAnswer());
                no.setText(curr+ 1 + " / " + questionArrayList.size());
                score.setText(MessageFormat.format("Score: {0}", s));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev:
                if(curr==0)
                    curr=1;
                curr = (curr - 1) % questionList.size();
                update();
                break;
            case R.id.next:
                curr = (curr + 1) % questionList.size();
                update();
                break;
            case R.id.tru:
                check(true);
                update();
                break;
            case R.id.fal:
                check(false);
                update();
                break;
        }
        return;

    }

    private void update() {
        tv.setText(questionList.get(curr).getAnswer());
        no.setText(curr + 1 + " / " + questionList.size());
    }

    private void check(boolean user) {
        boolean fin = questionList.get(curr).isAnswerTrue();
        int toastid = 0;
        if (user == fin) {
            toastid = R.string.correct;
            s=s+10;
            score.setText("Score: "+s);
            if(hs<s){
                hs=s;
            }
            //hsc.setText("HS: "+hs);
            fadev();
        } else {
            shake();
            if(s!=0)
            s=s-10;
            score.setText("Score: "+s);
            toastid = R.string.wrong;
        }
        Toast.makeText(MainActivity.this, toastid, Toast.LENGTH_SHORT).show();
    }

    private void fadev() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cv.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                cv.setBackgroundResource(R.color.green);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cv.setBackgroundResource(R.color.orange);
                curr = (curr + 1) % questionList.size();
                update();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shake() {
        Animation shakeit = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        cv.setAnimation(shakeit);

        shakeit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                cv.setBackgroundColor(getResources().getColor(R.color.red));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                cv.setBackgroundColor(getResources().getColor(R.color.orange));
                curr = (curr + 1) % questionList.size();
                update();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return;
    }
    @Override
    public  void onPause()
    {
        prefs.saveCurr(curr);
        prefs.saveHighScore(hs);
        super.onPause();
    }
}
