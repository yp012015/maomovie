package com.maomovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.maomovie.R;
import com.maomovie.activity.MovieTheaterActivity;
import com.maomovie.components.listsort.ViewHolder;
import com.maomovie.entity.TodayMovieEntity;

import java.util.List;

/**
 * Created by YanP on 2016/4/27.
 * 业务功能：主页中电影的listView适配器
 */
public class FragmentMovieAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private boolean scrollState = false;
    private List<TodayMovieEntity> todayMovies;
    private Context context;
    private Callback mCallback;


    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface Callback {
        public void click(View v, int position);
    }

    public FragmentMovieAdapter(Context context, List<TodayMovieEntity> todayMovies, Callback callback) {
        mInflater = LayoutInflater.from(context);
        this.todayMovies = todayMovies;
        this.context = context;
        mCallback = callback;
    }

    public void setScrollState(boolean scrollState) {
        this.scrollState = scrollState;
    }

    public void addFirst(TodayMovieEntity entity) {
        todayMovies.add(0, entity);
    }

    public void addLast(TodayMovieEntity entity) {
        todayMovies.add(entity);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return todayMovies.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return todayMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final TodayMovieEntity movieEntity = todayMovies.get(position);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_movie, null);
        }
        TextView title = ViewHolder.get(convertView,R.id.title);
        TextView content =  ViewHolder.get(convertView,R.id.content);
        TextView status = ViewHolder.get(convertView,R.id.state);
        TextView mark = ViewHolder.get(convertView,R.id.tvMark);
        TextView icMark = ViewHolder.get(convertView,R.id.icMark);
        ImageView ivPhoto = ViewHolder.get(convertView,R.id.moviePhoto);
        Button btnBuy = ViewHolder.get(convertView,R.id.btn_BuyTicket);
        title.setText(movieEntity.getNm());
        String url = movieEntity.getImg();

        content.setText(movieEntity.getScm());
        status.setText(movieEntity.getShowInfo());
        mark.setText(String.valueOf(movieEntity.getSc()));
        ivPhoto.setTag(url);
        //这句代码的作用是为了解决convertView被重用的时候，图片预设的问题
        ivPhoto.setImageResource(R.drawable.cartoon_list_item_default);

        if(movieEntity.getPreSale() == 1){//预售标识 1-预售 0-正常
            icMark.setVisibility(View.INVISIBLE);
            mark.setVisibility(View.INVISIBLE);
            btnBuy.setBackgroundResource(R.drawable.btn_buyticket_blue_selector);
            btnBuy.setText("预售");
            btnBuy.setTextColor(context.getResources().getColor(R.color.color_buyticketbtn_blue));
        } else {
            icMark.setVisibility(View.VISIBLE);
            mark.setVisibility(View.VISIBLE);
            btnBuy.setBackgroundResource(R.drawable.btn_buyticket_bg);
            btnBuy.setText("购票");
            btnBuy.setTextColor(context.getResources().getColor(R.color.color_buyticketbtn));
        }

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v, position);
            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,position);
            }
        });
        return convertView;
    }

}
