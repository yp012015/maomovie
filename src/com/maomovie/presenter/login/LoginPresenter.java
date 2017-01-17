package com.maomovie.presenter.login;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.maomovie.activity.login.LoginViewInterface;
import com.maomovie.util.HttpUtil;
import com.maomovie.util.MD5Utils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yanpeng on 2016/11/7.
 */
public class LoginPresenter implements LoginBizInterface {
    private Handler handler;
    private LoginViewInterface loginView;
    public LoginPresenter(LoginViewInterface loginView) {
        this.loginView = loginView;
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                setProgressbarVisible(View.GONE);
                switch (msg.what){
                    case -2://异常
                        doLoginResult("登录过程中发生了意外",msg.what);
                        break;
                    case -1://网络访问不成功
                        doLoginResult("请求服务失败", msg.what);
                        break;
                    case 0://接口返回错误信息
                        doLoginResult(msg.obj.toString(),msg.what);
                        break;
                    case 1://登录成功
                        doLoginResult(null,msg.what);
                        gotoUserInfoActivity((JSONObject)msg.obj);
                        break;
                }
            }
        };
    }

    /**
     * 登录需要执行的后台操作（网络登录）
     * @param accout    账号
     * @param pwd       密码
     */
    @Override
    public void doLogin(String accout, String pwd) {
        setProgressbarVisible(View.VISIBLE);
        LoginRunnable loginRunnable = new LoginRunnable(accout,pwd);
        new Thread(loginRunnable).start();//开启线程，发送网络请求
    }

    /**
     * 子线程中发送网络请求
     */
    class LoginRunnable implements Runnable {
        String account,pwd;
        public LoginRunnable(String account, String pwd) {
            this.account = account;
            this.pwd = pwd;
        }

        @Override
        public void run() {
            JSONObject jsonParams = new JSONObject();
            JSONObject items = new JSONObject();
            try {
                items.put("account", account);
                items.put("password", MD5Utils.dencryption(pwd));
                items.put("provinceCode", "SC");
                jsonParams.put("items", items);
                Object result = new HttpUtil().invoke(jsonParams.toString(), "eamAppOrgUserService_login.app");
                if (result == null || result instanceof Exception) {
                    handler.sendEmptyMessage(-1);
                } else {
                    JSONObject jsonResult = new JSONObject(result.toString());
                    int core = jsonResult.optInt("core",-2);
                    Message msg = handler.obtainMessage(core);
                    if (core == 1) { //登录成功
                        //将登录返回结果连同登录账号缓存至程序变量中
                        JSONObject jsonUser = jsonResult.getJSONArray("items").getJSONObject(0);
                        msg.obj = jsonUser;
                        handler.sendMessage(msg);
                    } else if (core == 0){
                        msg.obj = jsonResult.optString("msg");
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(core);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(-2);
            }
        }
    }

    /**
     * 设置进度条是否可见，调用View层的方法在UI显示
     * @param visible
     */
    @Override
    public void setProgressbarVisible(int visible) {
        if (loginView != null)
            loginView.setProgressBarVisibile(visible);
    }

    @Override
    public void toastMsg(String msg) {
        if (loginView != null)
            loginView.toastMsg(msg);
    }

    @Override
    public void doLoginResult(String errorMsg,int errorFlag) {
        if(loginView != null)
            loginView.doLonginResult(errorMsg, errorFlag);
    }

    @Override
    public void gotoUserInfoActivity(JSONObject user) {
        if(loginView != null)
            loginView.gotoUserInfoActivity(user);
    }

    @Override
    public void onDestory() {
        if(loginView != null)
            loginView = null;
    }
}
