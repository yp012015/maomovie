package com.maomovie.entity;

import java.util.List;

/**
 * Created by yanpeng on 2016/12/16.
 * 正在热播的电影
 */
public class HotMovie {

    /**
     * boxInfo : 上映1天，累计票房9942万
     * cat : 剧情,冒险,奇幻
     * civilPubSt : 0
     * desc : 主演:马特·达蒙,景甜,佩德罗·帕斯卡
     * dir : 张艺谋
     * dur : 104
     * effectShowNum : 0
     * fra : 美国
     * frt : 2017-02-17
     * globalReleased : true
     * headLineShow : false
     * headLines : []
     * headLinesVO : [{"movieId":246267,"title":"张艺谋：如果《长城》大卖，就会拍续集","type":"资讯","url":"meituanmovie://www.meituan.com/forum/newsDetail?id=17472"},{"movieId":246267,"title":"马特·达蒙：为电影自创了一套英语口音","type":"专访","url":"meituanmovie://www.meituan.com/forum/newsDetail?id=17422"}]
     * id : 246267
     * img : http://p0.meituan.net/w.h/movie/e4a3447ebe8c44eea59ab7f68790c7e2179321.jpeg
     * late : false
     * localPubSt : 0
     * mk : 8.3
     * newsHeadlines : [{"movieId":246267,"title":"张艺谋：如果《长城》大卖，就会拍续集","type":"资讯","url":"meituanmovie://www.meituan.com/forum/newsDetail?id=17472"},{"movieId":246267,"title":"马特·达蒙：为电影自创了一套英语口音","type":"专访","url":"meituanmovie://www.meituan.com/forum/newsDetail?id=17422"}]
     * nm : 长城
     * pn : 171
     * preSale : 0
     * preShow : false
     * proScore : 5.2
     * proScoreNum : 24
     * pubDate : 1481817600000
     * pubShowNum : 0
     * recentShowDate : 1481817600000
     * recentShowNum : 0
     * rt : 2016-12-16
     * sc : 8.3
     * scm : 五军战饕餮，中国魂不灭
     * showInfo : 今天117家影院放映2402场
     * showNum : 2402
     * showTimeInfo : 2016-12-16 本周五上映
     * showst : 3
     * snum : 43348
     * star : 马特·达蒙,景甜,佩德罗·帕斯卡
     * totalShowNum : 5070
     * ver : 2D/3D
     * videoId : 82447
     * videoName : 希望版预告片
     * videourl : http://maoyan.meituan.net/movie/videos/854x4804781df4753a148648c534e4e8531a3ff.mp4
     * vnum : 35
     * weight : 1
     * wish : 242315
     * wishst : 0
     */

    private String boxInfo;
    private String cat;
    private int civilPubSt;
    private String desc;
    private String dir;
    private int dur;
    private int effectShowNum;
    private String fra;
    private String frt;
    private boolean globalReleased;
    private boolean headLineShow;
    private int id;
    private String img;
    private boolean late;
    private int localPubSt;
    private double mk;
    private String nm;
    private int pn;
    private int preSale;
    private boolean preShow;
    private double proScore;
    private int proScoreNum;
    private long pubDate;
    private int pubShowNum;
    private long recentShowDate;
    private int recentShowNum;
    private String rt;
    private double sc;
    private String scm;
    private String showInfo;
    private int showNum;
    private String showTimeInfo;
    private int showst;
    private int snum;
    private String star;
    private int totalShowNum;
    private String ver;
    private int videoId;
    private String videoName;
    private String videourl;
    private int vnum;
    private int weight;
    private int wish;
    private int wishst;
    private List<?> headLines;
    /**
     * movieId : 246267
     * title : 张艺谋：如果《长城》大卖，就会拍续集
     * type : 资讯
     * url : meituanmovie://www.meituan.com/forum/newsDetail?id=17472
     */

    private List<HeadLinesVOBean> headLinesVO;
    /**
     * movieId : 246267
     * title : 张艺谋：如果《长城》大卖，就会拍续集
     * type : 资讯
     * url : meituanmovie://www.meituan.com/forum/newsDetail?id=17472
     */

    private List<NewsHeadlinesBean> newsHeadlines;

    public String getBoxInfo() {
        return boxInfo;
    }

