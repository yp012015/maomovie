package com.maomovie.entity;

import java.io.Serializable;
import java.util.List;

/**
 *	支持城市实体
 */
public class SupportCityEntity implements Serializable{

	/**
	 * id : 1
	 * nm : 北京
	 * py : beijing
	 * areas : [{"id":14,"nm":"朝阳区"},{"id":17,"nm":"海淀区"}]
	 */

	private int id;
	private String nm;
	private String py;
	/**
	 * id : 14
	 * nm : 朝阳区
	 */

	private List<AreasBean> areas;


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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public List<AreasBean> getAreas() {
		return areas;
	}

	public void setAreas(List<AreasBean> areas) {
		this.areas = areas;
	}

	public static class AreasBean implements Serializable{
		private int id;
		private String nm;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getNm() {
			return nm;
		}

		public void setNm(String nm) {
			this.nm = nm;
		}
	}
}
