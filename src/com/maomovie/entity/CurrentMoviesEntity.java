package com.maomovie.entity;

import java.io.Serializable;
import java.util.List;

/**
 *	影院上映影片信息
 */
public class CurrentMoviesEntity implements Serializable{

    /**
     * isShowing : true
     * nm : 头号玩家
     * img : http://p0.meituan.net/165.220/movie/a547dd7f6851d7ced67ec1b6c8b7f3b2447754.jpg
     * sc : 9.1
     * ver : 2D/3D/IMAX 3D
     * preferential : 0
     * id : 341178
     */

    private CurrentMovieBean currentMovie;
    /**
     * callboard :
     * brd : 太平洋电影城
     * dis : 华达商城
     * tel : ["028-85569009"]
     * suw : 暂无
     * dri : 暂无
     * note :
     * dealtp : 0
     * deals :
     * nm : 太平洋影城(武侯店)
     * lat : 30.64178
     * lng : 104.04204
     * sellmin : 1
     * sell : true
     * addr : 武侯区武侯祠大街266号华达商城4F（近一环路南四段）
     * imax : 0
     * snum : 30
     * s : 7.8
     * s1 : 7.0666666
     * s2 : 7.2
     * s3 : 7.3333335
     * featureTags : [{"tag":"可停车","desc":"华达商城停车场、人民商场武侯分场停车场、青田家私停车场，车位总计150","type":5},{"tag":"3D眼镜","desc":"无需押金","type":4},{"tag":"WiFi","desc":"影院提供免费WIFI","type":9}]
     * ct : 成都
     * price : 0
     * area : 武侯区
     * bus : 乘坐34/27/19/77/59/11/8/21/72/53/1/153/213/904路至高升桥站下车即是
     * park : 华达商城停车场、人民商场武侯分场停车场、青田家私停车场，车位总计150
     * preferential : 0
     * id : 1527
     */

    private CinemaDetailModelBean cinemaDetailModel;
    /**
     * grayDesc :
     * seatStatus : 1
     * isMorrow : false
     * th : 4号厅
     * sellPrStr : <span class="m3 true2"><i>38347</i></span>
     * prStr : <span class="m3 true2"><i>69907</i></span>
     * showId : 186514
     * showDate : 2018-04-03
     * end : 18:00
     * tm : 15:40
     * sell : true
     * dt : 2018-04-03
     * lang : 英语
     * tp : 3D
     * embed : 0
     * preferential : 0
     */
    private DateShowBean DateShow;

    public static class DateShowBean{
        private String grayDesc;
        private int seatStatus;
        private boolean isMorrow;
        private String th;
        private String sellPrStr;
        private String prStr;
        private int showId;
        private String showDate;
        private String end;
        private String tm;
        private boolean sell;
        private String dt;
        private String lang;
        private String tp;
        private int embed;
        private int preferential;

        public String getGrayDesc() {
            return grayDesc;
        }

        public void setGrayDesc(String grayDesc) {
            this.grayDesc = grayDesc;
        }

        public int getSeatStatus() {
            return seatStatus;
        }

        public void setSeatStatus(int seatStatus) {
            this.seatStatus = seatStatus;
        }

        public boolean isIsMorrow() {
            return isMorrow;
        }

        public void setIsMorrow(boolean isMorrow) {
            this.isMorrow = isMorrow;
        }

        public String getTh() {
            return th;
        }

        public void setTh(String th) {
            this.th = th;
        }

        public String getSellPrStr() {
            return sellPrStr;
        }

        public void setSellPrStr(String sellPrStr) {
            this.sellPrStr = sellPrStr;
        }

        public String getPrStr() {
            return prStr;
        }

        public void setPrStr(String prStr) {
            this.prStr = prStr;
        }

        public int getShowId() {
            return showId;
        }

        public void setShowId(int showId) {
            this.showId = showId;
        }

        public String getShowDate() {
            return showDate;
        }

