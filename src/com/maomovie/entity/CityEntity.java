package com.maomovie.entity;

import java.util.List;

/**
 * Created by YanP on 2016/8/29.
 * 业务功能：猫眼电影支持的城市
 */
public class CityEntity {

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

    public static class AreasBean {
        private int id;
        private String nm;
        private int count;//临时添加的数目信息
        private boolean selected = false;//是否选中

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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
