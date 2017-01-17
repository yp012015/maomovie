package com.maomovie.entity;
/**
 * 周边影院的详细信息
 */
public class AroundCinemaEntity {
	private String id;				//string	电影院ID
	private String idcityName;		//string	影院所属城市
	private String idcinemaName;	//string	影院名称
	private String idaddress;		//string	影院地址
	private String idtelephone;		//string	联系电话
	private double idlatitude;		//double	纬度，适合百度地图
	private double idlongitude;		//double	经度，适合百度地图
	private String idtrafficRoutes;	//string	交通路线
	private String iddistance;		//string	大概距离(米)

	/**
	 * 返回创建数据表的sql语句
	 * @return
	 */
	public static String getCreateTableSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ID NOT EXISTS [AroundCinema] (");
		sql.append("[_ID] integer PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sql.append("[id] varchar(60),");
		sql.append("[idcityName] varchar(60),");
		sql.append("[idcinemaName] varchar(60),");
		sql.append("[idaddress] varchar(60),");
		sql.append("[idtelephone] varchar(60),");
		sql.append("[idlatitude] varchar(60),");
		sql.append("[idtrafficRoutes] txt,");
		sql.append("[iddistance] varchar(60)");
		sql.append(");");
		return sql.toString();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdcityName() {
		return idcityName;
	}
	public void setIdcityName(String idcityName) {
		this.idcityName = idcityName;
	}
	public String getIdcinemaName() {
		return idcinemaName;
	}
	public void setIdcinemaName(String idcinemaName) {
		this.idcinemaName = idcinemaName;
	}
	public String getIdaddress() {
		return idaddress;
	}
	public void setIdaddress(String idaddress) {
		this.idaddress = idaddress;
	}
	public String getIdtelephone() {
		return idtelephone;
	}
	public void setIdtelephone(String idtelephone) {
		this.idtelephone = idtelephone;
	}
	public double getIdlatitude() {
		return idlatitude;
	}
	public void setIdlatitude(double idlatitude) {
		this.idlatitude = idlatitude;
	}
	public double getIdlongitude() {
		return idlongitude;
	}
	public void setIdlongitude(double idlongitude) {
		this.idlongitude = idlongitude;
	}
	public String getIdtrafficRoutes() {
		return idtrafficRoutes;
	}
	public void setIdtrafficRoutes(String idtrafficRoutes) {
		this.idtrafficRoutes = idtrafficRoutes;
	}
	public String getIddistance() {
		return iddistance;
	}
	public void setIddistance(String iddistance) {
		this.iddistance = iddistance;
	}
}
