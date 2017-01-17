package com.maomovie.sqlite.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.sqlite.SqliteHelper;
import com.maomovie.sqlite.dao.CinemaDao;

import java.util.List;

/**
 * Created by YanP on 2016/8/15.
 */
public class CinemaService {
	private CinemaDao dao = new CinemaDao();

	public boolean add(List<CinemaEntity> list, Context context) {
		boolean flag = false;
		// 获取数据库对象
		SQLiteDatabase db = SqliteHelper.getInstance(context).getReadableDatabase();
		// 开启事务
		db.beginTransaction();
		try {
			// 对list进行遍历，1.根据id删除之前的数据，2.插入新数据
			for (CinemaEntity entity : list) {
				dao.deleteById(entity.getId(), db);
				dao.add(entity, db);
			}
			// 设置事务成功
			db.setTransactionSuccessful();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		// 事务结束 提交
		db.endTransaction();
		db.close();
		return flag;
	}

	/**
	 * 查询全部影院
	 * @param context
	 * @return
	 */
	public Object queryAll(Context context){
		return dao.queryAll(context);
	}

	/**
	 * 分页查询影院信息
	 * @param context
	 * @param pageIndex	查询第几页
	 * @param pageSize	每页查询的条数
	 * @return
	 */
	public Object queryByPage(Context context,int pageIndex,int pageSize){
		return dao.queryByPage(context, pageIndex, pageSize);
	}

	/**
	 * 查询总条数
	 * @param context
	 * @return
	 */
	public int queryCount(Context context){
		return dao.queryCount(context);
	}
}
