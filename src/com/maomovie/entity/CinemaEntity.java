package com.maomovie.entity;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.maomovie.context.MaoApplication;

import java.io.Serializable;
import java.util.List;

/**
 * 影院信息实体
 */
public class CinemaEntity implements Serializable,Comparable<CinemaEntity> {

    /**
     * sellPrice : 28
     * bus :
     * park : 影城所在广场有停车位
     * tel : 028-84820378
     * img : http://p0.meituan.net/deal/68a12719c98ceca48c501b4c5c81fff2307145.jpg
     * addr : 龙泉驿区锦绣路176号阳光商业世界（安阳商务酒店二楼）
     * lng : 104.30801
     * deals : [{"title":"12元，爆米花1桶+矿泉水1瓶1次","price":12,"dealId":36806913,"dealUrl":"http://www.meituan.com/deal/36806913.html"},{"title":"17.5元，爆米花1次+矿泉水2瓶","price":17.5,"dealId":39684799,"dealUrl":"http://www.meituan.com/deal/39684799.html"},{"title":"30元，电影票1张，可观看2D/3D，提供免费WiFi","price":30,"dealId":28842509,"dealUrl":"http://www.meituan.com/deal/28842509.html"}]
     * ct : 成都
     * areaId : 510112
     * dri :
     * id : 13356
     * referencePrice : 0
     * area : 龙泉驿区
     * dealPrice : 30
     * deal : 1
     * sell : 1
     * suw :
     * dis :
     * feature :
     * imax : 0
     * note : 周二周三影城现金购票6折
     * lat : 30.605047
     * nm : 龙泉维多利国际电影城
     * brd : 其它
     * ctid : 59
     */

    private String addr;            // 地址
    private String area;            // 行政区
    private int areaId;             // 行政区id
    private String brd;             // 品牌
    private String bus;             // 公交信息
    private String ct;              // 城市名
    private int ctid;               // 城市id
    private int deal;               // 是否团购 0否 1是
    private int dealPrice;          // 团购起始价
    private String dis;             // 商圈
    private String dri;             // 自驾信息
    private String feature;         // 影院特色
    private int id;                 // 影院id
    private int imax;               // 是否imax 0否 1是
    private String img;             // 影院图片
    private double lat;             // 经度值
    private double lng;             // 纬度值
    private String nm;              // 影院名称
    private String note;            // 特殊说明
    private String park;            // 停车位信息
    private int referencePrice;     // 参考价
    private int sell;               // 是否选座 0否 1是
    private int sellPrice;          // 选座起始价
    private String suw;             // 地铁信息
    private String tel;             // 电话

    private Integer distance;         // 与当前位置的距离

    /**
     * title : 12元，爆米花1桶+矿泉水1瓶1次
     * price : 12
     * dealId : 36806913
     * dealUrl : http://www.meituan.com/deal/36806913.html
     */

    private List<DealsBean> deals;//团购单列表

    /**
     * 返回创建数据表的sql语句
     *
     * @return
     */
    public static String getCreateTableSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS [Cinema](");
        sql.append("[No] integer PRIMARY KEY AUTOINCREMENT NOT NULL,");
        sql.append("[id] interger , ");
        sql.append("[addr] varchar(60),");
        sql.append("[area] varchar(60),");
        sql.append("[areaId] interger ,");
        sql.append("[brd] varchar(60),");
        sql.append("[bus] varchar(60),");
        sql.append("[ct] varchar(60),");
        sql.append("[ctid] interger,");
        sql.append("[deal] interger,");
        sql.append("[dealPrice] interger,");
        sql.append("[dis] varchar(60),");
        sql.append("[dri] varchar(60),");
        sql.append("[feature] varchar(60),");
        sql.append("[imax] interger,");
        sql.append("[img] varchar(60),");
        sql.append("[lat] double ,");
        sql.append("[lng] double ,");
        sql.append("[nm] varchar(60),");
        sql.append("[note] varchar(60),");
        sql.append("[park] varchar(60),");
        sql.append("[referencePrice] interger,");
        sql.append("[sell] interger,");
        sql.append("[sellPrice] interger,");
        sql.append("[suw] varchar(60),");
        sql.append("[tel] varchar(60),");
        sql.append("[deals] txt");
        sql.append(");");
        return sql.toString();
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getDri() {
        return dri;
    }

    public void setDri(String dri) {
        this.dri = dri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(int referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(int dealPrice) {
        this.dealPrice = dealPrice;
    }

    public int getDeal() {
        return deal;
    }

    public void setDeal(int deal) {
        this.deal = deal;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }

    public String getSuw() {
        return suw;
    }

    public void setSuw(String suw) {
        this.suw = suw;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getImax() {
        return imax;
    }

    public void setImax(int imax) {
        this.imax = imax;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getBrd() {
        return brd;
    }

    public void setBrd(String brd) {
        this.brd = brd;
    }

    public int getCtid() {
        return ctid;
    }

    public void setCtid(int ctid) {
        this.ctid = ctid;
    }

    public List<DealsBean> getDeals() {
        return deals;
    }

    public void setDeals(List<DealsBean> deals) {
        this.deals = deals;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(double lat,double lng) {
        LatLng latLng1 = new LatLng(lat,lng);
        LatLng latLng2 = null;
        try {
            latLng2 = new LatLng(MaoApplication.currentLocation.getLatitude(),MaoApplication.currentLocation.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(latLng2 == null){
            this.distance = 0;
        }
        this.distance = (int)AMapUtils.calculateLineDistance(latLng1,latLng2);
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(CinemaEntity another) {
        return this.distance.compareTo(another.getDistance());
    }

    public static class DealsBean implements Serializable{
        private String title;//团购单标题
        private int price;//团购价格
        private int dealId;//单子id
        private String dealUrl;//购买链接

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getDealId() {
            return dealId;
        }

        public void setDealId(int dealId) {
            this.dealId = dealId;
        }

        public String getDealUrl() {
            return dealUrl;
        }

        public void setDealUrl(String dealUrl) {
            this.dealUrl = dealUrl;
        }
    }
}
