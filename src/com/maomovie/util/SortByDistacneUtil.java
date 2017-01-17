package com.maomovie.util;

import com.maomovie.activity.mainfragment.CinemaFragment;
import com.maomovie.context.MaoApplication;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.CityEntity;

import java.util.Collections;
import java.util.List;

/**
 * Created by YanP on 2016/8/25.
 * 业务功能：通过经纬度计算出影院与当前位置的距离，然后根据距离升序排序
 */
public class SortByDistacneUtil {
    /**
     * 按位置远近对影院进行排序
     *
     * @param list
     */
    public static boolean sort(List<CinemaEntity> list) {
        if (MaoApplication.currentLocation == null || MaoApplication.currentLocation.getErrorCode() != 0) {
            return false;
        }
        CinemaFragment.areaList.clear();
        //添加一个【全部】
        CityEntity.AreasBean areasBean = new CityEntity.AreasBean();
        areasBean.setId(0);
        areasBean.setNm("全部");
        areasBean.setCount(list.size());
        CinemaFragment.areaList.add(areasBean);
        for (int i = list.size()-1; i >=0; i--) {
            CinemaEntity entity = list.get(i);//当前影院
            entity.setDistance(entity.getLat(),entity.getLng());
            boolean isExisted = false;
            //如果之前已经录入过改行政区域，那么数量+1
            for(CityEntity.AreasBean bean : CinemaFragment.areaList){
                if(bean.getId() == entity.getAreaId()){
                    int count = bean.getCount();
                    bean.setCount((count + 1));
                    isExisted = true;
                    break;
                }
            }
            //否则，新建数据
            if(!isExisted){
                areasBean = new CityEntity.AreasBean();
                areasBean.setId(entity.getAreaId());
                areasBean.setNm(entity.getArea());
                areasBean.setCount(1);
                CinemaFragment.areaList.add(areasBean);
            }
        }
        Collections.sort(list);
        return true;
    }
}
