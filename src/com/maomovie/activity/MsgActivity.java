package com.maomovie.activity;

import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.maomovie.R;
import com.maomovie.context.MaoApplication;
import com.maomovie.view.MyProgressBar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by YanP on 2016/8/2.
 */
public class MsgActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvTitle;   //  消息标题
    private TextView tvContent; //  消息内容
    private ImageButton btnBack;     //  返回按钮
    private MyProgressBar myProgressBar;
    //通过handler更新进度
    private Handler handler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		if(msg.what == 123){
    			int progress = msg.arg1;
    			myProgressBar.setProgress(progress);
    		}
    	}
    };
    private ThreadHelper threadHelper = new ThreadHelper(handler);
    @Override
    public void onCreate() {
        MaoApplication.addActivityList(this);
        setContentView(R.layout.activity_msg);
        initView();//初始化界面
        setView();//为控件赋值
        setProgress();
    }


    /**
     * 初始化界面
     */
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvMsgTitle);
        tvContent = (TextView) findViewById(R.id.tvMsgContent);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        
        myProgressBar = (MyProgressBar) findViewById(R.id.myProgressBar);
    }

    private void setView() {

        try {
        	//获取消息通知数据
        	Bundle bundle = getIntent().getExtras();
        	String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        	String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        	String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONObject jsonResult = new JSONObject(extras);
            String items = jsonResult.optString("items");
            JSONArray jsonArray = new JSONArray(items);
            JSONObject jsonObj = jsonArray.getJSONObject(0);
            String userName = jsonObj.optString("userName");
            String msgTitle = jsonObj.optString("title");
            String message = jsonObj.optString("message");
            tvTitle.setText(userName + "-" + msgTitle);
            tvContent.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 模拟进度条加载
     */
    private void setProgress(){
    	threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                //模拟加载数据，更新进度
                for(int i=0; i<=100; i+=10){
                    try {
                        Thread.sleep(100);
                        Message msg = handler.obtainMessage(123);
                        msg.arg1 = i;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public void result(Object result) {
                myProgressBar.setVisibility(View.GONE);
                tvContent.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack){
            finish();
        }
    }
}