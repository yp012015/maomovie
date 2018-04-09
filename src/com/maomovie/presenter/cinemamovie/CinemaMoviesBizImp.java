package com.maomovie.presenter.cinemamovie;

import android.os.Handler;
import com.google.gson.reflect.TypeToken;
import com.maomovie.entity.CurrentMoviesEntity;
import com.maomovie.service.ThreadHandler;
import com.maomovie.service.ThreadHelper;
import com.maomovie.util.GsonUtils;
import com.maomovie.util.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yanpeng on 2018/4/3.
 */
public class CinemaMoviesBizImp implements CinemaMovieBizInterface {
    private CinemaMovieViewInterface view;
    private ThreadHelper threadHelper = new ThreadHelper(new Handler());

    public CinemaMoviesBizImp(CinemaMovieViewInterface view) {
        this.view = view;
    }

    @Override
    public void loadMoviesOfCinema(int cinemaId, int movieId) {
        threadHelper.dataHander(new ThreadHandler() {
            @Override
            public Object run() {
                return HttpUtil.getPlayingMoviesByCinemaId(cinemaId,movieId);
            }

            @Override
            public void result(Object result) throws JSONException {
                if (null == result) {
                    view.tvMsg("数据为空");
                    return;
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());
                    if (jsonObject.optInt("status") != 0) {
                        view.tvMsg("获取数据异常");
                        return;
                    }
                    jsonObject = jsonObject.optJSONObject("data");
                    loadResult(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadResult(JSONObject jsonObject) {
        //获取影院上映的电影信息
        String moviesInfo = jsonObject.optString("movies");
        //将电影信息转成List集合
        List<CurrentMoviesEntity.CurrentMovieBean> movieObjList = (List<CurrentMoviesEntity.CurrentMovieBean>)
                GsonUtils.jsonToList(moviesInfo, new TypeToken<List<CurrentMoviesEntity.CurrentMovieBean>>() {
                }.getType());
        showMoviesOfCinema(movieObjList);
        //获取电影的场次信息
        String dateInfo = jsonObject.optString("DateShow");
        try {
            JSONObject dateObj = new JSONObject(dateInfo);
           /* Iterator iterator = dateObj.keys();
            StringBuffer value = new StringBuffer();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                value.append(key + ":   " + dateObj.optString(key) + "\n");
            }
            view.tvMsg(value.toString());*/
            showMoviesOfCinema(movieObjList);
            displayDateShow(dateObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMoviesOfCinema(List<CurrentMoviesEntity.CurrentMovieBean> movieBeans) {
        view.showMoviesOfCinema(movieBeans);
    }

    @Override
    public void displayDateShow(JSONObject jsonObject) {
        view.displayDateShow(jsonObject);
    }
}