    public void setBoxInfo(String boxInfo) {
        this.boxInfo = boxInfo;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public int getCivilPubSt() {
        return civilPubSt;
    }

    public void setCivilPubSt(int civilPubSt) {
        this.civilPubSt = civilPubSt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getDur() {
        return dur;
    }

    public void setDur(int dur) {
        this.dur = dur;
    }

    public int getEffectShowNum() {
        return effectShowNum;
    }

    public void setEffectShowNum(int effectShowNum) {
        this.effectShowNum = effectShowNum;
    }

    public String getFra() {
        return fra;
    }

    public void setFra(String fra) {
        this.fra = fra;
    }

    public String getFrt() {
        return frt;
    }

    public void setFrt(String frt) {
        this.frt = frt;
    }

    public boolean isGlobalReleased() {
        return globalReleased;
    }

    public void setGlobalReleased(boolean globalReleased) {
        this.globalReleased = globalReleased;
    }

    public boolean isHeadLineShow() {
        return headLineShow;
    }

    public void setHeadLineShow(boolean headLineShow) {
        this.headLineShow = headLineShow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isLate() {
        return late;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public int getLocalPubSt() {
        return localPubSt;
    }

    public void setLocalPubSt(int localPubSt) {
        this.localPubSt = localPubSt;
    }

    public double getMk() {
        return mk;
    }

    public void setMk(double mk) {
        this.mk = mk;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getPreSale() {
        return preSale;
    }

    public void setPreSale(int preSale) {
        this.preSale = preSale;
    }

    public boolean isPreShow() {
        return preShow;
    }

    public void setPreShow(boolean preShow) {
        this.preShow = preShow;
    }

    public double getProScore() {
        return proScore;
    }

    public void setProScore(double proScore) {
        this.proScore = proScore;
    }

    public int getProScoreNum() {
        return proScoreNum;
    }

    public void setProScoreNum(int proScoreNum) {
        this.proScoreNum = proScoreNum;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public int getPubShowNum() {
        return pubShowNum;
    }

    public void setPubShowNum(int pubShowNum) {
        this.pubShowNum = pubShowNum;
    }

    public long getRecentShowDate() {
        return recentShowDate;
    }

    public void setRecentShowDate(long recentShowDate) {
        this.recentShowDate = recentShowDate;
    }

    public int getRecentShowNum() {
        return recentShowNum;
    }

    public void setRecentShowNum(int recentShowNum) {
        this.recentShowNum = recentShowNum;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public double getSc() {
        return sc;
    }

    public void setSc(double sc) {
        this.sc = sc;
    }

    public String getScm() {
        return scm;
    }

    public void setScm(String scm) {
        this.scm = scm;
    }

    public String getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(String showInfo) {
        this.showInfo = showInfo;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public String getShowTimeInfo() {
        return showTimeInfo;
    }

    public void setShowTimeInfo(String showTimeInfo) {
        this.showTimeInfo = showTimeInfo;
    }

    public int getShowst() {
        return showst;
    }

    public void setShowst(int showst) {
        this.showst = showst;
    }

    public int getSnum() {
        return snum;
    }

    public void setSnum(int snum) {
        this.snum = snum;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public int getTotalShowNum() {
        return totalShowNum;
    }

    public void setTotalShowNum(int totalShowNum) {
        this.totalShowNum = totalShowNum;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public int getVnum() {
        return vnum;
    }

    public void setVnum(int vnum) {
        this.vnum = vnum;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWish() {
        return wish;
    }

    public void setWish(int wish) {
        this.wish = wish;
    }

    public int getWishst() {
        return wishst;
    }

    public void setWishst(int wishst) {
        this.wishst = wishst;
    }

    public List<?> getHeadLines() {
        return headLines;
    }

    public void setHeadLines(List<?> headLines) {
        this.headLines = headLines;
    }

    public List<HeadLinesVOBean> getHeadLinesVO() {
        return headLinesVO;
    }

    public void setHeadLinesVO(List<HeadLinesVOBean> headLinesVO) {
        this.headLinesVO = headLinesVO;
    }

    public List<NewsHeadlinesBean> getNewsHeadlines() {
        return newsHeadlines;
    }

    public void setNewsHeadlines(List<NewsHeadlinesBean> newsHeadlines) {
        this.newsHeadlines = newsHeadlines;
    }

    public static class HeadLinesVOBean {
        private int movieId;
        private String title;
        private String type;
        private String url;

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class NewsHeadlinesBean {
        private int movieId;
        private String title;
        private String type;
        private String url;

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
