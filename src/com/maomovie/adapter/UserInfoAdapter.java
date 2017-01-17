package com.maomovie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.activity.UserInfoActivity;
import com.maomovie.components.listsort.ViewHolder;
import org.json.JSONObject;

/**
 * Created by yanpeng on 2017/1/9.
 * 用户信息资料
 */
public class UserInfoAdapter extends BaseAdapter {
    private JSONObject userInfo;
    private Context context;
    public UserInfoAdapter(JSONObject userJson,Context context) {
        this.context = context;
        userInfo = userJson;
    }

    @Override
    public int getCount() {
        return userInfo.length();
    }

    @Override
    public Object getItem(int position) {
        String value = "";
        int index = 1;
        while (UserInfoActivity.iterator.hasNext()){
            if(index == position){
                String key = UserInfoActivity.iterator.next();
                value = key +":   " +  userInfo.optString(key);
                break;
            }
            index ++;
        }
        UserInfoActivity.iterator = userInfo.keys();
        return value;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_cinemafillter, null);
        }
        TextView textView = ViewHolder.get(convertView,R.id.tvContent);
        String value = "";
        int index = 0;
        while (UserInfoActivity.iterator.hasNext()){
            if(index == position){
                String key = UserInfoActivity.iterator.next();
                value = key +":   " +  userInfo.optString(key);
                break;
            }
            index ++;
        }
        textView.setText(value);
        return convertView;
    }
}
