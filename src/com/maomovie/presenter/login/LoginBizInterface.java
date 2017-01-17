package com.maomovie.presenter.login;

import org.json.JSONObject;

/**
 * Created by yanpeng on 2016/11/7.
 * 业务功能：此接口用于规划登录需要执行的业务操作，具体功能需要导出类LoginPresenter来实现
 */
public interface LoginBizInterface {
    void doLogin(String accout,String pwd);//网络登录
    void setProgressbarVisible(int visible);//设置进度条是否可见
    void toastMsg(String msg);//Toast弹窗显示消息
    void doLoginResult(String errorMsg,int errorFlag);//登录成功之后执行的方法
    void onDestory();//Activity生命周期中执行onDestory()方法时，调用该方法
    void gotoUserInfoActivity(JSONObject userJson);
}
