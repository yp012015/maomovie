package com.maomovie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by yanpeng on 2016/12/14.
 * 通过回调实现跟随手指的小球
 */
public class DrawView extends View {
    private float currentX = 40;
    private float currentY = 50;
    //定义、创建画笔
    Paint paint = new Paint();
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取窗口服务
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕宽高
        float width = windowManager.getDefaultDisplay().getWidth();
        float height = windowManager.getDefaultDisplay().getHeight();
        currentX = width/2;
        currentY = height/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画笔颜色
        paint.setColor(Color.argb(122,255,0,0));
        //绘制圆圈
        canvas.drawCircle(currentX,currentY,50,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.currentX = event.getX();
        this.currentY = event.getY();
        this.invalidate();//通知组件重绘
        return true;
    }
}
