package com.maomovie.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.maomovie.R;
import com.maomovie.components.listsort.ViewHolder;
import com.maomovie.util.ToastUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yanpeng on 2017/1/16.
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private JSONArray dataArray;

    public CommentAdapter(Context context, JSONArray dataArray) {
        this.context = context;
        this.dataArray = dataArray;
    }

    @Override
    public int getCount() {
        return dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return dataArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.comment_adapter_item,null);
        }
        JSONObject commentJsonObj = dataArray.optJSONObject(position);
        //评论者昵称
        TextView tvNickname = ViewHolder.get(convertView, R.id.tvCommentator1);
        //评论内容
        TextView tvContent = ViewHolder.get(convertView,R.id.tvComment1);
        //评分
        RatingBar ratingBar = ViewHolder.get(convertView,R.id.ratingBar1);
        tvNickname.setText(commentJsonObj.optString("nickName"));
        tvContent.setText(commentJsonObj.optString("content"));
        ratingBar.setRating((float) commentJsonObj.optDouble("score"));
        return convertView;
    }
}
