package com.maomovie.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.SupportCityEntity;
import com.maomovie.util.GsonUtils;

/**
 * Created by YanP on 2016/9/7.
 */
public class CityDao extends BaseDao {
    //需要查询的字段
    private String[] fields = new String[]{"id","nm","py","areas"};

    /**
     * 添加一条数据
     * @param cityEntity
     * @param db
     */
    public void add(SupportCityEntity cityEntity,SQLiteDatabase db){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO City (id,nm,py,areas) VALUES (");
        sql.append(cityEntity.getId() + ",");
        sql.append("'" + cityEntity.getName() + "',");
        sql.append("'" + cityEntity.getPinyin() + "',");
        sql.append("'" + cityEntity.getRank() + "'");
        sql.append(")");
        db.execSQL(sql.toString());
    }

    /**
     * 根据id删除数据
     * @param id    城市id
     * @param db
     */
    public void deleteById(int id,SQLiteDatabase db){
        String sql = "DELETE FROM City WHERE id = " + id;
        db.execSQL(sql);
    }

    /**
     * 根据id查询数据
     * @param id    城市id
     * @param context
    */
    public Object queryById(int id,Context context){
        String sql = "SELECT * FROM City WHERE id = " + id;
        try {
            return super.query(SupportCityEntity.class, sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 查询所有的数据
     * @param context
     */
    public Object queryAll(Context context){
        String sql = "SELECT * FROM City";
        try {
            return super.query(SupportCityEntity.class, sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }
}
