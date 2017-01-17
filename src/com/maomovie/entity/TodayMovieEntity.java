package com.maomovie.entity;

import com.google.gson.annotations.SerializedName;

public class TodayMovieEntity {

	/**
	 * late : false
	 * cnms : 0
	 * sn : 0
	 * showInfo : 今天109家影院放映1311场
	 * imax : false
	 * snum : 193210
	 * preSale : 0
	 * vd :
	 * dir : 文伟鸿
	 * star : 张家辉,古天乐,佘诗曼
	 * cat : 警匪,动作,犯罪
	 * wish : 165123
	 * 3d : false
	 * pn : 248
	 * src :
	 * dur : 109
	 * img : http://p0.meituan.net/165.220/movie/646a1aee2e10352f045f359026697ce3293272.jpg
	 * sc : 9
	 * ver : 2D
	 * rt : 2016-08-11上映
	 * nm : 使徒行者
	 * showDate :
	 * scm : 混战入迷局，料谁是卧底
	 * id : 334722
	 * time :
	 */

	private boolean late;		//是否下架
	private int cnms;			//上映影院数目
	private int sn;				//上映场次数
	private String showInfo;	//上映信息
	private boolean imax;		//巨幕电影
	private int snum;			//评分人数
	private int preSale;		//预售标识 1-预售 0-正常
	private String vd;			//预告片
	private String dir;			//导演
	private String star;		//主演
	private String cat;			//分类
	private int wish;			//想看人数
	@SerializedName("3d")
	private boolean value3d;	//是否为3d电影
	private int pn;
	private String src;			//出产地
	private int dur;			//时长(分钟)
	private String img;			//海报
	private int sc;				//影片分数
	private String ver;			//版本(2D/3D)
	private String rt;			//上映日期
	private String nm;			//影片名称
	private String showDate;
	private String scm;			//一句话短评
	private int id;				//影片id
	private String time;		//模糊上映日期

	/**
	 * 获取创建数据表的sql语句
	 * @return
	 */
	public static String getCreateTableSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS [TodayMovie] (");
		sql.append("[No] integer PRIMARY KEY AUTOINCREMENT NOT NULL,");
		sql.append("[id] integer ,");
		sql.append("[cnms] integer,");
		sql.append("[dur] integer,");
		sql.append("[pn] integer,");
		sql.append("[preSale] integer,");
		sql.append("[sc] integer ,");
		sql.append("[sn] integer,");
		sql.append("[snum] integer,");
		sql.append("[wish] integer,");
		sql.append("[cat] varchar(60),");
		sql.append("[dir] varchar(60),");
		sql.append("[imax] varchar(20),");
		sql.append("[img] varchar(60),");
		sql.append("[late] varchar(20),");
		sql.append("[nm] varchar(60),");
		sql.append("[rt] varchar(60),");
		sql.append("[scm] varchar(60),");
		sql.append("[showInfo] varchar(60),");
		sql.append("[src] varchar(60),");
		sql.append("[star] varchar(60),");
		sql.append("[showDate] varchar(60),");
		sql.append("[time] varchar(60),");
		sql.append("[value3d] varchar(20),");
		sql.append("[vd] varchar(60),");
		sql.append("[ver] varchar(60)");
		sql.append(")");
		return sql.toString();
	}


	public boolean isLate() {
		return late;
	}

	public void setLate(boolean late) {
		this.late = late;
	}

	public int getCnms() {
		return cnms;
	}

	public void setCnms(int cnms) {
		this.cnms = cnms;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public String getShowInfo() {
		return showInfo;
	}

	public void setShowInfo(String showInfo) {
		this.showInfo = showInfo;
	}

	public boolean isImax() {
		return imax;
	}

	public void setImax(boolean imax) {
		this.imax = imax;
	}

	public int getSnum() {
		return snum;
	}

	public void setSnum(int snum) {
		this.snum = snum;
	}

	public int getPreSale() {
		return preSale;
	}

	public void setPreSale(int preSale) {
		this.preSale = preSale;
	}

	public String getVd() {
		return vd;
	}

	public void setVd(String vd) {
		this.vd = vd;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public int getWish() {
		return wish;
	}

	public void setWish(int wish) {
		this.wish = wish;
	}

	public boolean isValue3d() {
		return value3d;
	}

	public void setValue3d(boolean value3d) {
		this.value3d = value3d;
	}

	public int getPn() {
		return pn;
	}

	public void setPn(int pn) {
		this.pn = pn;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public int getDur() {
		return dur;
	}

	public void setDur(int dur) {
		this.dur = dur;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getSc() {
		return sc;
	}

	public void setSc(int sc) {
		this.sc = sc;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
	}

	public String getShowDate() {
		return showDate;
	}

	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}

	public String getScm() {
		return scm;
	}

	public void setScm(String scm) {
		this.scm = scm;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
