package com.maomovie.entity;
/**
 *	影片放映的具体信息
 */
public class MoviePlayingInfoEntity {

	/**
	 * cinemaName : 金逸影城上海龙之梦IMAX店
	 * cinemaId : 1
	 * address : 上海市虹口区西江湾路388号凯德龙之梦B座6F-7F
	 * latitude : 31.27125
	 * longitude : 121.4787
	 */

	private String cinemaName;
	private String cinemaId;
	private String address;
	private String latitude;
	private String longitude;

	/**
	 * 获取创建数据表的sql语句
	 * @return
	 */
	public static String getCreateTableSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS [MoviePlaying](");
		sql.append("[No] integer PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sql.append("[cinemaId] varchar(60) ,");
		sql.append("[cinemaName] varchar(60) ,");
		sql.append("[address] varchar(60) ,");
		sql.append("[latitude] varchar(60) ,");
		sql.append("[longitude] varchar(60) ");
		sql.append(")");
		return sql.toString();
	}

	public String getCinemaName() {
		return cinemaName;
	}

	public void setCinemaName(String cinemaName) {
		this.cinemaName = cinemaName;
	}

	public String getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
