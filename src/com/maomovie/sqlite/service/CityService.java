package com.maomovie.sqlite.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.SupportCityEntity;
import com.maomovie.sqlite.SqliteHelper;
import com.maomovie.sqlite.dao.CityDao;

import java.util.List;

/**
 * Created by YanP on 2016/9/7.
 * 业务功能：对城市列表数据表（City）进行增删改查操作
 */
public class CityService {
    private CityDao dao = new CityDao();

    /**
     * 批量添加或更新数据
     * @param cityList
     * @param context
     * @return
     */
    public boolean createOrUpdate(List<SupportCityEntity> cityList,Context context){
        boolean flag = false;//用于标记事务操作是否成功
        SQLiteDatabase db = SqliteHelper.getInstance(context).getReadableDatabase();
        //1.开启事务
        db.beginTransaction();
        try {
            //2.对cityList遍历，向数据库添加数据
            for(SupportCityEntity entity : cityList){
                dao.deleteById(entity.getId(),db);
                dao.add(entity,db);
            }
            //3.设置事务成功
            db.setTransactionSuccessful();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        //4.关闭事物
        finally {
            db.endTransaction();
            db.close();
            return flag;
        }
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
     * 根据城市id查询数据
     * @param id    城市id
     * @param context
     * @return
     */
    public Object queryById(int id ,Context context){
        return dao.queryById(id,context);
    }
}
