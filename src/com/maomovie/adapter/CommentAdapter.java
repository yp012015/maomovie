package com.maomovie.adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.maomovie.R;
import com.maomovie.components.listsort.ViewHolder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by yanpeng on 2017/1/16.
 */
public class CommentAdapter extends BaseAdapter {
    private Context context;
    private JSONArray dataArray;
    private ClickImageCallBack callBack;

    public interface ClickImageCallBack {
        void clickImage(String avatarUrl);
    }

    public CommentAdapter(Context context, JSONArray dataArray,ClickImageCallBack callBack) {
        this.context = context;
        this.dataArray = dataArray;
        this.callBack = callBack;
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
        ImageView imageView = ViewHolder.get(convertView,R.id.ivPhoto1);
        final String avatarUrl = commentJsonObj.optString("avatarurl");
        if(avatarUrl.endsWith("__40465654__9539763.png")){
            imageView.setImageResource(R.drawable.default_avatar);
        } else {
            Glide.with(context).load(avatarUrl).thumbnail(0.1f)
                    .placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(imageView);
        }
        //点击图片监听器
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.clickImage(avatarUrl);
            }
        });
        tvNickname.setText(commentJsonObj.optString("nickName"));
        tvContent.setText(commentJsonObj.optString("content"));
        ratingBar.setRating((float) commentJsonObj.optDouble("score"));
        return convertView;
    }
}
