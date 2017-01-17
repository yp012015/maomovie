package com.maomovie.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.maomovie.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by YanP on 2016/8/17.
 */
public abstract class BaseActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //只对api19以上版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        //为状态栏着色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        setStatusBarColor(tintManager);
        onCreate();
    }

    /**
     * 为状态栏设置颜色
     * @param tintManager
     */
    protected void setStatusBarColor(SystemBarTintManager tintManager) {
        tintManager.setStatusBarTintResource(R.color.color_moviebtn_normal);
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
     * 抽象方法，自己实现加载布局文件的操作
     */
    public abstract void onCreate();
}