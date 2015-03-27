package com.walfud.util;

import com.walfud.util.animation.Animator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    private View mDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemo = findViewById(R.id.txt_demo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        final int START_OFFSET = 0;
        final int DURATION = 234;
        final Animation.AnimationListener LISTENER = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "end");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "repeat");
            }
        };

        Animator animator = new Animator(mDemo);

        switch (v.getId()) {
            case R.id.btn_alpha: {
                animator.alpha(0.0, 1.0, 0, DURATION, LISTENER);
            }
            break;
            case R.id.btn_blank: {
                animator.twinkle(new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0}, START_OFFSET, DURATION, LISTENER);
            }
            break;

            case R.id.btn_translate: {
                animator.translate(0, 100, 0, 50, START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_shake: {
                animator.shakeH(new int[] {-50, +100, -100, +100, -100, +100, -50}, START_OFFSET, DURATION, LISTENER);
            }
            break;

            case R.id.btn_scale: {
                animator.scale(1.0, 1.0, 0.0, 1.0,
                        Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 0.5,
                        START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_mirrorLeft: {
                animator.mirrorLeft(START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_mirrorCenterX: {
                animator.mirrorCenterX(START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_mirrorRight: {
                animator.mirrorRight(START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_mirrorTop: {
                animator.mirrorTop(START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_mirrorCenterY: {
                animator.mirrorCenterY(START_OFFSET, DURATION, LISTENER);
            }
            break;
            case R.id.btn_mirrorBottom: {
                animator.mirrorBottom(START_OFFSET, DURATION, LISTENER);
            }
            break;

            default: {

                Animation animation = new ScaleAnimation((float) 2.0, (float) 3.0, (float) 1.0, (float) 1.0,
                        Animation.RELATIVE_TO_SELF, (float) 0.0, Animation.RELATIVE_TO_SELF, (float) 0.0);
                animation.setFillBefore(false);
                animation.setFillEnabled(true);
                animation.setFillAfter(true);
                animation.setStartOffset(0);
                animation.setDuration(DURATION);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        final int newLeft = mDemo.getLeft() - mDemo.getWidth();
                        final int newTop = mDemo.getTop();
                        final int newRight = mDemo.getRight() - mDemo.getWidth();
                        final int newBottom = mDemo.getBottom();

                        mDemo.clearAnimation();

                        final int newLeft2 = mDemo.getLeft() - mDemo.getWidth();
                        final int newTop2 = mDemo.getTop();
                        final int newRight2 = mDemo.getRight() - mDemo.getWidth();
                        final int newBottom2 = mDemo.getBottom();
//                        mDemo.setVisibility(View.INVISIBLE);
//                        mDemo.layout(newLeft, newTop, newRight, newBottom);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mDemo.startAnimation(animation);
            }
                break;
        }
    }

}