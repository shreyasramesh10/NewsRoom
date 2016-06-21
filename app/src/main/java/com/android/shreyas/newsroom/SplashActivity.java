package com.android.shreyas.newsroom;

/**
 * Created by Karthik's on 27-02-2016.
 */
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.android.shreyas.newsroom.splash.KenBurnsView;
import com.android.shreyas.newsroom.splash.RobotoTextView;
import com.firebase.client.Firebase;


public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 9000;
    private KenBurnsView mKenBurns;
    Button login;
    Button skip;
    Firebase ref = new Firebase("https://popping-inferno-9534.firebaseio.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setAnimation();

        getWindow().getDecorView().setSystemUiVisibility(View.FOCUSABLES_TOUCH_MODE);

        //mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
        //mKenBurns.setImageResource(R.drawable.newsroom);
        login = (Button) findViewById(R.id.loginButton);
        skip = (Button) findViewById(R.id.skipButton);
        if(ref.getAuth()!=null){
            login.setVisibility(View.INVISIBLE);
            skip.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            },SPLASH_TIME_OUT);
        }
        else{
            login.setVisibility(View.VISIBLE);
            skip.setVisibility(View.VISIBLE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.putExtra("activity","Splash");
                        startActivity(i);
                        finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void setAnimation() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(findViewById(R.id.welcome_text), "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();

        ObjectAnimator scaleXAnimation1 = ObjectAnimator.ofFloat(findViewById(R.id.loginButton), "scaleX", 3.0F, 1.0F);
        scaleXAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation1.setDuration(1200);
        ObjectAnimator scaleYAnimation1 = ObjectAnimator.ofFloat(findViewById(R.id.loginButton), "scaleY", 3.0F, 1.0F);
        scaleYAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation1.setDuration(1200);
        ObjectAnimator alphaAnimation1 = ObjectAnimator.ofFloat(findViewById(R.id.loginButton), "alpha", 0.0F, 1.0F);
        alphaAnimation1.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation1.setDuration(1200);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.play(scaleXAnimation1).with(scaleYAnimation1).with(alphaAnimation1);
        animatorSet1.setStartDelay(500);
        animatorSet1.start();

        ObjectAnimator scaleXAnimation2 = ObjectAnimator.ofFloat(findViewById(R.id.skipButton), "scaleX", 3.0F, 1.0F);
        scaleXAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation2.setDuration(1200);
        ObjectAnimator scaleYAnimation2 = ObjectAnimator.ofFloat(findViewById(R.id.skipButton), "scaleY", 3.0F, 1.0F);
        scaleYAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation2.setDuration(1200);
        ObjectAnimator alphaAnimation2 = ObjectAnimator.ofFloat(findViewById(R.id.skipButton), "alpha", 0.0F, 1.0F);
        alphaAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation2.setDuration(1200);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.play(scaleXAnimation2).with(scaleYAnimation2).with(alphaAnimation2);
        animatorSet2.setStartDelay(500);
        animatorSet2.start();

        findViewById(R.id.imagelogo).setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        findViewById(R.id.imagelogo).startAnimation(anim);
    }
}