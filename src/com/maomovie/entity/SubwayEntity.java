package com.maomovie.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YanP on 2016/8/31.
 * 业务功能：统计影院地铁情况
 */
public class SubwayEntity implements Serializable {
    private String id;//地铁线路
    private List<Integer> positons;//记录含有地铁信息的影院在ListView中的位置
    private boolean isSelected;//是否选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getPositons() {
        return positons;
    }

    public void setPositons(List<Integer> positons) {
        this.positons = positons;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
