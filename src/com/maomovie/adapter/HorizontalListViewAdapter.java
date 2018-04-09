package com.maomovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.maomovie.R;
import com.maomovie.entity.CurrentMoviesEntity;

import java.util.List;

/**
 * Created by yanpeng on 2018/4/3.
 * 横向listview的适配器
 */
public class HorizontalListViewAdapter extends BaseAdapter {
    private List<CurrentMoviesEntity.CurrentMovieBean> dataList;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context, List<CurrentMoviesEntity.CurrentMovieBean> dataList){
        this.mContext = context;
        this.dataList = dataList;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return dataList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        CurrentMoviesEntity.CurrentMovieBean movieBean = dataList.get(position);
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
            holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(position == selectIndex){
            convertView.setSelected(true);
//            holder.mImage.setMaxHeight(100);
//            holder.mImage.setMaxWidth(60);
        }else{
            convertView.setSelected(false);
        }

        //这句代码的作用是为了解决convertView被重用的时候，图片预设的问题
        holder.mImage.setImageResource(R.drawable.cartoon_list_item_default);

        Glide.with(mContext).load(movieBean.getImg()).thumbnail(0.4f)
                .placeholder(R.drawable.cartoon_list_item_default).error(R.drawable.cartoon_list_item_default).into(holder.mImage);

        return convertView;
    }

    private static class ViewHolder {
        private ImageView mImage;
    }
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}
