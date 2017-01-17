package com.maomovie.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

	/**
	 * 功能：将对象转为json格式字符串
	 * @param src
	 * @return
	 */
	public static String toJsonString(Object src) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();  
		return gson.toJson(src);
	}
	
	/**
	 * 功能：将json字符串转为对象
	 * @param jsonString
	 * @return
	 */
	public static Object jsonToObject(String jsonString,Class clazz) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(jsonString, clazz);
	}
	
	/**
	 * 将Json格式字符转为List对象
	 * @param jsonString
	 * @param type  new TypeToken<List<Object>>(){}.getType();
	 * @return
	 */
	public static Object jsonToList(String jsonString,Type type) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		return gson.fromJson(jsonString, type);
	}
}

