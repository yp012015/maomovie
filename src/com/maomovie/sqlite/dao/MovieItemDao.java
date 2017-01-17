package com.maomovie.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.MovieEntity;

/**
 * Created by YanP on 2016/8/17.
 * 业务功能：查询电影的详细信息
 */
public class MovieItemDao extends BaseDao{
    //需要查询的字段
    private String[] fields = new String[]{"movieid","actors","also_known_as","country","directors",
            "film_locations","genres","language","plot_simple","poster","rated","rating","rating_count",
            "release_date","runtime","title","type","writers","year"};

    /**
     * 添加一条数据
     * @param entity
     */
    public void add(MovieEntity entity,SQLiteDatabase db){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO MovieItem (movieid,actors,also_known_as,country,directors," +
                ",film_locations,genres,language,plot_simple,poster,rated,rating,rating_count," +
                ",release_date,runtime,title,type,writers,year) values (");
        sql.append("'" + entity.getMovieid() + "',");           //string	影片唯一标识ID
        sql.append("'" + entity.getActors() + "',");            //string	影片的演员列表。
        sql.append("'" + entity.getAlso_known_as() + "',");     //string	影片的其它名称。
        sql.append("'" + entity.getCountry() + "',");           //string	影片的拍摄国家。
        sql.append("'" + entity.getDirectors() + "',");         //string	影片的导演列表。
        sql.append("'" + entity.getFilm_locations() + "',");    //string	影片的拍摄地。
        sql.append("'" + entity.getGenres() + "',");            //string	影片的分类。（如：戏剧，战争）
        sql.append("'" + entity.getLanguage() + "',");          //string	影片的对白使用的语言。
        sql.append("'" + entity.getPlot_simple() + "',");       //String	影片的剧情概要。
        sql.append("'" + entity.getPoster() + "',");            //String	影片的海报。
        sql.append("'" + entity.getRated() + "',");             //String	影片的分类与评级。
        sql.append("'" + entity.getRating() + "',");            //string	影片的得分。
        sql.append("'" + entity.getRating_count() + "',");      //string	影片的评分人数。
        sql.append("" + entity.getRelease_date() + ",");        //Int		影片的上映时间。
        sql.append("'" + entity.getRuntime() + "',");           //string	影片的持续时间。
        sql.append("'" + entity.getTitle() + "',");             //string	影片的名称。
        sql.append("'" + entity.getType() + "',");              //string	影片类型.
        sql.append("'" + entity.getWriters() + "',");           //string	影片的编剧列表。
        sql.append(entity.getYear());                           //Int		影片的拍摄年代。
        sql.append(")");
        db.equals(sql.toString());
    }

    /**
     * 查询所有的电影详情
     * @param context
     * @return
     */
    public Object queryAll(Context context){
        String sql = "SELECT * FROM MovieItem";
        try {
            return super.query(MovieEntity.class,sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 根据电影id查询电影详情
     * @param movieId   电影id
     * @param context
     * @return
     */
    public Object queryById(String movieId,Context context){
        String sql = "SELECT * FROM MovieItem WHERE movieid = '" + movieId + "'";
        try {
            return super.query(MovieEntity.class,sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 根据电影id删除电影详情
     * @param movieId
     * @param db
     */
    public void deleteById(String movieId, SQLiteDatabase db){
        String sql = "DELETE FROM MovieItem WHERE movieid = '" + movieId + "'";
        db.execSQL(sql);
    }
}
