package com.maomovie.sqlite.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.MovieEntity;
import com.maomovie.sqlite.SqliteHelper;
import com.maomovie.sqlite.dao.MovieItemDao;

import java.util.List;

/**
 * Created by YanP on 2016/8/17.
 * 业务功能：对电影详情数据表MovieItem进行增删改查操作
 */
public class MoiveItemService {
    private MovieItemDao dao = new MovieItemDao();

    public boolean createOrUpdate(List<MovieEntity> list,Context context){
        boolean flag = false;//标记数据库事务操作是否成功
        SQLiteDatabase db = SqliteHelper.getInstance(context).getWritableDatabase();
        //1.开启事务
        db.beginTransaction();
        try {
            //2.遍历数据，依次进行添加或覆盖
            for(MovieEntity entity : list){
                dao.deleteById(entity.getMovieid(),db);
                dao.add(entity,db);
            }
            flag = true;
            //2.1设置事物操作成功
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        //3关闭事物
        db.endTransaction();
        db.close();
        return flag;
    }

    /**
     * 根据电影id 查询电影详情
     * @param movieId   电影id
     * @param context
     * @return
     */
    public Object queryById(String movieId,Context context){
        return dao.queryById(movieId,context);
    }

    /**
     * 查询所有的电影详情
     * @param context
     * @return
     */
    public Object queryAll(Context context){
        return dao.queryAll(context);
    }

    public void deleteById(String movieId,Context context){
        SQLiteDatabase db = SqliteHelper.getInstance(context).getWritableDatabase();
        dao.deleteById(movieId,db);
    }
}
