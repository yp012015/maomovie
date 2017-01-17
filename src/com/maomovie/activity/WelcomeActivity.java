package com.maomovie.activity;

import android.os.Build;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;
import com.maomovie.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.maomovie.context.MaoApplication;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class WelcomeActivity extends Activity {
    private ImageView imgFullsreen;
    private ImageView imgLogo;
    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        MaoApplication.addActivityList(this);
        setContentView(R.layout.activity_welcome);
        initView();//初始化控件

        //2秒后显示广告图片，并进行图片缩放效果
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgFullsreen.setVisibility(View.VISIBLE);
                //切换白色的猫眼logo
                imgLogo.setImageResource(R.drawable.bg_welcome_logo_white);
                //设置屏幕底部的背景色为灰色渐变
                bottomLayout.setBackgroundResource(R.drawable.bg_welcome_bottom);
                bottomLayout.setPadding(0, 0, 0, 20);
                //为状态栏着色
                SystemBarTintManager tintManager = new SystemBarTintManager(WelcomeActivity.this);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarTintResource(R.color.welcome_title);
                //动画初始化
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
                //设置动画时间
                scaleAnimation.setDuration(2000);
                scaleAnimation.setFillAfter(true);
                imgFullsreen.startAnimation(scaleAnimation);
                //3秒后跳转到主界面
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        }, 2000);
        //只对api19以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
    }

    /**
     * 设置沉侵式状态栏
     * @param on
     */
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        imgFullsreen = (ImageView) findViewById(R.id.fullscreen_img);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        bottomLayout = (LinearLayout) findViewById(R.id.fullscreen_content_controls);
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }
}
