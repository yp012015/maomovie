package com.maomovie.sqlite.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.components.listsort.SideBar;
import com.maomovie.entity.TodayMovieEntity;
import com.maomovie.sqlite.SqliteHelper;
import com.maomovie.sqlite.dao.TodayMovieDao;

import java.util.List;

/**
 * Created by YanP on 2016/8/15.
 */
public class TodayMovieService {
    private TodayMovieDao dao = new TodayMovieDao();

    /**
     * 批量添加或更新数据
     * @param list      数据源
     * @param context   上下文对象
     * @return
     */
    public boolean createOrUpdateData(List<TodayMovieEntity> list,Context context){
        boolean flag = false;
        SQLiteDatabase db = SqliteHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();//开启事务
        try{
            for(TodayMovieEntity entity : list){
                dao.deleteById(entity.getId(),db);
                dao.add(entity, db);
            }
            db.setTransactionSuccessful();//设置事物成功
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        db.endTransaction();//结束事物
        db.close();
        return flag;
    }

    /**
     * 根据电影id查询
     * @param id
     * @param context
     * @return
     */
    public Object queryById(int id, Context context){
        return dao.queryById(id, context);
    }

    /**
     * 查询所有数据
     * @param context
     * @return
     */
    public Object queryAll(Context context){
        return dao.queryAll(context);
    }

    /**
     * 分页查询电影数据
     * @param context
     * @param pageIndex 当前页数
     * @return
     */
    public Object queryByPage(Context context,int pageIndex){
        return dao.queryByPage(context, pageIndex);
    }

    public int queryCount(Context context){
        return dao.queryCount(context);
    }

}
