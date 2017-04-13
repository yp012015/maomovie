package com.maomovie.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.util.GsonUtils;

/**
 * Created by YanP on 2016/8/15.
 */
public class CinemaDao extends BaseDao{
    //需要查询的字段
    private String[] fields = new String[]{"addr","area","areaId","brd","bus",
            "ct","ctid","deal","dealPrice","dis","dri","feature","id","imax",
            "img","lat","lng","nm","note","park","referencePrice","sell","sellPrice","suw","tel"};

    /**
     * 向数据表中添加一条数据
     * @param cinemaEntity
     * @param db
     */
    public void add (CinemaEntity cinemaEntity,SQLiteDatabase db){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO Cinema (addr,area,areaId,brd,bus,ct,ctid,deal,dealPrice,dis,dri,feature,id," +
                "imax,img,lat,lng,nm,note,park,referencePrice,sell,sellPrice,suw,tel,deals) values (");
        sql.append("'" + cinemaEntity.getAddr() + "' ,");
        sql.append("'" + cinemaEntity.getArea() + "' ,");
        sql.append(cinemaEntity.getAreaId() + " ,");
        sql.append("'" + cinemaEntity.getBrd() + "' ,");
        sql.append("'" + cinemaEntity.getBus() + "' ,");
        sql.append("'" + cinemaEntity.getCt() + "' ,");
        sql.append(cinemaEntity.getCtid() + " ,");
        sql.append(cinemaEntity.getDeal() + ",");
        sql.append(cinemaEntity.getDealPrice() + ",");
        sql.append("'" + cinemaEntity.getDis() + "',");
        sql.append("'" + cinemaEntity.getDri() + "',");
        sql.append("'" + cinemaEntity.getFeature() + "',");
        sql.append(cinemaEntity.getId() + ",");
        sql.append(cinemaEntity.getImax() + ",");
        sql.append("'" + cinemaEntity.getImg() + "',");
        sql.append(cinemaEntity.getLat() + ",");
        sql.append(cinemaEntity.getLng() + ",");
        sql.append("'" + cinemaEntity.getNm() + "',");
        sql.append("'" + cinemaEntity.getNote() + "',");
        sql.append("'" + cinemaEntity.getPark() + "',");
        sql.append(cinemaEntity.getReferencePrice() + ",");
        sql.append("'" + cinemaEntity.getSell() + "',");
        sql.append(cinemaEntity.getSellPrice() + ",");
        sql.append("'" + cinemaEntity.getSuw() + "',");
        sql.append("'" + cinemaEntity.getTel() + "',");
        sql.append("'" + GsonUtils.toJsonString(cinemaEntity.getDeals()) + "'");
        sql.append(")");
        db.execSQL(sql.toString());
    }

    /**
     * 通过电影院id删除数据
     * @param id    电影院id
     * @param db
     */
    public void deleteById(int id, SQLiteDatabase db){
        String sql = "DELETE FROM Cinema WHERE id = " + id;
        db.execSQL(sql);
    }

    /**
     * 根据影院id查询影院信息
     * @param id        影院id
     * @param fields    查询字段
     * @param context   上下文对象
     * @return
     */
    public Object queryById(int id, String[] fields, Context context){
        String sql = "SELECT * FROM Cinema WHERE id = " + id;
        try {
            Object result = super.query(CinemaEntity.class, sql, fields, context);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public Object queryAll(Context context){
        String sql = "SELECT * FROM Cinema";
        try {
            return super.query(CinemaEntity.class,sql,fields,context);
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 分页查询影院信息
     * @param context
     * @param pageIndex
     * @return
     */
    public Object queryByPage(Context context,int pageIndex,int pageSize){
        int firstIndex = (pageIndex - 1) * pageSize;
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM Cinema ");
        sql.append(" LIMIT " + firstIndex + "," + pageSize);
        try {
            Object res = super.query(CinemaEntity.class, sql.toString(), fields, context);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    /**
     * 查询总条数
     * @param context
     * @return
     */
    public int queryCount(Context context){
        String sql = "SELECT count(*) FROM Cinema";
        return super.getCount(context,sql);
    }

}
