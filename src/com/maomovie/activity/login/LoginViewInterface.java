package com.maomovie.activity.login;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yanpeng on 2016/11/7.
 * 业务功能：此接口用于规划登录界面需要执行的UI操作
 *          通过其导出类LoginActivity来实现具体细节
 */
public interface LoginViewInterface {
    /**
     * 登录操作结束后需要在界面上执行的操作
     * @param errorMsg  错误消息
     * @param errorFlag 错误标识
     */
    void doLonginResult(String errorMsg,int errorFlag);

    /**
     * Toast弹窗
     * @param str 弹窗消息
     */
    void toastMsg(String str);

    /**
     * 在UI界面的TextView显示警告信息
     * @param info  警告信息
     */
    void showWarmMsg(String info);

    /**
     * 设置进度条是否可见
     * @param visibile
     */
    void setProgressBarVisibile(int visibile);

    /**
     * 跳转到个人信息界面
     */
    void gotoUserInfoActivity(JSONObject user);
}
