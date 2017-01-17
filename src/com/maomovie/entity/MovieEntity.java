package com.maomovie.entity;
/**
 * 影片详细信息实体
 */
public class MovieEntity {
	private String movieid;			//string	影片唯一标识ID
	private String actors;			//string	影片的演员列表。
	private String also_known_as;	//string	影片的其它名称。
	private String country	;		//string	影片的拍摄国家。
	private String directors;		//string	影片的导演列表。
	private String film_locations;	//string	影片的拍摄地。
	private String genres;			//string	影片的分类。（如：戏剧，战争）
	private String language;		//string	影片的对白使用的语言。
	private String plot_simple;		//String	影片的剧情概要。
	private String poster;			//String	影片的海报。
	private String rated;			//String	影片的分类与评级。
	private String rating;			//string	影片的得分。
	private String rating_count;	//string	影片的评分人数。
	private int release_date;		//Int		影片的上映时间。
 	private String runtime;			//string	影片的持续时间。
 	private String title;			//string	影片的名称。
 	private String type;			//string	影片类型.
 	private String writers;			//string	影片的编剧列表。
 	private int year;				//Int		影片的拍摄年代。

	/**
	 * 获取创建数据表的sql语句
	 * @return
	 */
	public static String getCreateTableSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS [MovieItem] (");
		sql.append("[No] integer PRIMARY KEY AUTOINCREMENT NOT NULL ,");
		sql.append("[movieid] varchar(60) ,");
		sql.append("[actors] varchar(60) ,");
		sql.append("[also_known_as] varchar(60) ,");
		sql.append("[country] varchar(60) ,");
		sql.append("[directors] varchar(60) ,");
		sql.append("[film_locations] varchar(60) ,");
		sql.append("[genres] varchar(60) ,");
		sql.append("[language] varchar(60) ,");
		sql.append("[plot_simple] varchar(60) ,");
		sql.append("[poster] varchar(60) ,");
		sql.append("[rated] varchar(60) ,");
		sql.append("[rating] varchar(60) ,");
		sql.append("[rating_count] varchar(60) ,");
		sql.append("[release_date] integer ,");
		sql.append("[runtime] varchar(60) ,");
		sql.append("[title] varchar(60) ,");
		sql.append("[type] varchar(60) ,");
		sql.append("[writers] varchar(60) ,");
		sql.append("[year] integer");
		sql.append(")");
		return sql.toString();
	}

	public String getMovieid() {
		return movieid;
	}
	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getAlso_known_as() {
		return also_known_as;
	}
	public void setAlso_known_as(String also_known_as) {
		this.also_known_as = also_known_as;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public String getFilm_locations() {
		return film_locations;
	}
	public void setFilm_locations(String film_locations) {
		this.film_locations = film_locations;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPlot_simple() {
		return plot_simple;
	}
	public void setPlot_simple(String plot_simple) {
		this.plot_simple = plot_simple;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getRated() {
		return rated;
	}
	public void setRated(String rated) {
		this.rated = rated;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getRating_count() {
		return rating_count;
	}
	public void setRating_count(String rating_count) {
		this.rating_count = rating_count;
	}
	public int getRelease_date() {
		return release_date;
	}
	public void setRelease_date(int release_date) {
		this.release_date = release_date;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWriters() {
		return writers;
	}
	public void setWriters(String writers) {
		this.writers = writers;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
 	
 	
}
