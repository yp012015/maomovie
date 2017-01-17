package com.maomovie.sqlite.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.maomovie.sqlite.SqliteHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseDao {

	/**
	 * 功能：查询数据，返回相应的List对象
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param fields 需要绑定的字段（传入的字段的值必须和类名的字段一致）
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> List<T> query(Class<T> clazz,String sql,String[] fields,Context context) throws Exception{
		SqliteHelper helper = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			helper = SqliteHelper.getInstance(context);
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql,null);
			List<T> results = (List<T>) bindInstances(clazz, fields, cursor);
			return results;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			cursor.close();
			db.close();
			helper.close();
		}
	}

	/**
	 * 功能：查询数据，返回相应的List对象
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param fields 需要绑定的字段（传入的字段的值必须和类名的字段一致）
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> List<T> queryUseDB(Class<T> clazz,String sql,String[] fields,Context context,SQLiteDatabase db) throws Exception{
		Cursor cursor;
		try{
			cursor = db.rawQuery(sql,null);
			List<T> results = (List<T>) bindInstances(clazz, fields, cursor);
			return results;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getCount(Context context,String sql) {
		SqliteHelper helper = SqliteHelper.getInstance(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		helper.close();
		return count;
	}
	
	/**
	 * 第一个方法 两次调用时间较近时 会出bug 下面这个是 初级的替代品
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param fields
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> List<T> queryData(Class<T> clazz,String sql,String[] fields,Context context) throws Exception{
		//SqliteHelper helper = SqliteHelper.getInstance(context);
		SqliteHelper helper = new SqliteHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql,null);
		List<T> results = (List<T>) bindInstances(clazz, fields, cursor);
		cursor.close();
		db.close();
		helper.close();
		return results;
	}
	
	public int getDataCount(Context context,String sql) {
		SqliteHelper helper = SqliteHelper.getInstance(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		helper.close();
		return count;
	}
	
	/**
	 * 功能：查询数据，返回List<JsonObject>对象
	 * @param clazz
	 * @param sql
	 * @param fields 需要对JsonObject绑定的字段（传入的字段的值必须和sql语句As后面的一致）
	 * @param context
	 * @return
	 * @throws Exception 
	 */
	public synchronized JSONArray queryReturnJsonList(Class<JSONObject> clazz,String sql,String[] fields,Class[] types,Context context) throws Exception {
		SqliteHelper helper = SqliteHelper.getInstance(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql,null);
		JSONArray results = (JSONArray) bindInstancesForJson(clazz, fields,types, cursor);
		cursor.close();
		db.close();
		helper.close();
		return results;
	}
	
	public synchronized boolean executeUpdateWithTransaction(Context context,String sql) {
		boolean flag = false;
		SQLiteDatabase db = SqliteHelper.getInstance(context).getReadableDatabase();
		db.beginTransaction();
		try {
			db.execSQL(sql);
			db.setTransactionSuccessful();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		db.endTransaction();
		return flag;
	}
	
	public synchronized boolean executeUpdate(Context context,String sql) {
		SQLiteDatabase db = SqliteHelper.getInstance(context).getReadableDatabase();
		try {
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
	/**
	 * 功能：绑定字段S
	 * @param clazz
	 * @param fields
	 * @param cursor
	 * @return
	 * @throws Exception 
	 */
	protected Object bindInstances(Class<?> clazz,String[] fields,Cursor cursor) throws Exception {
		List<Object> results = new LinkedList<Object>();
		while (cursor.moveToNext()) {
			results.add(bindInstance(clazz, fields, cursor));
		}
		return results;
	}
	
	/**
	 * 功能：给Json对象绑定值
	 * @param clazz
	 * @param fields
	 * @param cursor
	 * @return
	 * @throws Exception 
	 */
	protected Object bindInstancesForJson(Class<?> clazz,String[] fields,Class[] types,Cursor cursor) throws Exception {
		JSONArray jsonArray = new JSONArray();
		while (cursor.moveToNext()) {
			jsonArray.put(bindInstanceForJson(clazz, fields,types, cursor));
		}
		return jsonArray;
	}
	
	/**
	 * 绑定字段
	 * @param clazz
	 * @param fields
	 * @param cursor
	 * @return
	 * @throws Exception
	 */
	protected Object bindInstance(Class<?> clazz, String[] fields,
			Cursor cursor) throws Exception {
		Object instance = clazz.newInstance();
		for (String field : fields) {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, getValue(cursor, field, f.getType()));
		}
		return instance;
	}
	/**
	 * 绑定字段
	 * @param clazz
	 * @param fields
	 * @param cursor
	 * @return
	 * @throws Exception
	 */
	protected Object bindInstanceForJson(Class<?> clazz, String[] fields,Class[]types,
			Cursor cursor) throws Exception {
		JSONObject instance = (JSONObject) clazz.newInstance();
		for (int i = 0; i < fields.length;i++) {
			instance.put(fields[i], getValue(cursor, fields[i], types[i]));
		}
		return instance;
	}
	
	/**
	 * 获取值
	 * @param cursor
	 * @param columnName
	 * @param type
	 * @return
	 */
	private Object getValue(Cursor cursor, String columnName, Class<?> type) {
		if (type == Integer.class || type.getName().equals("int")) {
			return cursor.getInt(cursor.getColumnIndex(columnName));
		} else if (type == Long.class || type.getName().equals("long")) {
			return cursor.getLong(cursor.getColumnIndex(columnName));
		} else if (type == Short.class || type.getName().equals("short")) {
			return cursor.getShort(cursor.getColumnIndex(columnName));
		} else if (type == Float.class || type.getName().equals("falot")) {
			return cursor.getFloat(cursor.getColumnIndex(columnName));
		} else if (type == Double.class || type.getName().equals("double")) {
			return cursor.getDouble(cursor.getColumnIndex(columnName));
		} else if (type == String.class) {
			String ss = cursor.getString(cursor.getColumnIndex(columnName));
			if(ss == null || ss.equals("null"))
				return null;
			return ss;
		} else if(type == Boolean.class || type.getName().equals("boolean")){
			int b = cursor.getInt(cursor.getColumnIndex(columnName));
			return b != 0;
		} else if(type == Date.class){
			try {
				String dateStr = cursor.getString(cursor.getColumnIndex(columnName));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.parse(dateStr);
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return null;
			}
		} else if(type == BigInteger.class || type.getName().equals("biginteger")) {
			String val = cursor.getString(cursor.getColumnIndex(columnName));
			return new BigInteger(val);
		} else if(type == BigDecimal.class || type.getName().equals("bigdecimal")) {
			String val = cursor.getString(cursor.getColumnIndex(columnName));
			return new BigDecimal(val);
		}
		return null;
	}
}
