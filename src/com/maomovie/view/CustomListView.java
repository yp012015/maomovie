package com.maomovie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.maomovie.R;

/**
 * Created by yanpeng on 2017/1/9.
 */
public class CustomListView extends ListView implements GestureDetector.OnGestureListener, View.OnTouchListener {
    // 删除按钮
    private View mDeleteBtn;
    // 列表项布局
    private ViewGroup mItemLayout;
    // 选择的列表项
    private int mSelectedItem;
    // 当前删除按钮是否显示出来了
    private boolean isDeleteShown;
    // 手势动作探测器
    private GestureDetector mGestureDetector;
    // 删除事件监听器
    public interface OnDeleteListener {
        void onDelete(int index);
    }

    private OnDeleteListener mOnDeleteListener;

    // 设置删除监听事件
    public void setOnDeleteListener(OnDeleteListener listener) {
        mOnDeleteListener = listener;
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //创建手势监听器
        mGestureDetector = new GestureDetector(this);
        //监听onTouch事件
        setOnTouchListener(this);
    }

    // 隐藏删除按钮
    public void hideDelete() {
        mItemLayout.removeView(mDeleteBtn);
        mDeleteBtn = null;
        isDeleteShown = false;
    }

    public boolean isDeleteShown() {
        return isDeleteShown;
    }

    //触摸监听
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(isDeleteShown){
            hideDelete();
            return false;
        } else {
            return mGestureDetector.onTouchEvent(event);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!isDeleteShown) {
            mSelectedItem = pointToPosition((int) e.getX(), (int) e.getY());
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // 如果当前删除按钮没有显示出来，并且x方向滑动的速度大于y方向的滑动速度
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)) {
            mDeleteBtn = LayoutInflater.from(getContext()).inflate(R.layout.delete_btn, null);

            mDeleteBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemLayout.removeView(mDeleteBtn);
                    mDeleteBtn = null;
                    isDeleteShown = false;
                    mOnDeleteListener.onDelete(mSelectedItem);
                }
            });
            /************此处如果ListView添加了HeaderView，需要判断滑动的是不是第0个item********************/
            if(mSelectedItem == 0){
                return false;
            }
            /*******************************************************************************************/
            mItemLayout = (ViewGroup) getChildAt(mSelectedItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            if (mItemLayout != null) {
                mItemLayout.addView(mDeleteBtn, params);
                isDeleteShown = true;
            }
        }

        return false;
    }
}
