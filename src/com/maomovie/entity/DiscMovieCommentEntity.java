package com.maomovie.entity;
/**
 *功能：主界面"发现"电影资讯的实体内容
 */
public class DiscMovieCommentEntity {

	private String titile;//电影资讯标题
	private String time;//评论时间
	private int zan;//点赞数目
	private int comment;//评论数目
	
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getZan() {
		return zan;
	}
	public void setZan(int zan) {
		this.zan = zan;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	
	
}
