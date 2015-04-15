package com.scho.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
/**
 * 
 * @author liwei
 * @Description: 在代码中实现动画的辅助类，对常用的动画进行封装 
 * @date:2015年2月3日 上午9:59:38
 */
public class AnimationUtil 
{
	/**
	 * 缩放动画
	 * @param fromX
	 * @param toX
	 * @param fromY
	 * @param toY
	 * @param durationMillis
	 * @return
	 */
	public static Animation scaleAnimation(float fromX, float toX,float fromY, float toY, int durationMillis) 
	{
		ScaleAnimation scaleAnimation = new ScaleAnimation(fromX,toX, fromY, toY, 1, 0.5F, 1, 0.5F);
		scaleAnimation.setDuration(durationMillis);
		
		return scaleAnimation;
	}
     
	/**
	 * 旋转动画
	 * @param fromDegrees
	 * @param toDegrees
	 * @param pivotXValue
	 * @param pivotYValue
	 * @param durationMillis
	 * @param repeatCount
	 * @param interpolator
	 * @return
	 */
	public static Animation rotateAnimation(float fromDegrees, float toDegrees,float pivotXValue, float pivotYValue, int durationMillis, int repeatCount,Interpolator interpolator) {
		RotateAnimation localRotateAnimation = new RotateAnimation(fromDegrees,toDegrees, 1, pivotXValue, 1, pivotYValue);
		localRotateAnimation.setRepeatCount(repeatCount);
		localRotateAnimation.setDuration(durationMillis);
		if (interpolator == null)
			interpolator = new LinearInterpolator();
		localRotateAnimation.setInterpolator(interpolator);
		return localRotateAnimation;
	}

	public static Animation translateAnimation(float fromXValue, float toXValue,
			float fromYValue, float toYValue, int durationMillis, long startOffset) 
	{
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.REVERSE,
				fromXValue, Animation.RELATIVE_TO_PARENT, toXValue, Animation.RELATIVE_TO_PARENT, fromYValue, Animation.RELATIVE_TO_PARENT, toYValue);
		translateAnimation.setDuration(durationMillis);
		translateAnimation.setStartOffset(startOffset);
		return translateAnimation;
	}

	public static Animation translateAnimation(float fromXValue, float toXValue, int durationMillis) {
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE,
				fromXValue, Animation.ABSOLUTE, toXValue, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F);
		translateAnimation.setDuration(durationMillis);
		return translateAnimation;
	}

	
	/**
	 * 启动渐变动画，又弱到强
	 * @param view
	 * @param durationMillis
	 * @param startOffset
	 * @param visibility
	 * @param animationListener
	 */
	public static void alphaAnimationShow(View view, int durationMillis, int startOffset,int visibility, Animation.AnimationListener animationListener) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
		alphaAnimation.setDuration(durationMillis);
		alphaAnimation.setStartOffset(startOffset);
		startAnimation(view, alphaAnimation, visibility, animationListener);
	}
	
	/**
	 * 启动渐变动画，由强到弱
	 * @param view
	 * @param durationMillis
	 * @param startOffset
	 * @param visibility
	 * @param animationListener
	 */
	public static void alphaAnimation(View view, int durationMillis, int startOffset,int visibility, Animation.AnimationListener animationListener) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0F, 0.0F);
		alphaAnimation.setDuration(durationMillis);
		alphaAnimation.setStartOffset(startOffset);
		startAnimation(view, alphaAnimation, visibility, animationListener);
	}

	/**
	 * Y坐标移动
	 * @param fromYValue
	 * @param toYValue
	 * @param paramInt
	 * @return
	 */
	public static Animation translateAnimationInY(float fromYValue, float toYValue, int durationMillis) {
		TranslateAnimation localTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0F, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, fromYValue, Animation.RELATIVE_TO_SELF, toYValue);
		localTranslateAnimation.setDuration(durationMillis);
		return localTranslateAnimation;
	}

	
	public static Animation alphaAnimation(float fromAlpha, float toAlpha, int durationMillis) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha,toAlpha);
		alphaAnimation.setDuration(durationMillis);
		return alphaAnimation;
	}
	

	/**
	 * 启动动画
	 * @param view
	 * @param animation
	 * @param visibility
	 * @param animationListener
	 */
	public static void startAnimation(View view, Animation animation,
			int visibility, Animation.AnimationListener animationListener) {
		view.setVisibility(visibility);
		if (animationListener != null)
			animation.setAnimationListener(animationListener);
		view.startAnimation(animation);
		
	}
}
