package com.maomovie.presenter.cinemamovie;

import com.maomovie.entity.CurrentMoviesEntity;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yanpeng on 2018/4/3.
 * 业务功能：规划影院上映电影信息的界面功能
 */
public interface CinemaMovieViewInterface {
    void tvMsg( String msg);//TextView显示消息
    //显示影院上映的电影信息
    void showMoviesOfCinema(List<CurrentMoviesEntity.CurrentMovieBean> movieBeans);
    //显示电影场次信息
    void displayDateShow(JSONObject jsonObject);
}
