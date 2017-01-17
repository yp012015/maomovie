package com.maomovie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.entity.CinemaEntity;
import com.maomovie.entity.SubwayEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YanP on 2016/8/30.
 * 业务功能：根据地铁信息过滤影院的适配器
 */
public class SubwayFillterAdapter extends BaseAdapter {
    private Context context;
    public List<SubwayEntity> subwayList;
    private SubwayFillterCallback mCallback;

    public SubwayFillterAdapter(List<CinemaEntity> dataList, Context context,SubwayFillterCallback callback) {
        subwayList = new ArrayList<SubwayEntity>();
        //对dataList遍历，找出有地铁信息的数据
        for (int i = dataList.size() - 1; i >= 0; i--) {
            CinemaEntity entity = dataList.get(i);
            if (entity.getSuw() == null || entity.getSuw().equals("")
                    || entity.getSuw().equals("暂无") || !entity.getSuw().contains("地铁")) {
            } else {
                String suwStr = entity.getSuw();
                if (suwStr != null && !suwStr.equals("")) {
                    //从地铁信息字段中截取地铁线路
                    int start = suwStr.indexOf("地铁");
                    int end = suwStr.indexOf("号线", start) + 2;
                    if (start != -1 && end != 1) {
                        suwStr = suwStr.substring(start, end);
                        suwStr = suwStr.replace("一", "1");
                        suwStr = suwStr.replace("二", "2");
                        suwStr = suwStr.replace("三", "3");
                        suwStr = suwStr.replace("四", "4");
                        suwStr = suwStr.replace("五", "5");
                        suwStr = suwStr.replace("六", "6");
                        suwStr = suwStr.replace("七", "7");
                        suwStr = suwStr.replace("八", "8");
                        suwStr = suwStr.replace("九", "9");
                    } else {
                        dataList.remove(i);
                    }
                    //统计ListView中哪些位置的影院有地铁
                    boolean flag = false;
                    for (SubwayEntity subwayEntity : subwayList) {
                        if (subwayEntity.getId().equals(suwStr)) {
                            List<Integer> positons = subwayEntity.getPositons();
                            positons.add(i);
                            subwayEntity.setPositons(positons);
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        SubwayEntity subwayEntity = new SubwayEntity();
                        subwayEntity.setId(suwStr);
                        List<Integer> positons = new ArrayList<Integer>();
                        positons.add(i);
                        subwayEntity.setPositons(positons);
                        subwayList.add(subwayEntity);
                    }
                }
            }
        }
        this.context = context;
        mCallback = callback;
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface SubwayFillterCallback {
        public void subwayFillterCallback(View v, int position);
    }

    @Override
    public int getCount() {
        return subwayList.size();
    }

    @Override
    public Object getItem(int position) {
        return subwayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final SubwayEntity entity = subwayList.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cinemafillter, null);
            viewHolder = new ViewHolder();
            viewHolder.tvSubway = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String suwStr = entity.getId();
        viewHolder.tvSubway.setText(suwStr);
        //添加点击监听事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将所有的数据源都置为未选中状态
                for(SubwayEntity subwayEntity : subwayList){
                    subwayEntity.setIsSelected(false);
                }
                entity.setIsSelected(true);
                mCallback.subwayFillterCallback(v, position);
            }
        });
        if(entity.isSelected()){//选中状态
            viewHolder.tvSubway.setTextColor(Color.RED);
        } else {
            viewHolder.tvSubway.setTextColor(context.getResources().getColor(R.color.black_gray));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvSubway;
    }
}
