package com.maomovie.activity.mainfragment;

/**
 * Created by yanpeng on 2016/12/16.
 * 用于规划MovieFragment的界面操作方法
 */
public interface MovieFragView {
    //设置进度条是否可见
    void setProgressBarVisibile(int visibile);
    //通过数据库查询电影后需要显示的操作
    void queryMovieDatabaseResult(int code,Object result);
    //通过服务器查询电影后需要执行的操作
    void queryMovieInternetResult(int code,Object result);
    //查询电影总条数后需要执行的操作
    void queryMovieCountResult(int code,int total);

}
