package com.yym.lovelayout;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoveLayout extends RelativeLayout {
    private Drawable firstDrawable;
    private Drawable secondDrawable;
    private Drawable threeDrawable;
    private int dHeight;//图片的高度
    private int dWidth;//图片的宽度
    private int mWidth = 1080;//整个布局的宽度
    private int mHeight = 1854;//整个布局的高度
    List<Drawable> mDrawablesList = new ArrayList<Drawable>();
    private LayoutParams params;
    private Random random = new Random();

    public LoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        firstDrawable = getResources().getDrawable(R.mipmap.iv1);
        mDrawablesList.add(firstDrawable);
        secondDrawable = getResources().getDrawable(R.mipmap.iv2);
        mDrawablesList.add(secondDrawable);
        threeDrawable = getResources().getDrawable(R.mipmap.iv3);
        mDrawablesList.add(threeDrawable);
        firstDrawable = getResources().getDrawable(R.mipmap.iv4);
        mDrawablesList.add(firstDrawable);
        secondDrawable = getResources().getDrawable(R.mipmap.iv5);
        mDrawablesList.add(secondDrawable);
        threeDrawable = getResources().getDrawable(R.mipmap.iv6);
        mDrawablesList.add(threeDrawable);
        //得到图片的宽高
        dHeight = firstDrawable.getIntrinsicHeight();
        dWidth = firstDrawable.getIntrinsicWidth();
        params = new LayoutParams(dWidth, dHeight);
        //给控件动态布局，使得始终在布局最底部的中间位置
        params.addRule(CENTER_HORIZONTAL, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //得到本布局的宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addLove() {
        final ImageView mImageView = new ImageView(getContext());
        mImageView.setImageDrawable(mDrawablesList.get(random.nextInt(3)));//通过随机对象，随机在这三张图片产生任意一张图片
        mImageView.setLayoutParams(params);
        addView(mImageView);
        //属性动画控制坐标
        AnimatorSet set = getAnimator(mImageView);//通过getAnimator得到整个所有动画集合
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private AnimatorSet getAnimator(ImageView mImageView) {
        ValueAnimator bezierValueAnimator = getBeziValueAnimator(mImageView);
        AnimatorSet bezierAnimatorSet = new AnimatorSet();
        bezierAnimatorSet.playSequentially(bezierValueAnimator);
        bezierAnimatorSet.setTarget(mImageView);
        return bezierAnimatorSet;
    }

    /**
     * @author mikyou
     * getBeziValueAnimator
     * 构造一个贝塞尔曲线动画
     */
    private ValueAnimator getBeziValueAnimator(final ImageView mImageView) {
        //贝塞尔曲线动画,不断修改ImageView的坐标,PointF(x,y)
        PointF pointF0 = new PointF(mWidth / 2 - dWidth / 2, mHeight);// 起点
        PointF pointF1 = new PointF(getRandom(mWidth / 2 - 100, mWidth / 2 + 100), mHeight / 2);// 第二个点
        PointF pointF3 = getPointF(3);//终点
        PointF pointF2 = getPointF(2);
        BezierEvalutor mBezierEvalutor = new BezierEvalutor(pointF1, pointF2);//创建一个估值器，然后并把P1，P2点传入
        ValueAnimator animator = ValueAnimator.ofObject(mBezierEvalutor, pointF0, pointF3);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();    //通过addUpdateListener监听事件实时获得从mBezierEvalutor估值器对象evalute方法实时计算出最新点的坐标	。
                mImageView.setX(pointF.x);
                mImageView.setY(pointF.y);
                if (animation.getAnimatedFraction() > 0.9) {
                    mImageView.setAlpha(1 - animation.getAnimatedFraction());//getAnimatedFraction()就是mBezierEvalutor估值器对象中evaluate方法t即时间因子,从0~1变化,所以透明度应该是从1~0变化正好到了顶部，t变为1，透明度变为0，即消失
                }
            }
        });
        animator.setTarget(mImageView);
        animator.setDuration(2000);
        return animator;
    }

    private PointF getPointF(int i) {
        PointF pointF = new PointF();
        int pointF3Y = 0;
        if (i == 2) {//P2点Y轴坐标变化
            pointF.x = random.nextInt(mWidth / 2 - dWidth / 2);
//            pointF.y = pointF3Y / 2;
            pointF.y = getRandom( - mHeight,mHeight / 2);
        } else if (i == 3) {
            pointF3Y = random.nextInt(mHeight);
            pointF.x = getRandom(-mWidth / 2, mWidth + mWidth);
            pointF.y = pointF3Y + mHeight / 2;
        }

        return pointF;
    }

    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

}
