package com.maomovie.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 *
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 *
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
    private boolean isCanScroll = true;
    public HackyViewPager(Context context,String[] srcArray) {
        super(context);
        if(srcArray.length <=1)
            isCanScroll = false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if(isCanScroll){
                return super.onInterceptTouchEvent(ev);
            }else{
                //false  不能左右滑动
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

}

