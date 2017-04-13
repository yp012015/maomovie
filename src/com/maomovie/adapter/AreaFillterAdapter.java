package com.maomovie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.components.listsort.ViewHolder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by YanP on 2016/8/29.
 * 业务功能：影院过滤适配器
 */
public class AreaFillterAdapter extends BaseAdapter{

    private JSONObject dataJSON;
    private Iterator<String> iterator;
    private Context context;
    private AreaFillterCallback mCallback;
    private boolean isClidked = false;
    private int clickedPosition = 0;

    public AreaFillterAdapter(JSONObject dataJSON, Context context, AreaFillterCallback callback) {
        if (dataJSON != null) {
            iterator = dataJSON.keys();
        }
        this.dataJSON = dataJSON;
        this.context = context;
        mCallback = callback;
    }

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface AreaFillterCallback {
        public void areaFillterCallback(View v, String key,int position);
    }

    @Override
    public int getCount() {
        if(dataJSON == null) return 0;
        return dataJSON.length();
    }

    @Override
    public Object getItem(int position) {
        if(dataJSON == null ) return null;
        int amcount = 0;
        JSONArray jsonArray = null;
        while (iterator.hasNext()){
            if(amcount++ == position){
                String key = iterator.next();
                jsonArray = dataJSON.optJSONArray(key);
                break;
            }
        }
        return jsonArray;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_cinemafillter,null);
        }
        int index = 0;
        JSONArray jsonArray = null;
        String key = "";
        iterator = this.dataJSON.keys();
        while (iterator.hasNext()){
            key = iterator.next();
            if(index++ == position){
                jsonArray = dataJSON.optJSONArray(key);
                break;
            }
        }
        TextView tvContent = ViewHolder.get(convertView, R.id.tvContent);
        if(jsonArray != null){
            tvContent.setText(key + "(" + jsonArray.length() + ")");
        }
        final String areaName = key;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.areaFillterCallback(v, areaName, position);
                isClidked = true;
                clickedPosition = position;
            }
        });
        tvContent.setTextColor(Color.GRAY);
        if(isClidked && position == clickedPosition){
            tvContent.setTextColor(Color.RED);
        }
        return convertView;
    }
}
