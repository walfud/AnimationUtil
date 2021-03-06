package com.walfud.util.animation;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by song on 15-3-24.
 */
public class Animator {

    public static final String TAG = "com.walfud.util.animation.Animator";

    private View mTargetView;

    public Animator(View mTargetView) {
        this.mTargetView = mTargetView;
    }

    /////////////////// Start animation procedure.
    /**
     * Basic Aplha animation, automatically `clearAnimation` at the `onAnimationEnd`.
     * @param from
     * @param to
     * @param startOffset
     * @param duration
     * @param listener
     */
    public void alpha(double from, double to, int startOffset, int duration, final Animation.AnimationListener listener) {
        Animation alphaAnimation = newAlphaAnimation(from, to, startOffset, duration, new ClearAnimationListener(mTargetView, listener));

        mTargetView.startAnimation(alphaAnimation);
    }

    /**
     * Twinkle animation, automatically `clearAnimation` at the `onAnimationEnd`.
     * @param proc
     * @param startOffset
     * @param duration
     * @param listener
     */
    public void twinkle(double[] proc, int startOffset, int duration, final Animation.AnimationListener listener) {

        Animation firstAnimation = null;

        double from1 = proc[0];
        double to1 = proc[1];
        Animation animation1 = newAlphaAnimation(from1, to1, startOffset, duration, null);
        firstAnimation = animation1;

        for (int i = 2; i < proc.length; i++) {
            double from2 = proc[i - 1];
            double to2 = proc[i];
            final Animation animation2 = newAlphaAnimation(from2, to2, 0, duration, null);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTargetView.clearAnimation();
                    mTargetView.startAnimation(animation2);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animation1 = animation2;
        }
        Animation lastAnimation = animation1;
        lastAnimation.setAnimationListener(new ClearAnimationListener(mTargetView, listener));

        mTargetView.startAnimation(firstAnimation);
    }

    /**
     * Basic Translate animation
     */
    public void translate(int fromX, final int toX, int fromY, final int toY, int startOffset, int duration, final Animation.AnimationListener listener) {

        //
        final int newLeft = mTargetView.getLeft() + toX;
        final int newTop = mTargetView.getTop() + toY;
        final int newRight = mTargetView.getRight() + toX;
        final int newBottom = mTargetView.getBottom() + toY;

        // Animation
        Animation translateAnimation = newTranslateAnimation(fromX, toX, fromY, toY, startOffset, duration,
                new TranslateListener(newLeft, newTop, newRight, newBottom,
                        new ClearAnimationListener(mTargetView, listener)));

        mTargetView.startAnimation(translateAnimation);
    }
    public void shakeH(int[] proc, int startOffset, int duration, final Animation.AnimationListener listener) {

        AnimationSet animationSet = new AnimationSet(true);

        Animation lastAnimation = null;
        for (int i = 0; i < proc.length; i++) {
            int toX = proc[i];
            Animation animation = newTranslateAnimation(0, toX, 0, 0, startOffset + (i) * duration, duration, null);
            animationSet.addAnimation(animation);

            lastAnimation = animation;
        }
        lastAnimation.setAnimationListener(new ClearAnimationListener(mTargetView, listener));

        mTargetView.startAnimation(animationSet);
    }

