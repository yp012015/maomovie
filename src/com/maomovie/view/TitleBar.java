package com.maomovie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.maomovie.R;

/**
 * Created by yanpeng on 2017/1/6.
 */
public class TitleBar extends RelativeLayout{
    // 返回按钮控件
    private ImageButton mLeftBtn;
    // 标题Tv
    private TextView mTitleTv;

    /**
     * 未使用自定义属性时，调用该构造方法
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.title_bar, this);

        // 获取控件
        mLeftBtn = (ImageButton) findViewById(R.id.btnBack);
        mTitleTv = (TextView) findViewById(R.id.tvTitle);
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setLeftButtonListener(OnClickListener listener) {
        mLeftBtn.setOnClickListener(listener);
    }

    // 设置标题的方法
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }
}
