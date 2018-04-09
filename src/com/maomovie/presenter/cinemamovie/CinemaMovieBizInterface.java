package com.maomovie.presenter.cinemamovie;

import com.maomovie.entity.CurrentMoviesEntity;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yanpeng on 2018/4/3.
 * 业务功能：定义影院的电影信息界的业务功能
 */
public interface CinemaMovieBizInterface {
    void loadMoviesOfCinema(int cinemaId,int movieId);//加载影院的上映电影
    void loadResult(JSONObject jsonObject);//电影信息加载完后的操作处理
    //显示影院上映的电影信息
    void showMoviesOfCinema(List<CurrentMoviesEntity.CurrentMovieBean> movieBeans);
    //显示电影场次信息
    void displayDateShow(JSONObject jsonObject);
}
