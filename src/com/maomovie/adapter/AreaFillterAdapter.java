package com.maomovie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.entity.CityEntity;

import java.util.List;

/**
 * Created by YanP on 2016/8/29.
 * 业务功能：影院过滤适配器
 */
public class AreaFillterAdapter extends BaseAdapter{

    private List<CityEntity.AreasBean> dataList;
    private Context context;
    private AreaFillterCallback mCallback;

    public AreaFillterAdapter(List<CityEntity.AreasBean> dataList, Context context, AreaFillterCallback callback) {
        this.dataList = dataList;
        this.context = context;
        mCallback = callback;
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface AreaFillterCallback {
        public void areaFillterCallback(View v, int position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final CityEntity.AreasBean areasBean = dataList.get(position);
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_cinemafillter,null);
            viewHolder = new ViewHolder();
            viewHolder.tvArea = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvArea.setText(areasBean.getNm() + "(" + areasBean.getCount() + ")");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先将所有的数据选中状态都设置为false
                for(CityEntity.AreasBean bean : dataList){
                    bean.setSelected(false);
                }
                areasBean.setSelected(true);
                mCallback.areaFillterCallback(v, position);
            }
        });
        if(areasBean.isSelected()){//将当前选中的字体变为红色
            viewHolder.tvArea.setTextColor(Color.RED);
        } else{//其他未选中的字体变为灰色
            viewHolder.tvArea.setTextColor(context.getResources().getColor(R.color.black_gray));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvArea;
    }
}