        public void setShowDate(String showDate) {
            this.showDate = showDate;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getTm() {
            return tm;
        }

        public void setTm(String tm) {
            this.tm = tm;
        }

        public boolean isSell() {
            return sell;
        }

        public void setSell(boolean sell) {
            this.sell = sell;
        }

        public String getDt() {
            return dt;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getTp() {
            return tp;
        }

        public void setTp(String tp) {
            this.tp = tp;
        }

        public int getEmbed() {
            return embed;
        }

        public void setEmbed(int embed) {
            this.embed = embed;
        }

        public int getPreferential() {
            return preferential;
        }

        public void setPreferential(int preferential) {
            this.preferential = preferential;
        }
    }


    public static class CurrentMovieBean {
        private boolean isShowing;
        private String nm;
        private String img;
        private double sc;
        private String ver;
        private int preferential;
        private int id;

        public boolean isIsShowing() {
            return isShowing;
        }

        public void setIsShowing(boolean isShowing) {
            this.isShowing = isShowing;
        }

        public String getNm() {
            return nm;
        }

        public void setNm(String nm) {
            this.nm = nm;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public double getSc() {
            return sc;
        }

        public void setSc(double sc) {
            this.sc = sc;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public int getPreferential() {
            return preferential;
        }

        public void setPreferential(int preferential) {
            this.preferential = preferential;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class CinemaDetailModelBean {
        private String callboard;
        private String brd;
        private String dis;
        private String suw;
        private String dri;
        private String note;
        private int dealtp;
        private String deals;
        private String nm;
        private double lat;
        private double lng;
        private int sellmin;
        private boolean sell;
        private String addr;
        private int imax;
        private int snum;
        private double s;
        private double s1;
        private double s2;
        private double s3;
        private String ct;
        private int price;
        private String area;
        private String bus;
        private String park;
        private int preferential;
        private int id;
        private List<String> tel;
        /**
         * tag : 可停车
         * desc : 华达商城停车场、人民商场武侯分场停车场、青田家私停车场，车位总计150
         * type : 5
         */

        private List<FeatureTagsBean> featureTags;

        public String getCallboard() {
            return callboard;
        }

        public void setCallboard(String callboard) {
            this.callboard = callboard;
        }

        public String getBrd() {
            return brd;
        }

        public void setBrd(String brd) {
            this.brd = brd;
        }

        public String getDis() {
            return dis;
        }

        public void setDis(String dis) {
            this.dis = dis;
        }

        public String getSuw() {
            return suw;
        }

        public void setSuw(String suw) {
            this.suw = suw;
        }

        public String getDri() {
            return dri;
        }

        public void setDri(String dri) {
            this.dri = dri;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public int getDealtp() {
            return dealtp;
        }

        public void setDealtp(int dealtp) {
            this.dealtp = dealtp;
        }

        public String getDeals() {
            return deals;
        }

        public void setDeals(String deals) {
            this.deals = deals;
        }

        public String getNm() {
            return nm;
        }

        public void setNm(String nm) {
            this.nm = nm;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public int getSellmin() {
            return sellmin;
        }

        public void setSellmin(int sellmin) {
            this.sellmin = sellmin;
        }

        public boolean isSell() {
            return sell;
        }

        public void setSell(boolean sell) {
            this.sell = sell;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public int getImax() {
            return imax;
        }

        public void setImax(int imax) {
            this.imax = imax;
        }

        public int getSnum() {
            return snum;
        }

        public void setSnum(int snum) {
            this.snum = snum;
        }

        public double getS() {
            return s;
        }

        public void setS(double s) {
            this.s = s;
        }

        public double getS1() {
            return s1;
        }

        public void setS1(double s1) {
            this.s1 = s1;
        }

        public double getS2() {
            return s2;
        }

        public void setS2(double s2) {
            this.s2 = s2;
        }

        public double getS3() {
            return s3;
        }

        public void setS3(double s3) {
            this.s3 = s3;
        }

        public String getCt() {
            return ct;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBus() {
            return bus;
        }

        public void setBus(String bus) {
            this.bus = bus;
        }

        public String getPark() {
            return park;
        }

        public void setPark(String park) {
            this.park = park;
        }

        public int getPreferential() {
            return preferential;
        }

        public void setPreferential(int preferential) {
            this.preferential = preferential;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<String> getTel() {
            return tel;
        }

        public void setTel(List<String> tel) {
            this.tel = tel;
        }

        public List<FeatureTagsBean> getFeatureTags() {
            return featureTags;
        }

        public void setFeatureTags(List<FeatureTagsBean> featureTags) {
            this.featureTags = featureTags;
        }

        public static class FeatureTagsBean {
            private String tag;
            private String desc;
            private int type;

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
