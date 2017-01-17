package com.maomovie.presenter.movieFragment;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import com.maomovie.activity.mainfragment.MovieFragView;
import com.maomovie.entity.TodayMovieEntity;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.sqlite.service.TodayMovieService;
import com.maomovie.util.HttpUtil;

import java.util.List;

/**
 * Created by yanpeng on 2016/12/16.
 * 用于实现MovieFragment的具体业务
 */
public class MovieFragBizPresenter implements MovieFragBizInterface{
    private MovieFragView movieView;
    private ThreadHelper threadHelper;
    private TodayMovieService movieService;

    public MovieFragBizPresenter(MovieFragView movieView) {
        this.movieView = movieView;
        threadHelper = new ThreadHelper(new Handler());
        movieService = new TodayMovieService();
    }

    /**
     * 从数据库查询电影数量
     * @param context
     */
    @Override
    public void queryCountFromDatabase(final Context context) {
        movieView.setProgressBarVisibile(View.VISIBLE);
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return movieService.queryCount(context);
            }

            @Override
            public void result(Object result) {
                if (result == null || result instanceof Exception) {
                    movieView.queryMovieCountResult(400,0);
                } else {
                    int total = (int) result;
                    movieView.queryMovieCountResult(200,total);
                }
            }
        });
    }

    /**
     * 从数据分页查询电影信息
     * @param context
     * @param pageIndex 下标
     */
    @Override
    public void queryDataFromDatabase(final Context context,final int pageIndex) {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return movieService.queryByPage(context, pageIndex);
            }

            @Override
            public void result(Object result) {
                //如果从数据库获取数据为空或者查询失败，从网络上获取数据
                if (result == null || result instanceof Exception) {
                    movieView.queryMovieDatabaseResult(400,result);
                } else {//如果从数据库查询数据成功，解析数据
                    movieView.queryMovieDatabaseResult(200,result);
                }
            }
        });
    }

    /**
     * 将电影信息保存到数据库
     * @param list
     */
    @Override
    public void saveToDatabase(final Context context,final List<TodayMovieEntity> list) {
        threadHelper.dataHander(new Runnable() {
            @Override
            public void run() {
                movieService.createOrUpdateData(list, context);
            }
        });
    }

    /**
     * 从服务获取电影信息
     * @param pageIndex 下标
     */
    @Override
    public void getDataFromInternet(final int pageIndex) {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return HttpUtil.getHotMovie(pageIndex - 1);
            }

            @Override
            public void result(Object result) {
                if (result == null || result instanceof Exception) {
                    movieView.queryMovieInternetResult(400,result);
                } else {
                    movieView.queryMovieInternetResult(200,result);
                }
            }
        });
    }

    /**
     * 上拉加载更多时回调的方法
     * @param context
     * @param pageIndex
     */
    @Override
    public void loadMoreFromDatabase(final Context context,final int pageIndex) {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return movieService.queryByPage(context,pageIndex);
            }

            @Override
            public void result(Object result) {
                if(result == null || result instanceof Exception){
                    movieView.queryMovieDatabaseResult(401,result);
                } else {
                    movieView.queryMovieDatabaseResult(201,result);
                }
            }
        });
    }

    /**
     * Acvity销毁时回调的方法
     */
    @Override
    public void onDestory() {
        if(movieView != null){
            movieView = null;
        }
    }

}