    /**
     *
     * @param fromX
     * @param toX
     * @param fromY
     * @param toY
     * @param startOffset
     * @param duration
     * @param listener
     */
    public void scale(double fromX, double toX, double fromY, double toY,
                      int startOffset, int duration,
                      Animation.AnimationListener listener) {
        scale(fromX, toX, fromY, toY,
                Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
                startOffset, duration,
                listener);
    }
    public void scale(double fromX, double toX, double fromY, double toY,
                      int pivotXType, double pivotXValue, int pivotYType, double pivotYValue,
                      int startOffset, int duration,
                      Animation.AnimationListener listener) {

        // In case of 'RELATIVE_TO_SELF' or 'RELATIVE_TO_PARENT',
        // the Scale Algorithm is follow:
        //   We take the X coordinate as example,
        //   First get the pivot value: `pivotX` = `left` value + `width * pivotXValue`
        //   You must know that the scale value is relative to pivot. So we'd get the
        // 'original distance'(left as example) relative to pivot: `left - pivotX`.
        //   Then, calculate scale value: `new distance` = `original distance` * `pivotValue`.
        //   Now we can get the right position after animation: `pivotX` + `new distance`.
        // Have fun!

        int pivotX = calculatePivotX(mTargetView, pivotXType, pivotXValue);
        final int newLeft = (int) (pivotX + (mTargetView.getLeft() - pivotX) * toX);
        final int newRight = (int) (pivotX + (mTargetView.getRight() - pivotX) * toX);

        int pivotY = calculatePivotY(mTargetView, pivotYType, pivotYValue);
        final int newTop = (int) (pivotY + (mTargetView.getTop() - pivotY) * toY);
        final int newBottom = (int) (pivotY + (mTargetView.getBottom() - pivotY) * toY);

        // Fix 'left > right' or 'top > bottom'
        final int fixedNewLeft = Math.min(newLeft, newRight);
        final int fixedNewRight = Math.max(newLeft, newRight);
        final int fixedNewTop = Math.min(newTop, newBottom);
        final int fixedNewBottom = Math.max(newTop, newBottom);

        Animation scaleAnimation = newScaleAnimation(
                fromX, toX, fromY, toY,
                pivotXType, pivotXValue, pivotYType, pivotYValue,
                startOffset, duration,
                new ClearAnimationListener(
                        new TranslateListener(fixedNewLeft, fixedNewTop, fixedNewRight, fixedNewBottom, mTargetView, listener)));

        mTargetView.startAnimation(scaleAnimation);

    }
    public void scaleCenter(double from, double to, int startOffset, int duration,
                        Animation.AnimationListener listener) {
        scale(from, to, from, to,
                Animation.RELATIVE_TO_SELF, 0.5, Animation.RELATIVE_TO_SELF, 0.5,
                startOffset, duration, listener);
    }
    public void mirrorLeft(int startOffset, int duration,
                           Animation.AnimationListener listener) {
        scale(1.0, -1.0, 1.0, 1.0,
                Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 0.0,
                startOffset, duration,
                listener);
    }
    public void mirrorCenterX(boolean reverse,
                              int startOffset, int duration,
                              Animation.AnimationListener listener) {
        scale(!reverse ? 1.0 : -1.0, !reverse ? -1.0 : 1.0, 1.0, 1.0,
                Animation.RELATIVE_TO_SELF, 0.5, Animation.RELATIVE_TO_SELF, 0.0,
                startOffset, duration, listener);
    }
    public void mirrorRight(int startOffset, int duration,
                            Animation.AnimationListener listener) {
        scale(1.0, -1.0, 1.0, 1.0,
                Animation.RELATIVE_TO_SELF, 1.0, Animation.RELATIVE_TO_SELF, 0.0,
                startOffset, duration,
                listener);
    }
    public void mirrorTop(int startOffset, int duration,
                          Animation.AnimationListener listener) {
        scale(1.0, 1.0, 1.0, -1.0,
                Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 0.0,
                startOffset, duration,
                listener);
    }
    public void mirrorCenterY(boolean reverse,
                              int startOffset, int duration,
                              Animation.AnimationListener listener) {
        scale(1.0, 1.0, !reverse ? 1.0 : -1.0, !reverse ? -1.0 : 1.0,
                Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 0.5,
                startOffset, duration, listener);
    }
    public void mirrorBottom(int startOffset, int duration,
                             Animation.AnimationListener listener) {
        scale(1.0, 1.0, 1.0, -1.0,
                Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 1.0,
                startOffset, duration,
                listener);
    }

