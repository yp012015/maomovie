package com.maomovie.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.TodayMovieEntity;

/**
 * Created by YanP on 2016/8/15.
 */
public class TodayMovieDao extends BaseDao {
    //需要查询的字段
    private String[] fields = new String[] {"id","cnms","dur","pn","preSale","sc","sn","snum","wish","cat","dir",
            "imax","img","late","nm","rt","scm","showInfo","src","star","showDate","time","value3d","vd","ver"};

    /**
     * 添加一条数据
     * @param entity    数据实体
     * @param db
     */
    public void add (TodayMovieEntity entity,SQLiteDatabase db){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TodayMovie (id,cnms,dur,pn,preSale,sc,sn,snum,wish,cat,dir,imax,img,late," +
                "nm,rt,scm,showInfo,src,star,showDate,time,value3d,vd,ver) values (");
        sql.append(entity.getId() + ",");
        sql.append(entity.getCnms()+ ",");
        sql.append(entity.getDur()+ ",");
        sql.append(entity.getPn()+ ",");
        sql.append(entity.getPreSale()+ ",");
        sql.append(entity.getSc()+ ",");
        sql.append(entity.getSn()+ ",");
        sql.append(entity.getSnum()+ ",");
        sql.append(entity.getWish()+ ",");
        sql.append("'" + entity.getCat() + "',");
        sql.append("'" + entity.getDir() + "',");
        sql.append("'" + entity.isImax() + "',");
        sql.append("'" + entity.getImg() + "',");
        sql.append("'" + entity.isLate() + "',");
        sql.append("'" + entity.getNm() + "',");
        sql.append("'" + entity.getRt() + "',");
        sql.append("'" + entity.getScm() + "',");
        sql.append("'" + entity.getShowInfo() + "',");
        sql.append("'" + entity.getSrc() + "',");
        sql.append("'" + entity.getStar() + "',");
        sql.append("'" + entity.getShowDate() + "',");
        sql.append("'" + entity.getTime() + "',");
        sql.append("'" + entity.isValue3d() + "',");
        sql.append("'" + entity.getVd() + "',");
        sql.append("'" + entity.getVer() + "'");
        sql.append(")");
        db.execSQL(sql.toString());
    }

    /**
     * 根据电影Id删除数据
     * @param id    电影id
     * @param db
     */
    public void deleteById(int id, SQLiteDatabase db){
        String sql = "DELETE FROM TodayMovie WHERE id = " + id;
        db.execSQL(sql);
    }

    /**
     * 根据电影id查询数据
     * @param id        电影id
     * @param context   上下文对象
     * @return
     */
    public Object queryById(int id,Context context){
        String sql = "SELECT * FROM TodayMovie WHERE id = " + id;
        try {
            return super.query(TodayMovieEntity.class,sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 查询所有的电影数据
     * @param context
     * @return
     */
    public Object queryAll(Context context){
        String sql = "SELECT * FROM TodayMovie";
        try {
            return super.query(TodayMovieEntity.class,sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 分页查询电影信息
     * @param context
     * @param pageIndex
     * @return
     */
    public Object queryByPage(Context context,int pageIndex){
        int firstIndex = (pageIndex - 1) * 20;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM TodayMovie ");
        sql.append(" LIMIT " + firstIndex + "," + 20);
        try {
            Object res = super.query(TodayMovieEntity.class, sql.toString(), fields, context);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }


    public int queryCount(Context context){
        String sql = "SELECT count(*) FROM TodayMovie";
        return super.getCount(context,sql);
    }

}
