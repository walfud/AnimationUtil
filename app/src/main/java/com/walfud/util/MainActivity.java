package com.walfud.util;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.walfud.util.animation.Animator;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = "MainActivity";

    private View mDemo;
    private int mOrigLeft;
    private int mOrigTop;
    private int mOrigRight;
    private int mOrigBottom;

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

    boolean mInitLayout = false;
    boolean mCenterXReverse = false;
    boolean mCenterYReverse = false;
    public void onClick(View v) {

        if (!mInitLayout) {
            mInitLayout = true;

            mOrigLeft = mDemo.getLeft();
            mOrigTop = mDemo.getTop();
            mOrigRight = mDemo.getRight();
            mOrigBottom = mDemo.getBottom();
        }

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

            // Alpha
            case R.id.btn_alpha: {
                animator.alpha(0.0, 1.0, 0, DURATION, LISTENER);
            }
                break;
            case R.id.btn_blank: {
                animator.twinkle(new double[]{1.0, 0.0, 1.0, 0.0, 1.0, 0.0}, START_OFFSET, DURATION, LISTENER);
            }
                break;

            // Translate
            case R.id.btn_translate: {
                animator.translate(0, 100, 0, 50, START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_shake: {
                animator.shakeH(new int[]{-50, +100, -100, +100, -100, +100, -50}, START_OFFSET, DURATION, LISTENER);
            }
                break;

            // Scale
            case R.id.btn_scale: {
                animator.scaleCenter(1.0, 2.0, START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_mirrorLeft: {
                animator.mirrorLeft(START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_mirrorCenterX: {
                animator.mirrorCenterX(mCenterXReverse = !mCenterXReverse, START_OFFSET, DURATION, LISTENER);
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
                animator.mirrorCenterY(mCenterYReverse = !mCenterYReverse, START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_mirrorBottom: {
                animator.mirrorBottom(START_OFFSET, DURATION, LISTENER);
            }
                break;

            // Rotate
            case R.id.btn_rotate: {
                animator.rotate(mDemo.getRotation(), mDemo.getRotation() + 30.0,
                        Animation.RELATIVE_TO_SELF, -1.0, Animation.RELATIVE_TO_SELF, -0.5,
                        START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_rotateLeftTop: {
                animator.rotateLeftTop(mDemo.getRotation(), mDemo.getRotation() + 30.0, START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_rotateRightTop: {
                animator.rotateRightTop(mDemo.getRotation(), mDemo.getRotation() + 30.0, START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_rotateLeftBottom: {
                animator.rotateLeftBottom(mDemo.getRotation(), mDemo.getRotation() + 30.0, START_OFFSET, DURATION, LISTENER);
            }
                break;
            case R.id.btn_rotateRightBottom: {
                animator.rotateRightBottom(mDemo.getRotation(), mDemo.getRotation() + 30.0, START_OFFSET, DURATION, LISTENER);
            }
                break;

            // Restore initial status.
            default: {
                mDemo.layout(mOrigLeft, mOrigTop, mOrigRight, mOrigBottom);
                mDemo.setRotation(0);
                mDemo.setVisibility(View.VISIBLE);
            }
                break;
        }
    }
}