    // Rotate
    public void rotate(final double fromDegrees, final double toDegrees,
                       int pivotXType, double pivotXValue, int pivotYType, double pivotYValue,
                       int startOffset, int duration,
                       Animation.AnimationListener listener) {

        // Current status.
        final int currentWidth = mTargetView.getWidth();
        final int currentHeight = mTargetView.getHeight();
        final int currentLeft = mTargetView.getLeft();
        final int currentTop = mTargetView.getTop();
        final int currentRight = mTargetView.getRight();
        final int currentBottom = mTargetView.getBottom();
        final int currentCenterX = (currentLeft + currentRight) / 2;
        final int currentCenterY = (currentTop + currentBottom) / 2;
        final int currentDegrees = (int) mTargetView.getRotation();
        final int currentOPointX = calculatePivotX(mTargetView, pivotXType, pivotXValue);     // pivot X
        final int currentOPointY = calculatePivotY(mTargetView, pivotYType, pivotYValue);     // pivot Y

        // Get the rotation radius
        final int initDegrees = (int) Math.toDegrees(Math.atan2(currentCenterY - currentOPointY, currentCenterX - currentOPointX));
        final int r = calculateDistance(currentCenterX, currentCenterY, currentOPointX, currentOPointY);
        final int oX = (int) (currentCenterX - r * Math.cos(Math.toRadians(initDegrees + currentDegrees)));
        final int oY = (int) (currentCenterY - r * Math.sin(Math.toRadians(initDegrees + currentDegrees)));

        // Calculate original position( degree is 0 ).
        final Point origCenter = pointRotate(new Point(oX, oY), new Point(currentCenterX, currentCenterY), -currentDegrees);
        final int origLeft = origCenter.x - currentWidth / 2;
        final int origTop = origCenter.y - currentHeight / 2;
        final int origRight = origLeft + currentWidth;
        final int origBottom = origTop + currentHeight;

        // Calculate the finish position and rotation from original.
        final Point finishLeftTop = pointRotate(
                new Point(oX, oY),
                new Point(origLeft, origTop),
                toDegrees);
        final Point finishRightBottom = pointRotate(
                new Point(oX, oY),
                new Point(origRight, origBottom),
                toDegrees);

        // Equivalent pre-rotation position.
        final int finishCenterX = (finishLeftTop.x + finishRightBottom.x) / 2;
        final int finishCenterY = (finishLeftTop.y + finishRightBottom.y) / 2;
        final int finishLeft = finishCenterX - currentWidth / 2;
        final int finishTop = finishCenterY - currentHeight / 2;
        final int finishRight = finishLeft + currentWidth;
        final int finishBottom = finishTop + currentHeight;

        Animation rotateAnimation = newRotateAnimation(
                fromDegrees, toDegrees,
                pivotXType, pivotXValue, pivotYType, pivotYValue,
                startOffset, duration,
                new ClearAnimationListener(mTargetView, listener) {

                    @Override
                    public void onAnimationStart(Animation animation) {

                        // Reset the view to original position.
                        mTargetView.layout(origLeft, origTop, origRight, origBottom);
                        mTargetView.setRotation(0);

                        super.onAnimationStart(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        mTargetView.layout(finishLeft, finishTop, finishRight, finishBottom);
                        mTargetView.setRotation((float) toDegrees);

                        super.onAnimationEnd(animation);
                    }
                });

        mTargetView.startAnimation(rotateAnimation);
    }
    public void rotateLeftTop(double fromDegrees, double toDegrees,
                              int startOffset, int duration,
                              Animation.AnimationListener listener) {
        rotate(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 0.0,
                startOffset, duration, listener);
    }
    public void rotateRightTop(double fromDegrees, double toDegrees,
                              int startOffset, int duration,
                              Animation.AnimationListener listener) {
        rotate(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 1.0, Animation.RELATIVE_TO_SELF, 0.0,
                startOffset, duration, listener);
    }
    public void rotateLeftBottom(double fromDegrees, double toDegrees,
                              int startOffset, int duration,
                              Animation.AnimationListener listener) {
        rotate(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.0, Animation.RELATIVE_TO_SELF, 1.0,
                startOffset, duration, listener);
    }
    public void rotateRightBottom(double fromDegrees, double toDegrees,
                              int startOffset, int duration,
                              Animation.AnimationListener listener) {
        rotate(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 1.0, Animation.RELATIVE_TO_SELF, 1.0,
                startOffset, duration, listener);
    }

    ////////////////////// Create an animation object.
    public static Animation newAlphaAnimation(double from, double to,
                                        int startOffset, int duration,
                                        Animation.AnimationListener listener) {

        Animation alphaAnimation = new AlphaAnimation((float) from, (float) to);

        alphaAnimation.setStartOffset(startOffset);
        alphaAnimation.setDuration(duration);

        alphaAnimation.setFillBefore(false);
        alphaAnimation.setFillEnabled(true);

        alphaAnimation.setFillAfter(true);

        alphaAnimation.setAnimationListener(listener);

        return alphaAnimation;
    }

    // TODO: parameter should be 'dp' instead of 'px'
    public static Animation newTranslateAnimation(int fromX, int toX, int fromY, int toY,
                                            int startOffset, int duration,
                                            Animation.AnimationListener listener) {

        TranslateAnimation translateAnimation = new TranslateAnimation((float) fromX, (float) toX, (float) fromY, (float) toY);
        translateAnimation.setStartOffset(startOffset);
        translateAnimation.setDuration(duration);

        translateAnimation.setFillBefore(false);
        translateAnimation.setFillEnabled(true);

        translateAnimation.setFillAfter(true);

        translateAnimation.setAnimationListener(listener);

        return translateAnimation;
    }

    public static Animation newScaleAnimation(double fromX, double toX, double fromY, double toY,
                                                  int startOffset, int duration,
                                                  Animation.AnimationListener listener) {
        return autoFillAnimationFields(
                new ScaleAnimation((float) fromX, (float) toX, (float) fromY, (float) toY),
                startOffset, duration, listener);
    }
    public static Animation newScaleAnimation(double fromX, double toX, double fromY, double toY,
                                              int pivotXType, double pivotXValue, int pivotYType, double pivotYValue,
                                              int startOffset, int duration,
                                              Animation.AnimationListener listener) {
        return autoFillAnimationFields(
                new ScaleAnimation((float) fromX, (float) toX, (float) fromY, (float) toY, pivotXType, (float) pivotXValue, pivotYType, (float) pivotYValue),
                startOffset, duration, listener);
    }
    public static Animation newRotateAnimation(double fromDegrees, double toDegrees,
                                               int pivotXType, double pivotXValue, int pivotYType, double pivotYValue,
                                               int startOffset, int duration,
                                               Animation.AnimationListener listener) {
        return autoFillAnimationFields(
                new RotateAnimation((float) fromDegrees, (float) toDegrees, pivotXType, (float) pivotXValue, pivotYType, (float) pivotYValue),
                startOffset, duration, listener);
    }

    // Utility
    private static Animation autoFillAnimationFields(Animation animation, int startOffset, int duration, Animation.AnimationListener listener) {
        animation.setStartOffset(startOffset);
        animation.setDuration(duration);

        animation.setFillBefore(false);
        animation.setFillEnabled(true);

        animation.setFillAfter(true);

        animation.setAnimationListener(listener);

        return animation;
    }
    private static int calculatePivot(int startValue, int length, int pivotType, double pivotValue) {
        int pivot = 0;

        switch (pivotType) {
            case Animation.ABSOLUTE:
                pivot = startValue;
                break;

            case Animation.RELATIVE_TO_SELF:
            case Animation.RELATIVE_TO_PARENT:
                pivot = startValue + (int) (length * pivotValue);
                break;

            default:
                break;
        }

        return pivot;
    }
    private static int calculatePivotX(View view, int pivotXType, double pivotXValue) {
        int pivotX = 0;
        switch (pivotXType) {
            case Animation.ABSOLUTE:
            case Animation.RELATIVE_TO_SELF:
                pivotX = calculatePivot(view.getLeft(), view.getWidth(),
                        pivotXType, pivotXValue);
                break;

            case Animation.RELATIVE_TO_PARENT:
                pivotX = calculatePivot(((ViewGroup) view.getParent()).getLeft(), ((ViewGroup) view.getParent()).getWidth(),
                        pivotXType, pivotXValue);
                break;

            default:
                break;
        }
        return pivotX;
    }
    private static int calculatePivotY(View view, int pivotYType, double pivotYValue) {
        int pivotY = 0;
        switch (pivotYType) {
            case Animation.ABSOLUTE:
            case Animation.RELATIVE_TO_SELF:
                pivotY = calculatePivot(view.getTop(), view.getHeight(),
                        pivotYType, pivotYValue);
                break;

            case Animation.RELATIVE_TO_PARENT:
                pivotY = calculatePivot(((ViewGroup) view.getParent()).getTop(), view.getHeight(),
                        pivotYType, pivotYValue);
                break;

            default:
                break;
        }
        return pivotY;
    }
    private static Point pointRotate(Point o, Point target, double toDegrees) {
        final int r = calculateDistance(target.x, target.y, o.x, o.y);
        final double finalDegrees = Math.toDegrees(Math.atan2(target.y - o.y, target.x - o.x)) + toDegrees;

        final Point finalPoint = new Point();
        finalPoint.x = o.x + (int) (r * Math.cos(Math.toRadians(finalDegrees)));
        finalPoint.y = o.y + (int) (r * Math.sin(Math.toRadians(finalDegrees)));
        return finalPoint;
    }
    private static int calculateDistance(Point a, Point b) {
        return (int) Math.sqrt(
                Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)
        );
    }
    private static int calculateDistance(int x1, int y1, int x2, int y2) {
        return calculateDistance(new Point(x1, y1), new Point(x2, y2));
    }

