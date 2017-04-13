package com.maomovie.entity;

import java.io.Serializable;

/**
 *	支持城市实体
 */
public class SupportCityEntity implements Serializable{


	/**
	 * onlineTime : 1272902400
	 * rank : S
	 * pinyin : shanghai
	 * name : 上海
	 * id : 10
	 * open : true
	 */

	private int onlineTime;
	private String rank;
	private String pinyin;
	private String name;
	private int id;
	private boolean open;

	/**
	 * 获取创建数据表的sql语句
	 * @return
	 */
	public static String getCreateTableSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS [City] (");
		sql.append("[No] integer PRIMARY KEY AUTOINCREMENT NOT NULL ,");
		sql.append("[id] integer ,");
		sql.append("[nm] varchar(60) ,");
		sql.append("[py] varchar(60) ,");
		sql.append("[areas] text ");
		sql.append(")");
		return sql.toString();
	}

	public int getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
