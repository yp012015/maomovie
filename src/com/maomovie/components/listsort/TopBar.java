package com.maomovie.components.listsort;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.maomovie.R;

/**
 * Created by YanP on 2016/7/12.
 * 功能：自定义控件-顶部标题栏
 */
public class TopBar extends RelativeLayout {
    private TextView tvTitle;
    private Button leftButton,rightButton;

    private String titleText;           //  标题文本
    private float titleTextSize;        //  文字大小
    private int titleTextColor;         //  标题字体颜色
    private Drawable titleBackground;   //  标题背景
    
    private String leftText;            //  左边按钮文本
    private float leftTextSize;         //  左边按钮文字大小
    private int leftTextColor;          //  左边按钮文字颜色
    private Drawable leftBackground;    //  左边按钮背景引用
    
    private String rightText;       //  右边按钮文本
    private float rightTextSize;        //  右边按钮文字大小
    private int rightTextColor;         //  右边按钮文字颜色
    private Drawable rightBackground;   //  右边按钮背景引用

    private LayoutParams titleParams,leftParams,rightParams;
    
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.TopBar);

        titleText = typedArray.getString(R.styleable.TopBar_titleText);
        titleTextSize = typedArray.getFloat(R.styleable.TopBar_titleTextSize, 0);
        titleTextColor = typedArray.getInt(R.styleable.TopBar_titleTextColor, 0);
        titleBackground = typedArray.getDrawable(R.styleable.TopBar_titleBackground);

        leftText = typedArray.getString(R.styleable.TopBar_leftText);
        leftTextSize = typedArray.getFloat(R.styleable.TopBar_leftTextSize, 0);
        leftTextColor = typedArray.getInt(R.styleable.TopBar_leftTextColor, 0);
        leftBackground = typedArray.getDrawable(R.styleable.TopBar_leftBackground);

        rightText = typedArray.getString(R.styleable.TopBar_rightText);
        rightTextSize = typedArray.getFloat(R.styleable.TopBar_rightTextSize, 0);
        rightTextColor = typedArray.getInt(R.styleable.TopBar_rightTextColor, 0);
        rightBackground = typedArray.getDrawable(R.styleable.TopBar_rightBackground);

        typedArray.recycle();//回收

        tvTitle = new TextView(context);
        leftButton = new Button(context);
        rightButton = new Button(context);

        tvTitle.setText(titleText);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setBackground(titleBackground);
        tvTitle.setGravity(Gravity.CENTER);

        setBackgroundColor(0xffd43e37);

        leftButton.setText(leftText);
        leftButton.setTextSize(leftTextSize);
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setGravity(Gravity.CENTER_VERTICAL);

        rightButton.setText(rightText);
        rightButton.setTextSize(rightTextSize);
        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setGravity(Gravity.CENTER_VERTICAL);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);

        addView(tvTitle, titleParams);
        addView(leftButton, leftParams);
        addView(rightButton,rightParams);
    }

}