    ///////////////////
    public static class AnimationListenerBase implements Animation.AnimationListener {

        protected View mTargetView;
        protected Animation.AnimationListener mUserListener;

        public AnimationListenerBase(View targetView, Animation.AnimationListener userListener) {
            mTargetView = targetView;
            mUserListener = userListener;
        }
        public AnimationListenerBase(AnimationListenerBase userListener) {
            if (userListener != null) {
                mTargetView = userListener.mTargetView;
                mUserListener = userListener;
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (mUserListener != null) {
                mUserListener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mUserListener != null) {
                mUserListener.onAnimationEnd(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            if (mUserListener != null) {
                mUserListener.onAnimationRepeat(animation);
            }
        }
    }

    public static class ClearAnimationListener extends AnimationListenerBase {

        public ClearAnimationListener(View targetView, Animation.AnimationListener userListener) {
            super(targetView, userListener);
        }
        public ClearAnimationListener(AnimationListenerBase userListener) {
            super(userListener);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mTargetView.clearAnimation();

            super.onAnimationEnd(animation);
        }
    }

    public static class TranslateListener extends AnimationListenerBase {

        int mNewLeft = 0;
        int mNewTop = 0;
        int mNewRight = 0;
        int mNewBottom = 0;

        public TranslateListener(int newLeft, int newTop, int newRight, int newBottom,
                                 View targetView, Animation.AnimationListener userListener) {
            super(targetView, userListener);

            mNewLeft = newLeft;
            mNewTop = newTop;
            mNewRight = newRight;
            mNewBottom = newBottom;
        }
        public TranslateListener(int newLeft, int newTop, int newRight, int newBottom,
                                 AnimationListenerBase userListener) {
            this(newLeft, newTop, newRight, newBottom,
                    userListener != null ? userListener.mTargetView : null, userListener);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mTargetView.layout(mNewLeft, mNewTop, mNewRight, mNewBottom);

            super.onAnimationEnd(animation);
        }
    }

    public static class VisibilityAnimationListener extends AnimationListenerBase {

        int mVisibilityOnStart = View.INVISIBLE;
        int mVisibilityOnEnd = View.VISIBLE;

        public VisibilityAnimationListener(int visibilityOnStart, int visibilityOnEnd,
                                           View targetView, Animation.AnimationListener userListener) {
            super(targetView, userListener);

            mVisibilityOnStart = visibilityOnStart;
            mVisibilityOnEnd = visibilityOnEnd;
        }
        public VisibilityAnimationListener(int visibilityOnStart, int visibilityOnEnd,
                                           AnimationListenerBase userListener) {
            this(visibilityOnStart, visibilityOnEnd,
                    userListener != null ? userListener.mTargetView : null, userListener);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            super.onAnimationStart(animation);

            mTargetView.setVisibility(mVisibilityOnStart);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mTargetView.setVisibility(mVisibilityOnEnd);

            super.onAnimationEnd(animation);
        }
    }
}
