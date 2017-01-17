package com.maomovie.presenter.movieFragment;

import android.content.Context;
import com.maomovie.entity.TodayMovieEntity;
import java.util.List;

/**
 * Created by yanpeng on 2016/12/16.
 * 用于规划MovieFragment的业务需求方法
 */
public interface MovieFragBizInterface {
    //从数据库查询电影总数
    void queryCountFromDatabase(Context context);
    //从数据库分页查询电影信息
    void queryDataFromDatabase(Context context,int pageIndex);
    //保存电影信息到数据库
    void saveToDatabase(Context context,List<TodayMovieEntity> list);
    //从服务器分页查询电影信息
    void getDataFromInternet(int pageIndex);
    //上拉加载更多时回调的方法
    void loadMoreFromDatabase(Context context,int pageIndex);
    //页面销毁时需要回调的方法
    void onDestory();
}
