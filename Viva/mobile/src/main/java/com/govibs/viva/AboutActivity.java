package com.govibs.viva;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView tvSplashScreenLetterV, tvSplashScreenDescVery,
            tvSplashScreenLetterI, tvSplashScreenDescIntelligent, tvSplashScreenLetterViv,
            tvSplashScreenDescVirtual, tvSplashScreenLetterViva, tvSplashScreenDescAssistant;
    RotateAnimation rotateV, rotateVi, rotateViv, rotateViva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvSplashScreenLetterV = (TextView) findViewById(R.id.tvSplashScreenLetterV);
        tvSplashScreenLetterV.setVisibility(View.GONE);
        tvSplashScreenLetterI = (TextView) findViewById(R.id.tvSplashScreenLetterI);
        tvSplashScreenLetterI.setVisibility(View.GONE);
        tvSplashScreenLetterViv = (TextView) findViewById(R.id.tvSplashScreenLetterViv);
        tvSplashScreenLetterViv.setVisibility(View.GONE);
        tvSplashScreenLetterViva = (TextView) findViewById(R.id.tvSplashScreenLetterViva);
        tvSplashScreenLetterViva.setVisibility(View.GONE);
        tvSplashScreenDescVery = (TextView) findViewById(R.id.tvSplashScreenDescVery);
        tvSplashScreenDescVery.setVisibility(View.GONE);
        tvSplashScreenDescIntelligent = (TextView) findViewById(R.id.tvSplashScreenDescIntelligent);
        tvSplashScreenDescIntelligent.setVisibility(View.GONE);
        tvSplashScreenDescVirtual = (TextView) findViewById(R.id.tvSplashScreenDescVirtual);
        tvSplashScreenDescVirtual.setVisibility(View.GONE);
        tvSplashScreenDescAssistant = (TextView) findViewById(R.id.tvSplashScreenDescAssistant);
        tvSplashScreenDescAssistant.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startVivaAnimation() {
        // V
        rotateV = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateV.setDuration(1000);
        rotateV.setAnimationListener(mAnimationListenerLetterV);
        // Vi
        rotateVi = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateVi.setDuration(1000);
        rotateVi.setAnimationListener(mAnimationListenerLetterVi);
        // Viv
        rotateViv = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateViv.setDuration(1000);
        rotateViv.setAnimationListener(mAnimationListenerLetterViv);
        // Viva
        rotateViva = new RotateAnimation(-90, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateViva.setDuration(1000);
        rotateViva.setAnimationListener(mAnimationListenerLetterViva);
        tvSplashScreenLetterV.startAnimation(rotateV);
    }

    private Animation.AnimationListener mAnimationListenerLetterV = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescVery.setVisibility(View.VISIBLE);
            tvSplashScreenLetterI.setVisibility(View.VISIBLE);
            tvSplashScreenLetterI.startAnimation(rotateVi);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mAnimationListenerLetterVi = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescIntelligent.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViv.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViv.startAnimation(rotateViv);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mAnimationListenerLetterViv = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescVirtual.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViva.setVisibility(View.VISIBLE);
            tvSplashScreenLetterViva.startAnimation(rotateViva);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener mAnimationListenerLetterViva = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            tvSplashScreenDescAssistant.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
