package com.maomovie.activity.login;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.maomovie.R;
import com.maomovie.activity.BaseActivity;
import com.maomovie.activity.UserInfoActivity;
import com.maomovie.presenter.login.LoginBizInterface;
import com.maomovie.presenter.login.LoginPresenter;
import com.maomovie.service.LoadingDialog;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yanpeng on 2016/9/25.
 * 用户登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,LoginViewInterface {
    private ImageButton btnBack;//返回按钮
    private Button btnLogin;//登录
    private EditText etAccount;//账号
    private EditText etPassword;//密码
    private TextView tvErrorMsg;
    private Dialog loadingDialog;
    private LoginBizInterface bizInterface;
    @Override
    public void onCreate() {
        setContentView(R.layout.activity_login);
        initView();//初始化控件
//        watchHeap();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //导出来实现接口的方法
        bizInterface = new LoginPresenter(this);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        
        etAccount = (EditText) findViewById(R.id.etAccount);
        etPassword = (EditText) findViewById(R.id.etPassword);

        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);
        loadingDialog = LoadingDialog.createLoadingDialog(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnBack){
            finish();
        } else if (v == btnLogin){
            doLogin();
        }
    }

    private void doLogin() {
        tvErrorMsg.setVisibility(View.INVISIBLE);
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        if(account == null || TextUtils.isEmpty(account)){//用户名为空
            showWarmMsg("用户名不能为空");
            return;
        }
        if(password == null || TextUtils.isEmpty(password)){
            showWarmMsg("密码不能为空");
            return;
        }
        btnLogin.setEnabled(false);
        bizInterface.doLogin(account, password);
    }

    /**
     * 登录完成后需要执行的操作
     * @param errorMsg  错误消息
     * @param errorFlag 错误标识(1成功，0失败，-1通信出错)
     */
    @Override
    public void doLonginResult(String errorMsg, int errorFlag) {
        btnLogin.setEnabled(true);
        if(errorFlag != 1){
            showWarmMsg(errorMsg);
        }
    }

    /**
     * 需要Toast弹窗的消息
     * @param str   消息内容
     */
    @Override
    public void toastMsg(String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    /**
     * 界面文字展示警告信息
     * @param info  警告信息内容
     */
    @Override
    public void showWarmMsg(String info) {
        tvErrorMsg.setVisibility(View.VISIBLE);
        tvErrorMsg.setText(info);
    }

    /**
     * 设置进度条是否可见
     * @param visibile
     */
    @Override
    public void setProgressBarVisibile(int visibile) {
        if(loadingDialog == null){
            return;
        }
        if(visibile == View.VISIBLE){
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }
    
    private void watchHeap(){
    	ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    	StringBuffer sb =new StringBuffer();
    	int memoryClass = manager.getMemoryClass();
    	int largeMemoryClass = manager.getLargeMemoryClass();
    	float total = Runtime.getRuntime().totalMemory() * 1.0f/(1024*1024);
    	float free = Runtime.getRuntime().freeMemory() * 1.0f/(1024*1024);
    	float max = Runtime.getRuntime().maxMemory() * 1.0f/(1024*1024);
    	sb.append("memoryClass: " + memoryClass);
    	sb.append("\nlargeMemoryClass: " + largeMemoryClass);
    	sb.append("\ntotal: " + total);
    	sb.append("\nfree: " + free);
    	sb.append("\nmax: " + max);
    	Log.i("Memory", sb.toString());
    }

    /**
     * 跳转到个人信息界面
     */
    @Override
    public void gotoUserInfoActivity(JSONObject userJson) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("User", userJson.toString());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (bizInterface != null) {
            bizInterface.onDestory();
            bizInterface = null;
        }
        super.onDestroy();
    }


    /**************以下方法主要用于点击屏幕空白处隐藏键盘***********/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，获得焦点。
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                ((EditText)v).setCursorVisible(true);// 光标出现
                return false;
            } else {
                // 失去焦点
                v.clearFocus();
                ((EditText)v).setCursorVisible(false);
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /*********点击屏幕空白处隐藏键盘的方法结束***********/
}